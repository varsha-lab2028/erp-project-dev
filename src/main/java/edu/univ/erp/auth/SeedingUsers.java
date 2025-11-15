package edu.univ.erp.auth;

import edu.univ.erp.data.ServerConnector;
import edu.univ.erp.domain.OnlineStatus;
import java.sql.*;

public class SeedingUsers {
    public static void main(String[] args) throws Exception {
        AuthDAO auth_dao = new AuthDAO();

        //seeding 4 users in user_auth table
        try {
            auth_dao.insertUser("admin1", "ADMIN", "admin@123");
        } catch (Exception ignored) {}
        try {
            auth_dao.insertUser("instructor1", "INSTRUCTOR", "inst@123");
        } catch (Exception ignored) {}
        try {
            auth_dao.insertUser("student1", "STUDENT", "stu1@123");
        } catch (Exception ignored) {}
        try {
            auth_dao.insertUser("student2", "STUDENT", "stu2@123");
        } catch (Exception ignored) {}

        //seeding in instructor and students in erp_db table
        seedInstructors("instructor1", "CSE");
        seedStudents("student1", "20250001", "B.Tech", "CSE", 1);
        seedStudents("student2", "20250002", "B.Tech", "CSE", 1);

        //to seed in courses into the courses table
        seedCourses();

        //to seed in sections into the sections table



        System.out.println("Information has been seeded successfully");
    }

    private static long findUserIdByUsername(String username) throws Exception {
        AuthDAO auth_dao = new AuthDAO();
        var u = auth_dao.findByUsername(username);
        if (u == null) throw new IllegalArgumentException("No such username: " + username);
        return u.user_id;
    }

    private static void seedInstructors(String instUsername, String department) throws Exception {
        long uid = findUserIdByUsername(instUsername);
        String sql = "INSERT IGNORE INTO erp_db.instructors(user_id, department) VALUES(?,?)";
        try (Connection c = ServerConnector.erp().getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, uid);
            ps.setString(2, department);
            ps.executeUpdate();
        }
    }

    private static void seedStudents(String student_username, String roll_no, String degree, String branch, int term_year) throws Exception {
        long uid = findUserIdByUsername(student_username);
        String sql = "INSERT IGNORE INTO erp_db.students(user_id, roll_no, degree, branch, term_year, status) VALUES(?,?,?,?, ?, ?)";
        try (Connection c = ServerConnector.erp().getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, uid);
            ps.setString(2, roll_no);
            ps.setString(3, degree);
            ps.setString(4, branch);
            ps.setInt(5, term_year);
            ps.setString(6, "INACTIVE");
            ps.executeUpdate();
        }
    }

    private static void seedCourses() throws Exception {
        String command = "INSERT IGNORE INTO courses(name, course_code, credits) VALUES(?, ?, ?)";
        try (Connection c = ServerConnector.erp().getConnection();
             PreparedStatement ps = c.prepareStatement(command)) {

            //seeding in the courses of semester 1
            ps.setString(1, "Introduction to Programming");
            ps.setString(2, "CSE101");
            ps.setInt(3, 4);
            ps.executeUpdate();

            ps.setString(1, "Linear Algebra");
            ps.setString(2, "MTH100");
            ps.setInt(3, 4);
            ps.executeUpdate();

            ps.setString(1, "Human Computer Interaction");
            ps.setString(2, "DES204");
            ps.setInt(3, 4);
            ps.executeUpdate();

            ps.setString(1, "Digital Circuits");
            ps.setString(2, "ECE111");
            ps.setInt(3, 4);
            ps.executeUpdate();

            ps.setString(1, "Communication Skills");
            ps.setString(2, "COM101");
            ps.setInt(3, 4);
            ps.executeUpdate();
        }
    }

    private static void seedSections() throws Exception{
        String command = "INSERT IGNORE INTO sections " +
                "(course_id, instructor_id, day_time, room, capacity, sem_no, sem_season, year) " +
                "VALUES ((SELECT course_id FROM courses WHERE course_code=?), " +
                " (SELECT i.user_id FROM erp_db.instructors i " +
                "JOIN auth_db.users_auth u ON u.user_id=i.user_id AND u.role='INSTRUCTOR' " +
                "WHERE u.username=?), ?, ?, ?, ?, ?, ? )";
        try (Connection c = ServerConnector.erp().getConnection();
             PreparedStatement ps = c.prepareStatement(command)) {
            //for IP course, only a single section
            ps.setString(1, "CSE101");                 // course_code
            ps.setString(2, "prof_prog");              // TODO: replace with real instructor username
            ps.setString(3, "Mon 09:00–10:30");        // day_time
            ps.setString(4, "LHC-101");                // room
            ps.setInt(5, 60);                          // capacity
            ps.setInt(6, 1);                           // sem_no (1)
            ps.setString(7, "Fall");                   // sem_season
            ps.setInt(8, 2025);                        // year
            ps.executeUpdate();
        }
    }
}
