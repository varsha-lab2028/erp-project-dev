package edu.univ.erp.auth;
import edu.univ.erp.data.ServerConnector;

import javax.sql.DataSource;
import java.sql.*;

public class SeedingUsers {
    public static void main(String[] args) throws Exception {
        AuthDAO dao = new AuthDAO();

        //creating 4 sample accounts
        try { dao.insertUser("admin1", "ADMIN", "admin@123"); } catch (Exception ignored) {}
        try { dao.insertUser("instructor1",  "INSTRUCTOR", "inst@123"); } catch (Exception ignored) {}
        try { dao.insertUser("student1",   "STUDENT", "stu1@123"); } catch (Exception ignored) {}
        try { dao.insertUser("student2",   "STUDENT", "stu2@123"); } catch (Exception ignored) {}

        //adding minimal ERP profiles matching user_id from auth_db by username lookup:
        linkErpProfiles("instructor1", "CSE");
        linkStudent("student1", "20240001", "B.Tech CSE", 2);
        linkStudent("student2", "20240002", "B.Tech CSE", 2);

        // Seed a course + section
        seedCourseAndSection();
        System.out.println("Seeding successful");
    }

    private static int userIdByUsername(String username) throws Exception {
        AuthDAO dao = new AuthDAO();
        var u = dao.findByUsername(username);
        if (u == null) throw new IllegalArgumentException("No such username: " + username);
        return u.user_id;
    }

    private static void linkErpProfiles(String instUsername, String department) throws Exception {
        int uid = userIdByUsername(instUsername);
        String sql = "INSERT IGNORE INTO erp_db.instructors(user_id, department) VALUES(?,?)";
        try (Connection c = ServerConnector.erp().getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, uid);
            ps.setString(2, department);
            ps.executeUpdate();
        }
    }

    private static void linkStudent(String stuUsername, String rollNo, String program, int year) throws Exception {
        int uid = userIdByUsername(stuUsername);
        String sql = "INSERT IGNORE INTO erp_db.students(user_id, roll_no, program, year) VALUES(?,?,?,?)";
        try (Connection c = ServerConnector.erp().getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, uid);
            ps.setString(2, rollNo);
            ps.setString(3, program);
            ps.setInt(4, year);
            ps.executeUpdate();
        }
    }

    private static void seedCourseAndSection() throws Exception {
        // Course
        int courseId;
        try (Connection c = ServerConnector.erp().getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO courses(code,title,credits) VALUES(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "AP101");
                ps.setString(2, "Applied Programming");
                ps.setInt(3, 4);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    courseId = rs.getInt(1);
                }
            } catch (SQLException e) {
                // If already exists, fetch id
                try (PreparedStatement ps2 = c.prepareStatement("SELECT course_id FROM courses WHERE code=?")) {
                    ps2.setString(1, "AP101");
                    try (ResultSet rs = ps2.executeQuery()) { rs.next(); courseId = rs.getInt(1); }
                }
            }
        }

        int instUid = userIdByUsername("inst1");

        try (Connection c = ServerConnector.erp().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO sections(course_id, instructor_id, day_time, room, capacity, semester, year) " +
                             "VALUES(?,?,?,?,?,?,?)")) {
            ps.setInt(1, courseId);
            ps.setInt(2, instUid);
            ps.setString(3, "Mon 10:00-11:30");
            ps.setString(4, "R-101");
            ps.setInt(5, 40);
            ps.setString(6, "Fall");
            ps.setInt(7, 2025);
            ps.executeUpdate();
        } catch (SQLException ignored) {}
    }
}
