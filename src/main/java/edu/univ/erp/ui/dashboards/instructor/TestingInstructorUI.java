package edu.univ.erp.ui.dashboards.instructor;

import javax.swing.*;

public class TestingInstructorUI {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            InstructorDashboardPanel dashboard = new InstructorDashboardPanel();
            dashboard.setVisible(true);
        });
    }
}
