package edu.univ.erp.ui.dashboards.instructor;

import javax.swing.*;
import edu.univ.erp.service.InstructorService;

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
            
            // Increased resolution to accommodate the sidebar and "floating" UI comfortably
            frame.setSize(1280, 850); 
            frame.setLocationRelativeTo(null); // Center on screen

            // Initialize required services
            InstructorService instructorService = new InstructorService();

            // Load the updated Instructor Dashboard
            frame.add(new InstructorDashboardPanel(instructorService));

            frame.setVisible(true);
        });
    }
}