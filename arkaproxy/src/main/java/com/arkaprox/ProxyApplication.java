package com.arkaprox;

import com.arkacommon.file.FilesWriter;
import com.arkacommon.model.ProxyData;
import com.arkacommon.utils.ProxyConfigUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ProxyApplication {

    private static String proxyName;

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: java -jar proxy.jar <proxyPort> <realDatabasePort>");
            System.exit(1);
        }
        int proxyAppPort = 1111;
        proxyName = args[0];
        int proxyPort = Integer.parseInt(args[1]);
        int realDatabasePort = Integer.parseInt(args[2]);

        while (!isPortAvailable(proxyAppPort)) {
            proxyAppPort++;
        }

        System.out.println("Starting proxy application on port: " + proxyAppPort);
        SpringApplication.run(ProxyApplication.class, "--server.port=" + proxyAppPort);
        ProxyData proxyData = ProxyConfigUtils.getProxyByName(proxyName);
        proxyData.setAppPort(proxyAppPort);
        FilesWriter.updateProxyConfig(proxyData);
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
