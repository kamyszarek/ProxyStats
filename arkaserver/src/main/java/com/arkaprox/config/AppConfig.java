package com.arkaprox.config;

import com.arkaprox.service.ProxyService;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Configuration
public class AppConfig {

    private final ProxyService proxyService;

    public AppConfig(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @PreDestroy
    public void onShutdown() throws IOException {
        List<String> activeProxies = proxyService.getActiveProxiesNamesList();
        for (String proxy :activeProxies) {
            proxyService.stopProxy(proxy);
        }
        System.out.println("Application is closing. Performing cleanup and closing active proxies...");
    }
}