package com.arkaprox.query;

import java.sql.*;

public class QueryExecute {

    public static ResultSet execute(String query) {
        String url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=test";
        String username = "postgres";
        String password = "password";
        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                resultSet = statement.executeQuery();
                return resultSet;
            }
        } catch (SQLException ex) {
            System.out.println("Query: \"" + query + "\" could not be executed." );
        }
        return resultSet;
    }
}
