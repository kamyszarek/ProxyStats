package com.arkacommon.utils;

import com.arkacommon.model.ProxyData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ProxyConfigUtils {

    public static List<ProxyData> getMiniProxiesList() throws FileNotFoundException {
        String fileName = "config/proxies.conf";
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            Type listType = new TypeToken<List<ProxyData>>() {}.getType();
            List<ProxyData> proxiesList = new Gson().fromJson(br, listType);
            return proxiesList;
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("File: " + fileName + " not found: ");
        }
    }

    public static ProxyData getProxyByName(String proxyName) {
        try {
            String configDirPath = "config";
            String configFilePath = configDirPath + "/" + proxyName + ".conf";
            String configFileContent = new String(Files.readAllBytes(Paths.get(configFilePath)), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            ProxyData proxyData = gson.fromJson(configFileContent, ProxyData.class);
            return proxyData;
        } catch (IOException e) {
            throw new RuntimeException("An error occured during reading the proxy config.");
        }
    }
}
