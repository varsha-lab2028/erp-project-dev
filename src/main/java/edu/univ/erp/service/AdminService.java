package edu.univ.erp.service;

import edu.univ.erp.data.*;
import edu.univ.erp.domain.*;
import edu.univ.erp.auth.AuthDAO;
import edu.univ.erp.access.AccessControl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdminService {
    private final SectionDAO sectionDAO = new SectionDAO();
    private final AuthDAO auth_dao = new AuthDAO();
    private final CourseDAO course_dao = new CourseDAO();
    private final MaintenanceService maintenanceService = new MaintenanceService();

    //dashboard statistics
    public Map<String, String> getDashboardStats() {
        AccessControl.checkRole("ADMIN"); 
        Map<String, String> stats = new HashMap<>();

        try {
            List<AuthClass> allUsers = auth_dao.listUsers();
            
            long studentCount = allUsers.stream().filter(u -> "STUDENT".equalsIgnoreCase(u.role)).count();
            long instructorCount = allUsers.stream().filter(u -> "INSTRUCTOR".equalsIgnoreCase(u.role)).count();
            int courseCount = course_dao.listCourses().size();

            stats.put("students", String.valueOf(studentCount));
            stats.put("instructors", String.valueOf(instructorCount));
            stats.put("courses", String.valueOf(courseCount));
            stats.put("status", maintenanceService.isMaintenanceOn() ? "Maintenance" : "Good");

        } catch (Exception e) {
            e.printStackTrace();
            stats.put("students", "0");
            stats.put("instructors", "0");
            stats.put("courses", "0");
            stats.put("status", "Error");
        }
        return stats;
    }

    public Object[][] getRecentActivity() {
        AccessControl.checkRole("ADMIN");
        return new Object[][]{
            {"System Login", "Admin", "Just Now"},
            {"Dashboard Loaded", "System", "1 min ago"},
            {"Database Check", "System", "5 mins ago"}
        };
    }

    public List<AuthClass> listAuthUsers() throws Exception {
        AccessControl.checkRole("ADMIN");
        return auth_dao.listUsers();
    }

    public void createAuthUser(String username, String rawPassword, String roleLabel) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();

        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username is required");
        if (rawPassword == null || rawPassword.isBlank()) throw new IllegalArgumentException("Password is required");
        
        try {
            Role.valueOf(roleLabel.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Role");
        }

        //create login
        auth_dao.insertUser(username.trim(), roleLabel.toUpperCase(), rawPassword);
        
        //auto sync
        syncUserToERP(username.trim(), roleLabel.toUpperCase());
    }

    private void syncUserToERP(String username, String role) {
        String sql = "";
        if ("INSTRUCTOR".equals(role)) {
            sql = "INSERT IGNORE INTO erp_db.instructors (instructor_id, name, email) " +
                  "SELECT user_id, username, CONCAT(username, '@univ.edu') FROM auth_db.user_auth WHERE username = ?";
        } else if ("STUDENT".equals(role)) {
            sql = "INSERT IGNORE INTO erp_db.students (user_id, name, email) " +
                  "SELECT user_id, username, CONCAT(username, '@univ.edu') FROM auth_db.user_auth WHERE username = ?";
        } else {
            return;
        }

        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }

    //course management
    public List<Course> getAllCourses() throws SQLException {
        AccessControl.checkRole("ADMIN");
        return course_dao.listCourses();
    }

    public void createCourse(String courseCode, String name, int credits) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();
        course_dao.insertCourse(courseCode, name, credits);
    }

    public void editCourse(String courseCode, String name, int credits) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();
        course_dao.updateCourse(courseCode, name, credits);
    }

    public void deleteCourse(String courseCode) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();
        course_dao.deleteCourse(courseCode);
    }

    //section management
    public List<Section> getAllSections() throws SQLException {
        AccessControl.checkRole("ADMIN");
        return sectionDAO.listAllSections();
    }

    public List<AuthClass> getAllInstructors() throws SQLException {
        AccessControl.checkRole("ADMIN");
        try {
            return auth_dao.listUsers().stream()
                    .filter(u -> "INSTRUCTOR".equalsIgnoreCase(u.role))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new SQLException("Failed to fetch instructors", e);
        }
    }

    public void createSectionFromUI(String courseCode, String instructorUsername, String room, String day, String time, int capacity) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();

        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be a positive number.");
        }

        //ensuring course exists
        if (course_dao.findByCourseCode(courseCode) == null) {
            throw new IllegalArgumentException("Course '" + courseCode + "' does not exist. Please create it first in the Courses tab.");
        }

        //finding the instructor
        List<AuthClass> instructors = getAllInstructors();
        Optional<AuthClass> instructorOpt = instructors.stream()
                .filter(u -> u.username.equals(instructorUsername))
                .findFirst();

        if (instructorOpt.isEmpty()) {
            throw new IllegalArgumentException("Selected instructor not found.");
        }
        
        AuthClass instructor = instructorOpt.get();
        
        //sync instructor
        syncUserToERP(instructor.username, "INSTRUCTOR");

        Section s = new Section(
            0L,                 
            courseCode,         
            instructor.user_id,       
            instructor.username,     
            day,                
            time,               
            room,               
            capacity,           
            1,                  
            "MONSOON",          
            2025                
        );

        // 5. Save
        sectionDAO.insertSection(s);
    }

    //maintenance
    public boolean isMaintenanceOn() {
        return maintenanceService.isMaintenanceOn();
    }

    public void setMaintenanceMode(boolean on) {
        AccessControl.checkRole("ADMIN");
        maintenanceService.toggleMaintenance(on);
    }
}