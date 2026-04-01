package edu.icet.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;
    
    // Database configuration - consider moving to external properties file
    private static final String URL = "jdbc:mysql://localhost:3306/pharmacy_ims";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static DBConnection getInstance() throws SQLException {
        return instance == null ? instance = new DBConnection() : instance;
    }

    public Connection getConnection() throws SQLException {
        // Validate and reconnect if connection is closed
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
