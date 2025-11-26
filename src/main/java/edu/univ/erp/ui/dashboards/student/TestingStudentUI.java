package edu.univ.erp.ui.dashboards.student;

import javax.swing.*;
import edu.univ.erp.service.StudentService;

public class TestingStudentUI {
    public static void main(String[] args) {
        try {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.application.name", "IIITD ERP");
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("IIITD ERP - Student Portal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280, 850);
            frame.setLocationRelativeTo(null);
            
            StudentService service = new StudentService();
            frame.add(new StudentDashboardPanel(service));
            frame.setVisible(true);
        });
    }
}