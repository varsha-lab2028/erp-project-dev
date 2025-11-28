package edu.univ.erp.auth;

import edu.univ.erp.domain.AuthClass;
import edu.univ.erp.domain.User;
import edu.univ.erp.util.DatabaseConnection; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthDAO {

    // 1. Login Method
    public User login(String username, String password) throws SQLException {
        // Updated table name to 'user_auth'
        String sql = "SELECT * FROM user_auth WHERE username = ? AND password_hash = ? AND status = 'ACTIVE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password); 
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getLong("user_id"), 
                        rs.getString("username"), 
                        rs.getString("username") + "@univ.edu", 
                        rs.getString("role")
                    );
                }
            }
        }
        return null; // Login failed
    }

    // 2. List All Users (For Admin Dashboard)
    public List<AuthClass> listUsers() throws SQLException {
        List<AuthClass> list = new ArrayList<>();
        // FIX: Changed 'users' to 'user_auth'
        String sql = "SELECT user_id, username, role, status FROM user_auth";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                AuthClass u = new AuthClass();
                u.user_id = rs.getLong("user_id");
                u.username = rs.getString("username");
                u.role = rs.getString("role");
                u.auth_status = rs.getString("status");
                list.add(u);
            }
        }
        return list;
    }

    // 3. Insert New User
    public void insertUser(String username, String role, String password) throws SQLException {
        // FIX: Changed 'users' to 'user_auth'
        String sql = "INSERT INTO user_auth (username, password_hash, role, status) VALUES (?, ?, ?, 'ACTIVE')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password); 
            stmt.setString(3, role);
            
            stmt.executeUpdate();
        }
    }

    // 4. Update User Status
    public void updateStatus(long userId, String status) throws SQLException {
        // FIX: Changed 'users' to 'user_auth'
        String sql = "UPDATE user_auth SET status = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setLong(2, userId);
            stmt.executeUpdate();
        }
    }
}