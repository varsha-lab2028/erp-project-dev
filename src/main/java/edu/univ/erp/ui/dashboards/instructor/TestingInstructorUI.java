package edu.univ.erp.ui.dashboards.instructor;

import javax.swing.*;
import edu.univ.erp.service.InstructorService;
import edu.univ.erp.service.MaintenanceService;
import edu.univ.erp.data.GradeDAO2;
import edu.univ.erp.ui.common.DashboardTheme;

public class TestingInstructorUI {
    public static void main(String[] args) {
        try {
            //macOS Specific Integration
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.application.name", "IIITD ERP");
            
            //Text Rendering: Enables smoother font antialiasing
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");

            //Set Native System Look and Feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {

        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("IIITD University ERP - Instructor Portal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setSize(1280, 850); 
            frame.setLocationRelativeTo(null); // Center on screen

            // Ensure your database connection is valid inside these services/DAOs
            InstructorService instructorService = new InstructorService();
            MaintenanceService maintenanceService = new MaintenanceService();

            // ---------- ADDED: pre-compute final grades for a test section ----------
            // Example: section_id = 10 (your CSE101 section for Instructor 1)
            try {
                GradeDAO2 gradeDAO = new GradeDAO2();
                gradeDAO.calculateFinalGrades(10L);   // <-- change 10L if your test section is different
                System.out.println("Final grades computed for section 10.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        frame,
                        "Failed to compute final grades in test harness: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            // Load the updated Instructor Dashboard
            InstructorDashboardPanel dashboardPanel = new InstructorDashboardPanel(instructorService, maintenanceService);
            
            frame.add(dashboardPanel);
            frame.setVisible(true);
        });
    }
}