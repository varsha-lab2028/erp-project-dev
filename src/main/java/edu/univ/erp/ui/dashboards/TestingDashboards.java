package edu.univ.erp.ui.dashboards;

import edu.univ.erp.ui.dashboards.admin.AdminDashboardPanel;
import edu.univ.erp.ui.dashboards.instructor.InstructorDashboardPanel;

import javax.swing.*;
import java.awt.*;

public class TestingDashboards {
    public static void main(String[] args) {
        // 1. Apply System Look and Feel for native window borders
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("University ERP - Dashboard Suite");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280, 850);
            frame.setLocationRelativeTo(null); // Center on screen

            // 2. Create Tabs to switch between roles
            JTabbedPane tabbedPane = new JTabbedPane();
            
        // Tab 1: Admin
        JPanel adminInfoPanel = new JPanel(new BorderLayout());
        adminInfoPanel.add(new JLabel("<html>Admin dashboard UI moved.<br/>Please run <b>TestingAdminUI</b> for testing.</html>", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("Admin View", adminInfoPanel);

        // Tab 2: Instructor
        JPanel instructorInfoPanel = new JPanel(new BorderLayout());
        instructorInfoPanel.add(new JLabel("<html>Instructor dashboard UI moved.<br/>Please run <b>TestingInstructorUI</b> for testing.</html>", SwingConstants.CENTER), BorderLayout.CENTER);
        tabbedPane.addTab("Instructor View", instructorInfoPanel);

            // Style the tabs slightly
            tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            frame.add(tabbedPane);
            frame.setVisible(true);
        });
    }
}