package edu.univ.backend.controller;

import edu.univ.erp.service.AdminService;
import edu.univ.erp.domain.User;
import edu.univ.erp.domain.Section;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for admin dashboard backend operations.
 * Acts as a bridge between the UI and AdminService.
 */
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    public AdminController() {
        this.adminService = new AdminService();
    }

    // Example: Fetch all users (admin use case)
    public List<User> getAllUsers() throws SQLException {
        return adminService.getAllUsers();
    }

    // Example: Fetch all sections for management
    public List<Section> getAllSections() throws SQLException {
        return adminService.getAllSections();
    }

    // Additional admin related controller methods can be added here for user management,
    // course and section management, etc.

}
