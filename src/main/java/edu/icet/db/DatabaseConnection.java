package edu.icet.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private static final Object lock = new Object();
    
    private BlockingQueue<Connection> connectionPool;
    private String url;
    private String username;
    private String password;
    private String driver;
    private int initialPoolSize;
    private int maxPoolSize;
    private int connectionTimeout;
    
    private DatabaseConnection() {
        loadConfiguration();
        initializeConnectionPool();
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
    
    private void loadConfiguration() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find config.properties");
            }
            props.load(input);
            
            this.url = props.getProperty("db.url");
            this.username = props.getProperty("db.username");
            this.password = props.getProperty("db.password");
            this.driver = props.getProperty("db.driver");
            this.initialPoolSize = Integer.parseInt(props.getProperty("db.pool.initialSize", "5"));
            this.maxPoolSize = Integer.parseInt(props.getProperty("db.pool.maxSize", "20"));
            this.connectionTimeout = Integer.parseInt(props.getProperty("db.pool.timeout", "30000"));
            
            Class.forName(driver);
            
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }
    
    private void initializeConnectionPool() {
        connectionPool = new ArrayBlockingQueue<>(maxPoolSize);
        try {
            for (int i = 0; i < initialPoolSize; i++) {
                connectionPool.add(createNewConnection());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize connection pool", e);
        }
    }
    
    private Connection createNewConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
    
    public Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.poll(connectionTimeout, TimeUnit.MILLISECONDS);
            
            if (connection == null) {
                throw new SQLException("Connection timeout - no available connections in pool");
            }
            
            if (connection.isClosed() || !connection.isValid(2)) {
                connection = createNewConnection();
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Interrupted while waiting for connection", e);
        }
        
        return connection;
    }
    
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    if (connectionPool.size() < maxPoolSize) {
                        connectionPool.offer(connection);
                    } else {
                        connection.close();
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error while closing connection: " + e.getMessage());
            }
        }
    }
    
    public boolean testConnection() {
        Connection connection = null;
        try {
            connection = getConnection();
            return connection != null && connection.isValid(2);
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        } finally {
            closeConnection(connection);
        }
    }
    
    public void shutdown() {
        if (connectionPool != null) {
            for (Connection connection : connectionPool) {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    System.err.println("Error closing connection during shutdown: " + e.getMessage());
                }
            }
            connectionPool.clear();
        }
    }
    
    public int getAvailableConnections() {
        return connectionPool.size();
    }
    
    public int getMaxPoolSize() {
        return maxPoolSize;
    }
}
