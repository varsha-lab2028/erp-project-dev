package edu.univ.backend.controller;

import edu.univ.erp.service.AdminService;
import edu.univ.erp.domain.AuthClass; // <--- ADD THIS
import edu.univ.erp.domain.Course;    // <--- ADD THIS
import edu.univ.erp.domain.Section;
import edu.univ.erp.domain.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Controller for admin dashboard backend operations.
 * Acts as a bridge between the UI/API and AdminService.
 */
public class AdminController {

    private final AdminService adminService;

    // Constructor Injection
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // Default Constructor
    public AdminController() {
        this.adminService = new AdminService();
    }

    // ==================================================================================
    // 1. DASHBOARD & STATS
    // ==================================================================================

    public Map<String, String> getDashboardStats() {
        return adminService.getDashboardStats();
    }

    public Object[][] getRecentActivity() {
        return adminService.getRecentActivity();
    }

    // ==================================================================================
    // 2. USER MANAGEMENT
    // ==================================================================================

    public List<User> getAllProfiles() throws SQLException {
        return adminService.getAllUsers();
    }

    public List<AuthClass> getSystemUsers() throws Exception {
        return adminService.listAuthUsers();
    }

    public void createUser(String username, String password, String role) throws Exception {
        adminService.createAuthUser(username, password, role);
    }

    // ==================================================================================
    // 3. COURSE MANAGEMENT
    // ==================================================================================

    public List<Course> getAllCourses() throws SQLException {
        return adminService.getAllCourses();
    }

    public void addCourse(String code, String name, int credits) throws Exception {
        adminService.createCourse(code, name, credits);
    }

    public void updateCourse(String code, String name, int credits) throws Exception {
        adminService.editCourse(code, name, credits);
    }

    public void deleteCourse(String code) throws Exception {
        adminService.deleteCourse(code);
    }

    // ==================================================================================
    // 4. SECTION MANAGEMENT
    // ==================================================================================

    public List<Section> getAllSections() throws SQLException {
        return adminService.getAllSections();
    }

    public List<AuthClass> getInstructors() throws SQLException {
        return adminService.getAllInstructors();
    }

    public void createSection(String courseCode, String instructorUser, String room, String day, String time, int capacity) throws Exception {
        adminService.createSectionFromUI(courseCode, instructorUser, room, day, time, capacity);
    }

    // ==================================================================================
    // 5. MAINTENANCE
    // ==================================================================================

    public boolean isMaintenanceMode() {
        return adminService.isMaintenanceOn();
    }

    public void setMaintenanceMode(boolean enable) {
        adminService.setMaintenanceMode(enable);
    }
}