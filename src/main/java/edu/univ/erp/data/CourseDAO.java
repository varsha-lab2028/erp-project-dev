package edu.univ.erp.data;

import edu.univ.erp.domain.Course;
import java.util.*;
import java.sql.*;

// Data access object for the 'courses' table in the ERP database
public class CourseDAO {
    public List<Course> listCourses() throws SQLException {
        String command = "SELECT course_code, name, credits FROM courses ORDER BY course_code";
        
        List<Course> courses = new ArrayList<>();
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                courses.add(new Course(
                        0L, //this is the default key
                        rs.getString("name"),
                        rs.getString("course_code"),
                        rs.getInt("credits")
                ));
            }
        }
        return courses;
    }

    public List<Course> searchCourse(String keyword) throws SQLException {
        String like = "%" + keyword + "%";
        String sql = """
            SELECT course_code, name, credits
            FROM courses
            WHERE course_code ILIKE ? OR name ILIKE ?
            ORDER BY course_code
        """; //ILIKE is for PostgreSQL, not mysql
        List<Course> courses = new ArrayList<>();
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    courses.add(new Course(
                            0L,
                            rs.getString("name"),
                            rs.getString("course_code"),
                            rs.getInt("credits")
                    ));
                }
            }
        }
        return courses;
    }

    public Course findByCourseCode(String code) throws SQLException {
        String sql = "SELECT name, course_code, credits FROM courses WHERE course_code = ?";
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Course(
                            0L,
                            rs.getString("name"),
                            rs.getString("course_code"),
                            rs.getInt("credits")
                    );
                }
                return null;
            }
        }
    }

    public String findNameByCourseCode(String courseCode) throws SQLException {
        String sql = "SELECT name FROM courses WHERE course_code = ?";
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        }
        return courseCode;
    }

    //admin write methods
    public void insertCourse(String courseCode, String name, int credits) throws SQLException {
        final String sql = "INSERT INTO courses (course_code, name, credits) VALUES (?, ?, ?)";

        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, courseCode);
            ps.setString(2, name);
            ps.setInt(3, credits);
            ps.executeUpdate();
        }
    }

    public void updateCourse(String courseCode, String name, int credits) throws SQLException {
        final String sql = "UPDATE courses SET name = ?, credits = ? WHERE course_code = ?";

        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, credits);
            ps.setString(3, courseCode);
            ps.executeUpdate();
        }
    }

    // Refuses if any section of the course has students. Otherwise, deletes its sections, then the course.
    public void deleteCourse(String courseCode) throws SQLException {
        String check = """
            SELECT 1 FROM enrollments e
            JOIN sections s ON s.section_id = e.section_id
            WHERE s.course_code = ?
            LIMIT 1
            """;
        String deleteSections = "DELETE FROM sections WHERE course_code = ?";
        String deleteCourse = "DELETE FROM courses WHERE course_code = ?";

        try (Connection conn = ServerConnector.ERPConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(check)) {
                ps.setString(1, courseCode);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new IllegalStateException("Course has sections with enrolled students.");
                    }
                }
            }

            conn.setAutoCommit(false);   // both deletes happen, or neither does
            try {
                try (PreparedStatement ps = conn.prepareStatement(deleteSections)) {
                    ps.setString(1, courseCode);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(deleteCourse)) {
                    ps.setString(1, courseCode);
                    ps.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}