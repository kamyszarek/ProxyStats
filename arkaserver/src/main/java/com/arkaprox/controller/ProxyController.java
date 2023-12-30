package com.arkaprox.controller;

import com.arkacommon.model.ProxyData;
import com.arkaprox.service.ProxyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/proxy")
public class ProxyController {

    private final ProxyService proxyService;

    public ProxyController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @GetMapping("/queries-count")
    public Map<String, Map<String, Long>> getQueriesNumberForAllProxies() throws FileNotFoundException {
        return proxyService.getQueriesNumberForAllProxies();
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createProxyConfig(@RequestBody ProxyData proxyData) {
        try {
            proxyService.updateProxyConfig(proxyData);
            return new ResponseEntity<>("Proxy configuration created successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error creating proxy configuration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/start")
    public void startProxy(@RequestParam String proxyName)
            throws Exception {
        proxyService.runProxy(proxyName);
    }

    @GetMapping("/stop")
    public void stopProxy(@RequestParam String proxyName) throws IOException {
        proxyService.stopProxy(proxyName);
    }


    @GetMapping(value = "/read/{proxyName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProxyData> readProxyConfig(@PathVariable String proxyName) {
        return new ResponseEntity<>(proxyService.getProxyByName(proxyName), HttpStatus.OK);
    }

    @GetMapping(value = "/proxies-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProxyData>> getMiniProxiesList() throws FileNotFoundException {
        return new ResponseEntity<>(proxyService.getMiniProxiesList(), HttpStatus.OK);
    }

    @GetMapping(value = "/active-proxies-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getActiveProxiesNamesList() throws FileNotFoundException {
        return new ResponseEntity<>(proxyService.getActiveProxiesNamesList(), HttpStatus.OK);
    }

    @GetMapping("/random-number")
    public int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(10) + 1; // Losowa liczba od 1 do 10
    }
}