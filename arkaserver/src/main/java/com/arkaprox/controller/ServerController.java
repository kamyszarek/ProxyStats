package com.arkaprox.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
class ServerController {

    private final Map<Integer, Process> proxyProcesses = new HashMap<>();

    @GetMapping("/start")
    public void startProxy(@RequestParam Integer proxyPort) throws IOException, InterruptedException {
        int realDatabasePort = 5432;
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "arkaproxy/build/libs/proxy.jar", String.valueOf(proxyPort), String.valueOf(realDatabasePort));
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

}