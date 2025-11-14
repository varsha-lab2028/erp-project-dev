package edu.univ.backend.databse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/university_erp";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "your_password";
    private static Connection connection = null;

    private DatabaseConnection() {
        // private constructor to prevent instantiation
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("✅ Database connected successfully!");
            } catch (SQLException e) {
                System.err.println("❌ Failed to connect to the database!");
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("🔌 Database connection closed.");
            } catch (SQLException e) {
                System.err.println("❌ Error closing connection!");
                e.printStackTrace();
            }
        }
    }
}
