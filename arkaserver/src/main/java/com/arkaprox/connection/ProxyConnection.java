package com.arkaprox.connection;

import com.arkacommon.model.ProxyData;
import com.arkacommon.utils.ProxyConfigUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ProxyConnection {

    private static ProxyConnection instance;

    public static synchronized ProxyConnection getInstance() {
        if (instance == null) {
            instance = new ProxyConnection();
        }
        return instance;
    }

    public Boolean testProxyConnection(String proxyName) {
        ProxyData proxyData = ProxyConfigUtils.getProxyByName(proxyName);

        String url = proxyData.getDbUrl() + "/" + proxyData.getDbName();
        String user = proxyData.getDbUsername();
        String password = proxyData.getDbPassword();

        try {
            Class.forName("org.postgresql.Driver");

            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
