package edu.univ.erp.ui.dashboards.admin;

import javax.swing.*;

public class TestingAdminUI {
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("University ERP - Admin");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            
            frame.add(new AdminDashboardPanel());
            
            frame.setVisible(true);
        });
    }
}