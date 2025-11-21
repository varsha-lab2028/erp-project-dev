package edu.univ.erp.ui.dashboards.student;

import javax.swing.*;
import java.awt.*;

public class TestingStudentUI {
    public static void main(String[] args) {
        // Set the look and feel to the system default for better aesthetics
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            StudentDashboardPanel dashboard = new StudentDashboardPanel();
            dashboard.setVisible(true);
        });
    }
}
