package edu.univ.erp;

import javax.swing.*;
import java.awt.*;

import edu.univ.erp.auth.AuthenticationService;
import edu.univ.erp.ui.login.LoginPanel;
import edu.univ.erp.ui.login.LoginController;
import edu.univ.erp.auth.LoginManager;
import edu.univ.erp.ui.dashboards.student.StudentDashboardPanel;
import edu.univ.erp.ui.dashboards.admin.AdminDashboardPanel;
import edu.univ.erp.ui.dashboards.instructor.InstructorDashboardPanel;
import edu.univ.erp.util.LoginTheme;


public class MainApp {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private StudentDashboardPanel studentDashboard;
    private AdminDashboardPanel adminDashboard;
    private InstructorDashboardPanel instructorDashboard;

    public MainApp() {
        //applying the theme first
        LoginTheme.applyTheme();

        //create the main window
        frame = new JFrame("University ERP System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        //Use CardLayout for panel navigation
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        initPanels();

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void initPanels() {
        LoginManager loginManager = new LoginManager();
        LoginController loginController = new LoginController((AuthenticationService) loginManager);
        LoginPanel loginPanel = new LoginPanel(loginController);

        //create dashboards
        studentDashboard = new StudentDashboardPanel();
        adminDashboard = new AdminDashboardPanel();
        instructorDashboard = new InstructorDashboardPanel();

        //register all cards with the layout
        mainPanel.add("login", loginPanel);
        mainPanel.add("studentDashboard", studentDashboard);
        mainPanel.add("adminDashboard", adminDashboard);
        mainPanel.add("instructorDashboard", instructorDashboard);

        //showing the login window first
        cardLayout.show(mainPanel, "login");
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
