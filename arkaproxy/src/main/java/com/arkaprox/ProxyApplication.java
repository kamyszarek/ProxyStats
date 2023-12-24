package com.arkaprox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

import java.io.IOException;

@SpringBootApplication
public class ProxyApplication {

    private static String proxyName;

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: java -jar proxy.jar <proxyPort> <realDatabasePort>");
            System.exit(1);
        }
        int proxyApp = 1111;
        proxyName = args[0];
        int proxyPort = Integer.parseInt(args[1]);
        int realDatabasePort = Integer.parseInt(args[2]);

        while (!isPortAvailable(proxyApp)) {
            proxyApp++;
        }

        System.out.println("Starting proxy application on port: " + proxyApp);


        SpringApplication application = new SpringApplication(ProxyApplication.class);

        // Konfiguracja ApplicationPidFileWriter do zapisu PID do pliku
        application.addListeners(new ApplicationPidFileWriter("proxy-application.pid"));


        SpringApplication.run(ProxyApplication.class, "--server.port=" + proxyApp);
        DatabaseProxy.main(new String[]{proxyName, String.valueOf(proxyPort), String.valueOf(realDatabasePort)});
    }

    public static String getProxyName() {
        return proxyName;
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
