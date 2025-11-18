package edu.univ.erp.ui.common;
import javax.swing.*;

public class TestingBaseDashboardUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // BaseDashboard requires a title string in its constructor
            BaseDashboard dash = new BaseDashboard("Base Dashboard Preview");
            dash.setVisible(true);
        });
    }
}
