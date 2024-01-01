package com.arkacommon.file;

import com.arkacommon.model.ProxyData;
import com.arkacommon.utils.ProxyConfigUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FilesWriter {

    public static void updateMiniProxiesConfig(ProxyData proxyData) throws IOException {
        String configDirPath = "config";
        Path configDir = Paths.get(configDirPath);
        if (!Files.exists(configDir)) {
            Files.createDirectory(configDir);
        }
        String proxiesConfigFilePath = configDirPath + "/proxies.conf";
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        List<ProxyData> miniProxies = ProxyConfigUtils.getMiniProxiesList();
        ProxyData miniProxyData = new ProxyData();
        miniProxyData.setProxyName(proxyData.getProxyName());
        miniProxyData.setProxyPort(proxyData.getProxyPort());
        miniProxies.add(miniProxyData);
        Files.write(Paths.get(proxiesConfigFilePath), gson.toJson(miniProxies).getBytes());
    }

    public static void deleteMiniProxyConfig(String proxyName) throws IOException {
        String configDirPath = "config";
        Path configDir = Paths.get(configDirPath);
        if (!Files.exists(configDir)) {
            Files.createDirectory(configDir);
        }
        String proxiesConfigFilePath = configDirPath + "/proxies.conf";
        List<ProxyData> miniProxies = ProxyConfigUtils.getMiniProxiesList();
        List<ProxyData> updatedMiniProxies = new ArrayList<>();
        for (ProxyData miniProxy : miniProxies) {
            if (!miniProxy.getProxyName().equals(proxyName)) {
                updatedMiniProxies.add(miniProxy);
            }
        }
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        Files.write(Paths.get(proxiesConfigFilePath), gson.toJson(updatedMiniProxies).getBytes());
    }

    public static void updateProxyConfig(ProxyData proxyData) throws IOException {
        String configDirPath = "config";
        Path configDir = Paths.get(configDirPath);
        if (!Files.exists(configDir)) {
            Files.createDirectory(configDir);
        }
        String proxyConfigFilePath = configDirPath + "/" + proxyData.getProxyName() + ".conf";
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        Files.write(Paths.get(proxyConfigFilePath), gson.toJson(proxyData).getBytes());
    }

    public static void deleteProxyConfig(String proxyName) throws IOException {
        String configDirPath = "config";
        String proxyConfigFilePath = configDirPath + "/" + proxyName + ".conf";
        Path proxyConfigPath = Paths.get(proxyConfigFilePath);
        if (Files.exists(proxyConfigPath)) {
            Files.delete(proxyConfigPath);
        } else {
            System.out.println("A config file does not exist: " + proxyConfigFilePath);
        }
    }
}
