package com.arkaprox.controller;

import com.arkaprox.proxy.ProxyData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/proxy")
public class ProxyController {

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
}