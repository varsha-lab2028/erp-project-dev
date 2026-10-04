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
    public User login(String username, String password) throws SQLException {
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
        return null;
    }

    //listing all users for the admin dashboard
    public List<AuthClass> listUsers() throws SQLException {
        List<AuthClass> list = new ArrayList<>();
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

    public void insertUser(String username, String role, String password) throws SQLException {
        String sql = "INSERT INTO user_auth (username, password_hash, role, status) VALUES (?, ?, ?, 'ACTIVE')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password); 
            stmt.setString(3, role);
            
            stmt.executeUpdate();
        }
    }

    public void updateStatus(long userId, String status) throws SQLException {
        String sql = "UPDATE user_auth SET status = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setLong(2, userId);
            stmt.executeUpdate();
        }
    }

    // Returns the stored login row for one username, or null if there is none
    public AuthClass findByUsername(String username) throws SQLException {
        String sql = "SELECT user_id, username, role, password_hash, status FROM user_auth WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                AuthClass u = new AuthClass();
                u.user_id = rs.getLong("user_id");
                u.username = rs.getString("username");
                u.role = rs.getString("role");
                u.password_hash = rs.getString("password_hash");
                u.auth_status = rs.getString("status");
                return u;
            }
        }
    }

    // Returns true if exactly one row was changed
    public boolean updatePassword(long userId, String newHash) throws SQLException {
        String sql = "UPDATE user_auth SET password_hash = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newHash);
            stmt.setLong(2, userId);
            return stmt.executeUpdate() == 1;
        }
    }

}