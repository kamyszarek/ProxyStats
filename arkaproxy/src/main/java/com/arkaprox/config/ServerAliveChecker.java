package com.arkaprox.config;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ServerAliveChecker {

    private final String serverAliveEndpoint = "http://localhost:8888/is-server-alive";
    private final RestTemplate restTemplate = new RestTemplate();

    public ServerAliveChecker() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::checkServerAlive, 0, 5, TimeUnit.SECONDS);
    }

    private void checkServerAlive() {
        try {
            String response = restTemplate.getForObject(serverAliveEndpoint, String.class);

            if (response != null && response.equals("Server is alive")) {
                System.out.println("Server is alive.");
            } else {
                System.out.println("Server is down! Closing proxy...");
                System.exit(1);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Server is down! Closing proxy...");
            System.exit(1);
        }
    }
}
