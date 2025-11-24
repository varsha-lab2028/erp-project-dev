package edu.univ.erp.ui.dashboards.instructor;

import javax.swing.*;

import edu.univ.erp.service.StudentService;
import edu.univ.erp.service.MaintenanceService;
import edu.univ.erp.service.InstructorService;

public class TestingInstructorUI {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("University ERP - Instructor");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);

            StudentService studentService = new StudentService();
            MaintenanceService maintenanceService = new MaintenanceService();
            InstructorService instructorService = new InstructorService();

            frame.add(new InstructorDashboardPanel(studentService, maintenanceService, instructorService));
            frame.setVisible(true);
        });
    }
}
