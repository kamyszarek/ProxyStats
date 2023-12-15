package com.arkaprox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ProxyApplication {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: java -jar proxy.jar <proxyPort> <realDatabasePort>");
            System.exit(1);
        }
        int proxyApp = 1111;
        int proxyPort = Integer.parseInt(args[0]);
        int realDatabasePort = Integer.parseInt(args[1]);

        while (!isPortAvailable(proxyApp)) {
            proxyApp++;
        }

        System.out.println("Starting proxy application on port: " + proxyApp);

        SpringApplication.run(ProxyApplication.class, "--server.port=" + proxyApp);
        DatabaseProxy.main(new String[]{String.valueOf(proxyPort), String.valueOf(realDatabasePort)});
    }

    private static boolean isPortAvailable(int port) {
        try {
            new java.net.ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
