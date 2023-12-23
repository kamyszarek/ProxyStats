package com.arkaprox;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchemaDetails {

    private static SchemaDetails instance;

    private String schemaName;

    private String readSchemaSchema = "SELECT table_name, column_name FROM information_schema.columns WHERE table_schema =";

    private final List<String> tablesInSchema = new ArrayList<>();

    private final Map<String, List<String>> columnsInTablesMap = new HashMap<>();

    private final ProxyDetails proxyDetails = ProxyDetails.getProxyDetails("aaa");

    private SchemaDetails() {

    }

    public static synchronized SchemaDetails getInstance() {
        if (instance == null) {
            instance = new SchemaDetails();
        }
        return instance;
    }

    public void createColumnsInTablesMap() {
        columnsInTablesMap.clear();
        tablesInSchema.clear();
        String url = proxyDetails.getUrl();
        String username = proxyDetails.getUsername();
        String password = proxyDetails.getPassword();
        String query = readSchemaSchema + "'" + proxyDetails.getSchemaName() + "'";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        String tableName = rs.getString("table_name");
                        String columnName = rs.getString("column_name");
                        if (columnsInTablesMap.containsKey(tableName)) {
                            columnsInTablesMap.get(tableName).add(columnName);
                        } else {
                            tablesInSchema.add(tableName);
                            List<String> columnsInTable = new ArrayList<>();
                            columnsInTable.add(columnName);
                            columnsInTablesMap.put(tableName, columnsInTable);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Query: \"" + query + "\" could not be executed: " + ex.getMessage());
        }
        int x = 2;
    }

    public void selectColumnsFromTable(String tableName) {
        String url = proxyDetails.getUrl();
        String username = proxyDetails.getUsername();
        String password = proxyDetails.getPassword();
        String query = "SELECT column_name FROM information_schema.columns WHERE table_schema = '" + proxyDetails.getSchemaName() +
                "' and table_name = '" + tableName + "'";
        List<String> columns = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        String columnName = rs.getString("column_name");
                        columns.add(columnName);
                    }
                    columnsInTablesMap.put(tableName, columns);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Query: \"" + query + "\" could not be executed: " + ex.getMessage());
        }
    }



    public List<String> getTablesInSchema() {
        return tablesInSchema;
    }

    public Map<String, List<String>> getColumnsInTablesMap() {
        return columnsInTablesMap;
    }
}

