package com.arkaprox;

import java.util.HashMap;
import java.util.Map;

public class ProxyDetails {

    private static final Map<String, ProxyDetails> proxyDetailsMap = new HashMap<>();

    private String proxyName;

    private String schemaName = "test";

    private Integer dbPort = 5432;

    private String password = "password";

    private String username = "postgres";

    private String url = "jdbc:postgresql://localhost:5432/postgres";

    private ProxyDetails(String proxyName) {
        this.proxyName = proxyName;
    }

    public static ProxyDetails getProxyDetails(String proxyName) {
        if (proxyDetailsMap.containsKey(proxyName)) {
            return proxyDetailsMap.get(proxyName);
        } else {
            ProxyDetails newProxyDetails = new ProxyDetails(proxyName);
            proxyDetailsMap.put(proxyName, newProxyDetails);
            return newProxyDetails;
        }
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getProxyName() {
        return proxyName;
    }

    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    public Integer getDbPort() {
        return dbPort;
    }

    public void setDbPort(Integer dbPort) {
        this.dbPort = dbPort;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
