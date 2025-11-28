package edu.univ.erp.data;

import edu.univ.erp.domain.User;
import edu.univ.erp.domain.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDAO {
    public List<User> listAllUsers() throws SQLException {
        String sql = "SELECT user_id, username, role, status FROM users ORDER BY username";
        List<User> users = new ArrayList<>();
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getLong("user_id"),
                        rs.getString("username"),
                        Role.valueOf(rs.getString("role").toUpperCase()),
                        rs.getString("status")
                ));
            }
        }
        return users;
    }

    public User findByUserId(long userId) throws SQLException {
        String sql = "SELECT user_id, username, role, status FROM users WHERE user_id = ?";
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getLong("user_id"),
                            rs.getString("username"),
                            Role.valueOf(rs.getString("role").toUpperCase()),
                            rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

}
