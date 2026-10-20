package edu.univ.erp.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Creates the erp-side profile row for a new login. The id is the same as authdb.user_auth.user_id.
public class ProfileDAO {
    public void insertStudent(long userId, String name, String email) throws SQLException {
        insert("INSERT INTO students (user_id, name, email) VALUES (?, ?, ?) ON CONFLICT DO NOTHING",
                userId, name, email);
    }

    public void insertInstructor(long instructorId, String name, String email) throws SQLException {
        insert("INSERT INTO instructors (instructor_id, name, email) VALUES (?, ?, ?) ON CONFLICT DO NOTHING",
                instructorId, name, email);
    }

    private void insert(String sql, long id, String name, String email) throws SQLException {
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.executeUpdate();
        }
    }
}
