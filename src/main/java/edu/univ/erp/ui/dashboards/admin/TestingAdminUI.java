package edu.univ.erp.ui.dashboards.admin;

import javax.swing.*;
import java.awt.*;
import edu.univ.erp.service.MaintenanceService;
import edu.univ.erp.service.AdminService;

public class TestingAdminUI {
    public static void main(String[] args) {
        // 1. Setup global UI properties for a smoother, professional look
        try {
            // macOS Specific: Integrates the menu bar with the system top bar & sets App Name
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.application.name", "IIITD ERP");
            
            // Text Rendering: Enables smoother font antialiasing
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");

            // Look & Feel: Use the native system look (buttons, scrollbars, window borders)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fallback to default if system L&F fails
        }
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("IIITD University ERP - Admin Portal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Increased resolution slightly to accommodate the sidebar and "floating" UI
            frame.setSize(1280, 850); 
            frame.setLocationRelativeTo(null); // Center on screen
            
            // Mock Services 
            // (Assuming these classes exist in your edu.univ.erp.service package)
            MaintenanceService maintenanceService = new MaintenanceService();
            AdminService adminService = new AdminService();
            
            // Initialize the Main Dashboard Panel
            AdminDashboardPanel dashboard = new AdminDashboardPanel(maintenanceService, adminService);
            frame.add(dashboard);
            
            frame.setVisible(true);
        });
    }
}