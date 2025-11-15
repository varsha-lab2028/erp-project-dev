package edu.univ.erp.ui.student;
import javax.swing.*;

public class TestingStudentUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentDashboard dash = new StudentDashboard();
            dash.setVisible(true);
        });
    }
}
