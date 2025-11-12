package edu.univ.erp.auth;
import edu.univ.erp.data.ServerConnector;
import edu.univ.erp.domain.AuthClass;
import edu.univ.erp.domain.Role;
import edu.univ.erp.domain.User;

import javax.sql.DataSource;
import java.sql.*;

/*this class is for talking to the database (MYSQL) and allows changes*/
/*for handling authentication*/
/*for handling user_auth table (user_id, username, role, password_hash, status, last_login)*/

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

    //finds a user by their username
    public AuthClass findByUsername(String username) throws Exception {
        String command = "SELECT user_id, username, role, password_hash, status FROM user_auth WHERE username=?";
        try (Connection connection = data_source.getConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
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

    //updates the last login timestamp after logging in
    public void updateLastLogin(long user_id) throws Exception {
        String command = "UPDATE user_auth SET last_login = CURRENT_TIMESTAMP WHERE user_id=?";
        try (Connection connection = data_source.getConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setLong(1, user_id);
            ps.executeUpdate();
        }
    }

    // converts AuthClass (from DB) → User (used in session/UI)
    public User toUser(AuthClass a) {
        Role r = Role.valueOf(a.role.toUpperCase());  // converts "student" → Role.STUDENT
        return new User(a.user_id, a.username, r, a.auth_status);
    }
}
