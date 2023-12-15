package com.arkaprox.controller;

import com.arkaprox.proxy.ProxyData;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.IOException;

@RestController
@RequestMapping("/api/proxy")
public class ProxyController {

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createProxyConfig(@RequestBody ProxyData proxyData) {
        try {
            String configDirPath = "config";

            java.nio.file.Path configDir = java.nio.file.Paths.get(configDirPath);
            if (!java.nio.file.Files.exists(configDir)) {
                java.nio.file.Files.createDirectory(configDir);
            }

            String configFilePath = configDirPath + "/" + proxyData.getProxyName() + ".conf";

            try (FileWriter fileWriter = new FileWriter(configFilePath)) {
                fileWriter.write("Proxy Name: " + proxyData.getProxyName() + "\n");
                fileWriter.write("Proxy Port: " + proxyData.getProxyPort() + "\n");
                fileWriter.write("DB URL: " + proxyData.getDbUrl() + "\n");
                fileWriter.write("DB Port: " + proxyData.getDbPort() + "\n");
                fileWriter.write("DB Schema: " + proxyData.getDbSchema() + "\n");
                fileWriter.write("DB Username: " + proxyData.getDbUsername() + "\n");
                fileWriter.write("DB Password: " + proxyData.getDbPassword() + "\n");
            }

            return new ResponseEntity<>("Proxy configuration created successfully", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error creating proxy configuration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}