package edu.univ.erp.service;

import edu.univ.erp.data.*;
import edu.univ.erp.domain.*;
import edu.univ.erp.auth.AuthDAO;
import edu.univ.erp.access.AccessControl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdminService {
    // DATA ACCESS OBJECTS (The "Backend")
    private final UserDAO userDAO = new UserDAO();
    private final SectionDAO sectionDAO = new SectionDAO();
    private final AuthDAO auth_dao = new AuthDAO();
    private final CourseDAO course_dao = new CourseDAO();
    private final MaintenanceService maintenanceService = new MaintenanceService();

    // ==================================================================================
    // 1. DASHBOARD STATISTICS (Middleman between DAO and AdminHomePanel)
    // ==================================================================================

    public Map<String, String> getDashboardStats() {
        AccessControl.checkRole("ADMIN"); 
        Map<String, String> stats = new HashMap<>();

        try {
            // 1. Get raw list from AuthDAO
            List<AuthClass> allUsers = auth_dao.listUsers();
            
            // 2. Process data (Count Students)
            long studentCount = allUsers.stream()
                    .filter(u -> "STUDENT".equalsIgnoreCase(u.role))
                    .count();

            // 3. Process data (Count Instructors)
            long instructorCount = allUsers.stream()
                    .filter(u -> "INSTRUCTOR".equalsIgnoreCase(u.role))
                    .count();

            // 4. Get Course Count
            int courseCount = course_dao.listCourses().size();

            // 5. Pack data for UI
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
        // Mock data for UI table
        return new Object[][]{
            {"System Login", "Admin", "Just Now"},
            {"Dashboard Loaded", "System", "1 min ago"},
            {"Database Check", "System", "5 mins ago"}
        };
    }

    // ==================================================================================
    // 2. USER MANAGEMENT (Middleman between AuthDAO and UserManagementPanel)
    // ==================================================================================

    public List<User> getAllUsers() throws SQLException {
        AccessControl.checkRole("ADMIN");
        return userDAO.listAllUsers();
    }

    // Used by UI to list users in table
    public List<AuthClass> listAuthUsers() throws Exception {
        AccessControl.checkRole("ADMIN");
        return auth_dao.listUsers();
    }

    // Used by UI to create a new user
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

        // Delegate to DAO
        auth_dao.insertUser(username.trim(), roleLabel.toUpperCase(), rawPassword);
    }

    // ==================================================================================
    // 3. COURSE MANAGEMENT
    // ==================================================================================

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

    // ==================================================================================
    // 4. SECTION MANAGEMENT (Middleman between UI Inputs and DAO)
    // ==================================================================================

    public List<Section> getAllSections() throws SQLException {
        AccessControl.checkRole("ADMIN");
        return sectionDAO.listAllSections();
    }

    /**
     * Used by SectionManagementPanel to fill the "Instructor" dropdown.
     * Fetches all users from AuthDAO, filters for INSTRUCTOR role.
     */
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

    /**
     * Takes raw strings from UI, resolves IDs, creates Object, sends to DAO.
     */
    public void createSectionFromUI(String courseCode, String instructorUsername, String room, String day, String time, int capacity) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();

        // 1. Logic: Convert UI Username -> Database ID
        List<AuthClass> instructors = getAllInstructors();
        Optional<AuthClass> instructorOpt = instructors.stream()
                .filter(u -> u.username.equals(instructorUsername))
                .findFirst();

        if (instructorOpt.isEmpty()) {
            throw new IllegalArgumentException("Selected instructor not found.");
        }
        
        AuthClass instructor = instructorOpt.get();
        long instructorId = instructor.user_id;

        // 2. Logic: Create Section Object (Using fixed constructor)
        Section s = new Section(
            0L,                 // Auto-increment ID
            courseCode,         
            instructorId,       
            instructor.username,     
            day,                
            time,               
            room,               
            capacity,           
            1,                  // Default Sem
            "MONSOON",          // Default Season
            2025                // Default Year
        );

        // 3. Data Access: Save to DB
        sectionDAO.insertSection(s);
    }

    // ==================================================================================
    // 5. MAINTENANCE
    // ==================================================================================

    public boolean isMaintenanceOn() {
        return maintenanceService.isMaintenanceOn();
    }

    public void setMaintenanceMode(boolean on) {
        AccessControl.checkRole("ADMIN");
        maintenanceService.toggleMaintenance(on);
    }
}