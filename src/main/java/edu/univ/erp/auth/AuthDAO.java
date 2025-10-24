package edu.univ.erp.auth;
import edu.univ.erp.data.ServerConnector;
import edu.univ.erp.domain.AuthClass;

import javax.sql.DataSource;
import java.sql.*;

/*this class is for talking to the database (MYSQL) and allows changes*/

public class AuthDAO {
    private final DataSource data_source = ServerConnector.auth();

    public void insertUser(String username, String role, String raw_password) throws Exception {
        //command = the sql command
        String command = "INSERT INTO user_auth(username, role, password_hash, status) VALUES (?,?,?, 'INACTIVE')";
        try (Connection connection = data_source.getConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setString(1, username);
            ps.setString(2, role);
            ps.setString(3, PasswordHasher.hash(raw_password));
            ps.executeUpdate();
        }

    }

    public AuthClass findByUsername(String username) throws Exception {
        String sql = "SELECT user_id, username, role, password_hash, status FROM users_auth WHERE username=?";
        try (Connection connection = data_source.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                AuthClass u = new AuthClass();
                u.user_id = rs.getInt("user_id");
                u.username = rs.getString("username");
                u.role = rs.getString("role");
                u.password_hash = rs.getString("password_hash");
                u.auth_status = rs.getString("status");
                return u;
            }
        }
    }

    public void updateLastLogin(int user_id) throws Exception {
        String command = "UPDATE user_auth SET last_login = CURRENT_TIMESTAMP WHERE user_id=?";
        try (Connection connection = data_source.getConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setInt(1, user_id);
            ps.executeUpdate();
        }
    }
}
