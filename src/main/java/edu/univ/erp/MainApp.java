package edu.univ.erp;

import javax.swing.*;
import java.awt.*;

import edu.univ.erp.ui.login.LoginPanel;
import edu.univ.erp.ui.dashboard.DashboardPanel;
import edu.univ.erp.util.Theme;


public class MainApp {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainApp() {
        // Apply theme first
        Theme.applyTheme();

        // Create the main application window
        frame = new JFrame("University ERP System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Use CardLayout for panel navigation
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);


        initPanels();

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void initPanels() {
        LoginPanel loginPanel = new LoginPanel(this);
        DashboardPanel dashboardPanel = new DashboardPanel(this);

        mainPanel.add(loginPanel, "login");
        mainPanel.add(dashboardPanel, "dashboard");

        cardLayout.show(mainPanel, "login"); // Show login first
    }


    public void showDashboard() {
        cardLayout.show(mainPanel, "dashboard");
    }

    public void showLogin() {
        cardLayout.show(mainPanel, "login");
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}
