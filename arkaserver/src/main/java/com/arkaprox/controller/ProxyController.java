package com.arkaprox.controller;

import com.arkaprox.proxy.ProxyData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/proxy")
public class ProxyController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/queries-count")
    public Map<String, Long> getRemoteQueriesNumber() {
        String remoteEndpointUrl = "http://localhost:1111/queries-count";
        ResponseEntity<Map<String, Long>> responseEntity = restTemplate.exchange(
                remoteEndpointUrl,
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        return responseEntity.getBody();
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createProxyConfig(@RequestBody ProxyData proxyData) {
        try {
            String configDirPath = "config";
            Path configDir = Paths.get(configDirPath);
            if (!Files.exists(configDir)) {
                Files.createDirectory(configDir);
            }
            String proxyConfigFilePath = configDirPath + "/" + proxyData.getProxyName() + ".conf";
            String proxiesConfigFilePath = configDirPath + "/proxies.conf";
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            ProxyData miniProxyData = new ProxyData();
            miniProxyData.setProxyName(proxyData.getProxyName());
            miniProxyData.setProxyPort(proxyData.getProxyPort());

            Files.write(Paths.get(proxyConfigFilePath), gson.toJson(proxyData).getBytes());
            Files.write(Paths.get(proxiesConfigFilePath), gson.toJson(miniProxyData).getBytes());
            return new ResponseEntity<>("Proxy configuration created successfully", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error creating proxy configuration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private final Map<Integer, Process> proxyProcesses = new HashMap<>();

    @GetMapping("/start")
    public void startProxy(@RequestParam Integer proxyPort, @RequestParam String proxyName)
            throws IOException, InterruptedException {
        String configDirPath = "config";
        String configFilePath = configDirPath + "/" + proxyName + ".conf";
        String configFileContent = new String(Files.readAllBytes(Paths.get(configFilePath)), StandardCharsets.UTF_8);
        Gson gson = new Gson();
        ProxyData proxyData = gson.fromJson(configFileContent, ProxyData.class);
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "arkaproxy/build/libs/proxy.jar",
                proxyName, String.valueOf(proxyPort), String.valueOf(proxyData.getDbPort()));
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process proxyProcess = processBuilder.start();
        proxyProcesses.put(proxyPort, proxyProcess);
        int exitCode = proxyProcess.waitFor();
        System.out.println("Proxy uruchomiono na porcie " + proxyPort + ". Kod zakończenia: " + exitCode);
    }

    @GetMapping("/stop")
    public void stopProxy(@RequestParam Integer proxyPort) {
        Process proxyProcess = proxyProcesses.get(proxyPort);
        if (proxyProcess != null) {
            proxyProcess.destroy();
            proxyProcesses.remove(proxyPort);
            System.out.println("Proxy na porcie " + proxyPort + " zatrzymano.");
        } else {
            System.out.println("Problem przy zatrzymywaniu proxy na porcie " + proxyPort + ".");
        }
    }


    @GetMapping(value = "/read/{proxyName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProxyData> readProxyConfig(@PathVariable String proxyName) {
        try {
            String configDirPath = "config";
            String configFilePath = configDirPath + "/" + proxyName + ".conf";
            String configFileContent = new String(Files.readAllBytes(Paths.get(configFilePath)), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            ProxyData proxyData = gson.fromJson(configFileContent, ProxyData.class);
            return new ResponseEntity<>(proxyData, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/proxies-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProxyData>> getProxiesList() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("config/proxies.conf"));
            Type listType = new TypeToken<List<ProxyData>>() {}.getType();
            List<ProxyData> proxiesList = new Gson().fromJson(br, listType);

            return ResponseEntity.ok().body(proxiesList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/random-number")
    public int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(10) + 1; // Losowa liczba od 1 do 10
    }
}