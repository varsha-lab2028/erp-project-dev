package edu.univ.erp.data;

import edu.univ.erp.domain.Course;
import java.util.*;
import java.sql.*;

// Data access object for the 'courses' table in the ERP database
public class CourseDAO {

    // --- READ METHODS ---

    public List<Course> listCourses() throws SQLException {
        // NOTE: If your database uses 'course_code' as the primary key and doesn't have 'course_id',
        // you may need to remove 'course_id' from this query and the constructor below.
        String command = "SELECT course_code, name, credits FROM courses ORDER BY course_code";
        
        List<Course> courses = new ArrayList<>();
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                courses.add(new Course(
                        0L, // Default ID since we use course_code as key
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
            WHERE course_code LIKE ? OR name LIKE ?
            ORDER BY course_code
        """;
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
        String sql = "SELECT course_code, name, credits FROM courses WHERE course_code = ?";
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Course(
                            0L,
                            rs.getString("course_code"),
                            rs.getString("name"),
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

    // --- ADMIN WRITE METHODS ---

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

    // --- FIX: ROBUST DELETE METHOD ---
    public void deleteCourse(String courseCode) throws SQLException {
        // We must delete the child records first to satisfy Foreign Key constraints
        String deleteGrades = "DELETE FROM grade_components WHERE section_id IN (SELECT section_id FROM sections WHERE course_code = ?)";
        // Also clean up final grades if that table exists
        String deleteFinalGrades = "DELETE FROM final_grades WHERE section_id IN (SELECT section_id FROM sections WHERE course_code = ?)";
        String deleteEnrollments = "DELETE FROM enrollments WHERE section_id IN (SELECT section_id FROM sections WHERE course_code = ?)";
        String deleteSections = "DELETE FROM sections WHERE course_code = ?";
        String deleteCourse = "DELETE FROM courses WHERE course_code = ?";

        try (Connection conn = ServerConnector.ERPConnection()) {
            // Start Transaction to ensure all or nothing deletes
            conn.setAutoCommit(false); 

            try {
                // 1. Delete Grade Components (Quizzes/Exams linked to sections)
                try (PreparedStatement ps = conn.prepareStatement(deleteGrades)) {
                    ps.setString(1, courseCode);
                    ps.executeUpdate();
                }
                
                // 1b. Delete Final Grades (if table exists, harmless if empty)
                try (PreparedStatement ps = conn.prepareStatement(deleteFinalGrades)) {
                    ps.setString(1, courseCode);
                    ps.executeUpdate();
                }

                // 2. Delete Student Enrollments linked to sections
                try (PreparedStatement ps = conn.prepareStatement(deleteEnrollments)) {
                    ps.setString(1, courseCode);
                    ps.executeUpdate();
                }

                // 3. Delete Sections linked to the course
                try (PreparedStatement ps = conn.prepareStatement(deleteSections)) {
                    ps.setString(1, courseCode);
                    ps.executeUpdate();
                }

                // 4. Finally, Delete the Course itself
                try (PreparedStatement ps = conn.prepareStatement(deleteCourse)) {
                    ps.setString(1, courseCode);
                    ps.executeUpdate();
                }

                conn.commit(); // Commit transaction
            } catch (SQLException e) {
                conn.rollback(); // Undo changes if any step fails
                throw e; // Re-throw error to be caught by UI
            } finally {
                conn.setAutoCommit(true); // Reset connection state
            }
        }
    }
}