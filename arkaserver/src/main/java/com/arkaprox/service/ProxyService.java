package com.arkaprox.service;

import com.arkacommon.file.FilesWriter;
import com.arkacommon.model.ProxyData;
import com.arkacommon.utils.ProxyConfigUtils;
import com.arkaprox.connection.ProxyConnection;
import com.google.gson.Gson;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProxyService {

    private final Map<String, Process> proxyProcesses = new HashMap<>();

    private final ProxyConnection proxyConnection = ProxyConnection.getInstance();

    private final RestTemplate restTemplate;

    public ProxyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void updateProxyConfig(ProxyData proxyData) throws IOException {
        FilesWriter.updateProxyConfig(proxyData);
        FilesWriter.updateMiniProxiesConfig(proxyData);
    }

    public void runProxy(String proxyName) throws Exception {
        String configDirPath = "config";
        String configFilePath = configDirPath + "/" + proxyName + ".conf";
        String configFileContent = new String(Files.readAllBytes(Paths.get(configFilePath)), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        ProxyData proxyData = gson.fromJson(configFileContent, ProxyData.class);
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "arkaproxy/build/libs/proxy.jar",
                proxyName, String.valueOf(proxyData.getProxyPort()), String.valueOf(proxyData.getDbPort()));
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process proxyProcess = processBuilder.start();
        proxyProcesses.put(proxyName, proxyProcess);
        proxyData.setEnable(true);
        FilesWriter.updateProxyConfig(proxyData);
        System.out.println("Proxy " + proxyName + " running on the port: " + proxyData.getProxyPort());
    }

    public void stopProxy(String proxyName) throws IOException {
        Process proxyProcess = proxyProcesses.get(proxyName);
        if (proxyProcess != null) {
            proxyProcess.destroy();
            proxyProcesses.remove(proxyName);
            ProxyData proxyData = ProxyConfigUtils.getProxyByName(proxyName);
            proxyData.setEnable(false);
            FilesWriter.updateProxyConfig(proxyData);
            System.out.println("Proxy " + proxyName + " has been stopped.");
        } else {
            System.out.println("An error occured during turning down the proxy: " + proxyName + ".");
        }
    }


    public ProxyData getProxyByName(String proxyName) {
        return ProxyConfigUtils.getProxyByName(proxyName);
    }

    public List<ProxyData> getMiniProxiesList() throws FileNotFoundException {
        return ProxyConfigUtils.getMiniProxiesList();
    }

    public List<String> getActiveProxiesNamesList() throws FileNotFoundException {
        return getMiniProxiesList().stream()
                .map(ProxyData::getProxyName)
                .filter(this::isProxyEnabled)
                .collect(Collectors.toList());
    }

    private Boolean isProxyEnabled(String proxyName) {
        return ProxyConfigUtils.getProxyByName(proxyName).getEnable();
    }

    public Map<String, Map<String, Long>> getQueriesNumberForAllProxies() throws FileNotFoundException {
        List<String> activeProxiesNames = getActiveProxiesNamesList();
        Map<String, Map<String, Long>> resultMap = new HashMap<>();

        for (String proxyName : activeProxiesNames) {
            Integer appPort = getProxyByName(proxyName).getAppPort();
            if (appPort != null) {
                String remoteEndpointUrl = "http://localhost:" + appPort + "/queries-count";

                ResponseEntity<Map<String, Long>> responseEntity = restTemplate.exchange(
                        remoteEndpointUrl,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

                resultMap.put(proxyName, responseEntity.getBody());
            }
        }

        return resultMap;
    }
}
