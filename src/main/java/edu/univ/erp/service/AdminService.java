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

/**
 * AdminService acts as the bridge between the Admin UI panels and the Database Layer.
 * It enforces RBAC and aggregates data for dashboards.
 */
public class AdminService {
    private final UserDAO userDAO = new UserDAO();
    private final SectionDAO sectionDAO = new SectionDAO();
    private final AuthDAO auth_dao = new AuthDAO();
    private final CourseDAO course_dao = new CourseDAO();
    private final MaintenanceService maintenanceService = new MaintenanceService();

    // ==================================================================================
    // 1. DASHBOARD STATISTICS (For AdminHomePanel)
    // ==================================================================================

    public Map<String, String> getDashboardStats() {
        AccessControl.checkRole("ADMIN"); 
        Map<String, String> stats = new HashMap<>();

        try {
            // Fetch live counts from DB
            List<AuthClass> allUsers = auth_dao.listUsers();
            
            long studentCount = allUsers.stream()
                    .filter(u -> "STUDENT".equalsIgnoreCase(u.role))
                    .count();

            long instructorCount = allUsers.stream()
                    .filter(u -> "INSTRUCTOR".equalsIgnoreCase(u.role))
                    .count();

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
        // Placeholder: In a real app, you would query an 'audit_logs' table here.
        return new Object[][]{
            {"System Login", "Admin", "Just Now"},
            {"Dashboard Loaded", "System", "1 min ago"},
            {"Database Check", "System", "5 mins ago"}
        };
    }

    // ==================================================================================
    // 2. USER MANAGEMENT
    // ==================================================================================

    public List<AuthClass> listAuthUsers() throws Exception {
        AccessControl.checkRole("ADMIN");
        return auth_dao.listUsers();
    }

    public void createAuthUser(String username, String rawPassword, String roleLabel) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();

        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username is required");
        if (rawPassword == null || rawPassword.isBlank()) throw new IllegalArgumentException("Password is required");
        
        // Validate Role
        try {
            Role.valueOf(roleLabel.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Role");
        }

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
    // 4. SECTION MANAGEMENT (Dynamic Integration)
    // ==================================================================================

    public List<Section> getAllSections() throws SQLException {
        AccessControl.checkRole("ADMIN");
        return sectionDAO.listAllSections();
    }

    /**
     * Helper to fetch instructor names for the UI Dropdown.
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
     * Bridges UI strings to Database logic for creating a section.
     */
    /**
     * Bridges UI strings to Database logic for creating a section.
     */
    public void createSectionFromUI(String courseCode, String instructorUsername, String room, String day, String time, int capacity) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();

        // 1. Find Instructor ID based on the username selected in UI
        List<AuthClass> instructors = getAllInstructors();
        Optional<AuthClass> instructorOpt = instructors.stream()
                .filter(u -> u.username.equals(instructorUsername))
                .findFirst();

        if (instructorOpt.isEmpty()) {
            throw new IllegalArgumentException("Selected instructor not found.");
        }
        
        AuthClass instructor = instructorOpt.get();
        long instructorId = instructor.user_id;
        String instructorName = instructor.username; // Using username as name for now

        // 2. Create Section Object using the FULL Constructor
        // Order: ID, Code, InstrID, InstrName, Day, Time, Room, Cap, SemNo, Season, Year
        Section s = new Section(
            0L,                 // sectionId (0 for new entry, DB handles auto-increment)
            courseCode,         // courseCode
            instructorId,       // instructorId
            instructorName,     // instructorName
            day,                // day
            time,               // timings
            room,               // classroom
            capacity,           // capacity
            1,                  // semesterNumber (Hardcoded defaults)
            "MONSOON",          // semesterSeason
            2025                // year
        );

        // 3. Persist
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