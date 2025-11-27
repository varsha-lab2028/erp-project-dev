package edu.univ.erp.ui.dashboards.instructor;

import javax.swing.*;
import edu.univ.erp.service.InstructorService;
import edu.univ.erp.service.MaintenanceService;
import edu.univ.erp.ui.common.DashboardTheme;

public class TestingInstructorUI {
    public static void main(String[] args) {
        try {
            // 1. macOS Specific Integration (Top Menu Bar & App Name)
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.application.name", "IIITD ERP");
            
            // 2. Text Rendering: Enables smoother font antialiasing
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");

            // 3. Set Native System Look and Feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fallback to default if system L&F fails
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("IIITD University ERP - Instructor Portal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Resolution set to accommodate sidebar and content comfortably
            frame.setSize(1280, 850); 
            frame.setLocationRelativeTo(null); // Center on screen

            // Initialize required services
            // Ensure your database connection is valid inside these services/DAOs
            InstructorService instructorService = new InstructorService();
            MaintenanceService maintenanceService = new MaintenanceService();

            // Load the updated Instructor Dashboard
            // This panel will now automatically instantiate the InstructorController internally
            InstructorDashboardPanel dashboardPanel = new InstructorDashboardPanel(instructorService, maintenanceService);
            
            frame.add(dashboardPanel);

            frame.setVisible(true);
        });
    }
}