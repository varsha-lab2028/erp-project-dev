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


import edu.univ.erp.service.StudentService;
import edu.univ.erp.service.MaintenanceService;

import edu.univ.erp.auth.AuthenticationService;
import edu.univ.erp.auth.LoginManager;

// New Service Registry to hold backend services centrally
class ServiceRegistry {
    public final StudentService studentService;
    public final MaintenanceService maintenanceService;
    public final AuthenticationService authService;

    public ServiceRegistry() {
        this.studentService = new StudentService();
        this.maintenanceService = new MaintenanceService();
        this.authService = (AuthenticationService) new LoginManager(); // casting explicitly
    }
}

public class MainApp {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private ServiceRegistry services;

    private StudentDashboardPanel studentDashboard;
    private AdminDashboardPanel adminDashboard;
    private InstructorDashboardPanel instructorDashboard;

    public MainApp() {
        //applying the theme first
        LoginTheme.applyTheme();

        // Initialize backend services centrally
        services = new ServiceRegistry();

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
        // Provide centralized AuthenticationService to LoginController
        LoginController loginController = new LoginController(services.authService);
        LoginPanel loginPanel = new LoginPanel(loginController);

        //create dashboards with service injection
        studentDashboard = new StudentDashboardPanel(services.studentService);
        adminDashboard = new AdminDashboardPanel(services.maintenanceService);
        //instructorDashboard = new InstructorDashboardPanel(services.studentService, services.maintenanceService);

        //register all cards with the layout
        mainPanel.add("login", loginPanel);
        mainPanel.add("studentDashboard", studentDashboard);
        mainPanel.add("adminDashboard", adminDashboard);
        mainPanel.add("instructorDashboard", instructorDashboard);

        //showing the login window first
        cardLayout.show(mainPanel, "login");
    }

    public void showDashboard(String role) {
        switch(role.toLowerCase()) {
            case "student":
                cardLayout.show(mainPanel, "studentDashboard");
                break;
            case "instructor":
                cardLayout.show(mainPanel, "instructorDashboard");
                break;
            case "admin":
                cardLayout.show(mainPanel, "adminDashboard");
                break;
            default:
                cardLayout.show(mainPanel, "login"); // fallback to login
                break;
        }
    }

    public void showLogin() {
        cardLayout.show(mainPanel, "login");
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}
