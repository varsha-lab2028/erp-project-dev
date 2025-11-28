package edu.univ.erp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // UPDATED: Pointing to 'auth_db'
    private static final String URL = "jdbc:mysql://192.168.41.50:3306/auth_db";
    private static final String USER = "Disha";
    private static final String PASSWORD = "2003"; // <--- TYPE PASSWORD

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}