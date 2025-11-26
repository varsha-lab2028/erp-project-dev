package edu.univ.erp.data;

import edu.univ.erp.domain.Course;
import java.util.*;
import java.sql.*;

//data access object for the 'courses' table in the ERP database
public class CourseDAO {
    public List<Course> listCourses() throws SQLException {
        String command = "SELECT course_id, name, course_code, credits FROM courses ORDER BY course_code";
        //creating courses list for storing
        List<Course> courses = new ArrayList<>();
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                courses.add(new Course(
                        rs.getLong("course_id"),
                        rs.getString("name"),
                        rs.getString("course_code"),
                        rs.getInt("credits")
                ));
            }
        }
        return courses;
    }

    //searching by course name or the course code
    public List<Course> searchCourse(String keyword) throws SQLException {
        String like = "%" + keyword + "%";
        String sql = """
            SELECT course_id, course_code, name, credits
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
                            rs.getLong("course_id"),
                            rs.getString("name"),
                            rs.getString("course_code"),
                            rs.getInt("credits")
                    ));
                }
            }
        }
        return courses;
    }

    //methods to find the course through their unique code
    public Course findByCourseCode(String code) throws SQLException {
        String sql = "SELECT course_id, course_code, name, credits FROM courses WHERE course_code = ?";
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Course(
                            rs.getLong("course_id"),
                            rs.getString("course_code"),
                            rs.getString("name"),
                            rs.getInt("credits")
                    );
                }
                return null; //no matching course found
            }
        }
    }

    //specifically made for instructor panel
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
}
