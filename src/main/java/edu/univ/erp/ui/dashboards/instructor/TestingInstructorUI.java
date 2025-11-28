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
            
     
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");

  
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {

        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("IIITD University ERP - Instructor Portal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setSize(1280, 850); 
            frame.setLocationRelativeTo(null); 

            
            InstructorService instructorService = new InstructorService();
            MaintenanceService maintenanceService = new MaintenanceService();

       
            try {
                GradeDAO2 gradeDAO = new GradeDAO2();
                gradeDAO.calculateFinalGrades(10L);   
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

            
            InstructorDashboardPanel dashboardPanel = new InstructorDashboardPanel(instructorService, maintenanceService);
            
            frame.add(dashboardPanel);
            frame.setVisible(true);
        });
    }
}