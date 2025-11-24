package edu.univ.erp.ui.dashboards.admin;

import javax.swing.*;
import edu.univ.erp.service.MaintenanceService;
import edu.univ.erp.service.AdminService;

public class TestingAdminUI {
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("University ERP - Admin");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            
            MaintenanceService maintenanceService = new MaintenanceService();
            AdminService adminService = new AdminService();
            frame.add(new AdminDashboardPanel(maintenanceService, adminService));
            
            frame.setVisible(true);
        });
    }
}