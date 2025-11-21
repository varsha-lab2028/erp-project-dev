package edu.univ.erp.auth;

import edu.univ.erp.data.ServerConnector;

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

        //seeding IP profs in auth table
        try {auth_dao.insertUser("md_shah",  "INSTRUCTOR", "mdshah@123"); } catch (Exception ignored) {}
        try { auth_dao.insertUser("pankaj_jalote","INSTRUCTOR", "pankaj@123"); } catch (Exception ignored) {}
        //seeding HCI profs in auth table
        try {auth_dao.insertUser("sonal_keshwani","INSTRUCTOR", "sonal@123"); }catch (Exception ignored) {}
        try {auth_dao.insertUser("pragma_kar","INSTRUCTOR", "pragma@123"); }catch (Exception ignored) {}
        //seeding LA profs in auth table
        try {auth_dao.insertUser("subhajit","INSTRUCTOR", "subhajit@123"); }catch (Exception ignored) {}
        try {auth_dao.insertUser("prahlad_deb", "INSTRUCTOR", "prahlad@123"); }catch (Exception ignored) {}
        //seeding COM prof in auth table
        try {auth_dao.insertUser("payal", "INSTRUCTOR", "payalc@123"); }catch (Exception ignored) {}
        //seeding DC prof in auth table
        try {auth_dao.insertUser("pravesh_biyani", "INSTRUCTOR", "pravesh@123"); }catch (Exception ignored) {}
        try {auth_dao.insertUser("tammam_tillo", "INSTRUCTOR", "tammam@123"); }catch (Exception ignored) {}


        //seeding in instructor in erp table
        seedInstructors("instructor1", "Mr. Instructor 1","CSE");
        seedInstructors("md_shah", "Md. Shah Akhtar", "CSE");
        seedInstructors("pankaj_jalote", "Pankaj Jalote", "CSE");
        seedInstructors("sonal_keshwani", "Sonal Keshwani", "DESIGN");
        seedInstructors("pragma_kar", "Pragma Kar", "DESIGN");
        seedInstructors("subhajit", "Subhajit Ghosechowdhury", "MATHEMATICS");
        seedInstructors("prahlad_deb", "Prahlad Deb", "MATHEMATICS");
        seedInstructors("payal", "Payal C Mukherjee", "COMMUNICATIONS");
        seedInstructors("pravesh_biyani", "Pravesh Biyani", "ECE");
        seedInstructors("tammam_tillo", "Tammam Tillo", "ECE");

        //seeding students in erp table
        seedStudents("student1", "20250001", "B.Tech", "CSE", 1);
        seedStudents("student2", "20250002", "B.Tech", "CSE", 1);

        //to seed in courses into the courses table
        seedCourses();

        //to seed in sections into the sections table
        seedSections();

        System.out.println("Everything seeded successfully");
    }

    private static long findUserIdByUsername(String username) throws Exception {
        AuthDAO auth_dao = new AuthDAO();
        var u = auth_dao.findByUsername(username);
        if (u == null) throw new IllegalArgumentException("No such username: " + username);
        return u.user_id;
    }

    private static void seedInstructors(String instructor_username, String instructor_name, String department) throws Exception {
        long uid = findUserIdByUsername(instructor_username);
        String command = "INSERT IGNORE INTO erp_db.instructors(user_id, instructor_name, department) VALUES(?,?,?)";
        try (Connection c = ServerConnector.erp().getConnection(); PreparedStatement ps = c.prepareStatement(command)) {
            ps.setLong(1, uid);
            ps.setString(2, instructor_name);
            ps.setString(3, department);
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
        //getting the prof ids by finding through username
        long ip_id1   = findUserIdByUsername("md_shah");
        long ip_id2  = findUserIdByUsername("pankaj_jalote");
        long hci_id1  = findUserIdByUsername("sonal_keshwani");
        long hci_id2 = findUserIdByUsername("pragma_kar");
        long la_id1  = findUserIdByUsername("subhajit");
        long la_id2 = findUserIdByUsername("prahlad_deb");
        long com_id1 = findUserIdByUsername("payal");
        long dc_id1 = findUserIdByUsername("pravesh_biyani");
        long dc_id2 = findUserIdByUsername("tammam_tillo");

        String command = "INSERT IGNORE INTO sections " +
                "(course_code, instructor_id, instructor_name, day, timings, classroom, capacity, sem_no, sem_season, year) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = ServerConnector.erp().getConnection();
             PreparedStatement ps = c.prepareStatement(command)) {

            //IP Section A
            ps.setString(1, "CSE101");
            ps.setLong(2, ip_id1);
            ps.setString(3, "Md. Shah Akhtar");
            ps.setString(4, "MONDAY, WEDNESDAY");
            ps.setString(5, "9:00 am - 10:30 am");
            ps.setString(6, "LHC-C101");
            ps.setInt(7, 300);
            ps.setInt(8, 1);
            ps.setString(9, "MONSOON");
            ps.setInt(10, 2025);
            ps.executeUpdate();

            //IP section B
            ps.setString(1, "CSE101");
            ps.setLong(2, ip_id2);
            ps.setString(3, "Pankaj Jalote");
            ps.setString(4, "MONDAY, WEDNESDAY");
            ps.setString(5, "9:00 am - 10:30 am");
            ps.setString(6, "LHC-C201");
            ps.setInt(7, 300);
            ps.setInt(8, 1);
            ps.setString(9, "MONSOON");
            ps.setInt(10, 2025);
            ps.executeUpdate();

            //LA SECTION A
            ps.setString(1, "MTH100");
            ps.setLong(2, la_id1);
            ps.setString(3, "Subhajit Ghosechowdhury");
            ps.setString(4, "MONDAY, WEDNESDAY");
            ps.setString(5, "10:45 am - 12:15 pm");
            ps.setString(6, "LHC-C101");
            ps.setInt(7, 300);
            ps.setInt(8, 1);
            ps.setString(9, "MONSOON");
            ps.setInt(10, 2025);
            ps.executeUpdate();

            //LA SECTION B
            ps.setString(1, "MTH100");
            ps.setLong(2, la_id2);
            ps.setString(3, "Prahlad Deb");
            ps.setString(4, "MONDAY, WEDNESDAY");
            ps.setString(5, "10:45 am - 12:15 pm");
            ps.setString(6, "LHC-C201");
            ps.setInt(7, 300);
            ps.setInt(8, 1);
            ps.setString(9, "MONSOON");
            ps.setInt(10, 2025);
            ps.executeUpdate();

            //HCI SECTION A
            ps.setString(1, "DES204");
            ps.setLong(2, hci_id1);
            ps.setString(3, "Sonal Keshwani");
            ps.setString(4, "TUESDAY, THURSDAY");
            ps.setString(5, "9:00 am - 10:30 am");
            ps.setString(6, "LHC-C101");
            ps.setInt(7, 300);
            ps.setInt(8, 1);
            ps.setString(9, "MONSOON");
            ps.setInt(10, 2025);
            ps.executeUpdate();

            //HCI SECTION B
            ps.setString(1, "DES204");
            ps.setLong(2, hci_id2);
            ps.setString(3, "Pragma Kar");
            ps.setString(4, "TUESDAY, THURSDAY");
            ps.setString(5, "9:00 am - 10:30 am");
            ps.setString(6, "LHC-C201");
            ps.setInt(7, 300);
            ps.setInt(8, 1);
            ps.setString(9, "MONSOON");
            ps.setInt(10, 2025);
            ps.executeUpdate();

            //DC SECTION A
            ps.setString(1, "ECE111");
            ps.setLong(2, dc_id1);
            ps.setString(3, "Pravesh Biyani");
            ps.setString(4, "TUESDAY, THURSDAY");
            ps.setString(5, "10:45 am - 12:15 pm");
            ps.setString(6, "LHC-C101");
            ps.setInt(7, 300);
            ps.setInt(8, 1);
            ps.setString(9, "MONSOON");
            ps.setInt(10, 2025);
            ps.executeUpdate();

            //DC SECTION B
            ps.setString(1, "ECE111");
            ps.setLong(2, dc_id2);
            ps.setString(3, "Tammam Tillo");
            ps.setString(4, "TUESDAY, THURSDAY");
            ps.setString(5, "10:45 am - 12:15 pm");
            ps.setString(6, "LHC-C201");
            ps.setInt(7, 300);
            ps.setInt(8, 1);
            ps.setString(9, "MONSOON");
            ps.setInt(10, 2025);
            ps.executeUpdate();

            //COM, ONLY A SINGLE SECTION
            ps.setString(1, "COM101");
            ps.setLong(2, com_id1);
            ps.setString(3, "Payal C Mukherjee");
            ps.setString(4, "FRIDAY");
            ps.setString(5, "3:00 pm - 6:00 pm");
            ps.setString(6, "LHC-C102");
            ps.setInt(7, 600);
            ps.setInt(8, 1);
            ps.setString(9, "MONSOON");
            ps.setInt(10, 2025);
            ps.executeUpdate();
        }
    }
}
