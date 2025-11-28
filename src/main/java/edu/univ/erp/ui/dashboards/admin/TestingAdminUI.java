package edu.univ.erp.ui.dashboards.admin;

import javax.swing.*;
import edu.univ.erp.service.MaintenanceService;
import edu.univ.erp.service.AdminService;
import edu.univ.erp.auth.session.Session; // Import Session
import edu.univ.erp.domain.User; // Import User

public class TestingAdminUI {
    public static void main(String[] args) {
        try {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.application.name", "IIITD ERP");
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(() -> {
            // --- SIMULATE LOGIN FOR TESTING ---
            // Create a dummy admin user so AccessControl doesn't block us
            User mockAdmin = new User(1L, "admin", "admin@univ.edu", "ADMIN");
            Session.setCurrentUser(mockAdmin);
            // ----------------------------------

            JFrame frame = new JFrame("IIITD University ERP - Admin Portal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280, 850); 
            frame.setLocationRelativeTo(null); 
            
            MaintenanceService maintenanceService = new MaintenanceService();
            AdminService adminService = new AdminService();
            
            AdminDashboardPanel dashboard = new AdminDashboardPanel(maintenanceService, adminService);
            frame.add(dashboard);
            
            frame.setVisible(true);
        });
    }
}