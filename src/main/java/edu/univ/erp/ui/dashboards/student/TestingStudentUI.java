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
            JFrame frame = new JFrame("Testing Student Dashboard");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280, 800);
            frame.setLocationRelativeTo(null); // Center on screen

            StudentDashboardPanel dashboard = new StudentDashboardPanel();
            frame.add(dashboard);
            frame.setVisible(true);
        });
    }
}
