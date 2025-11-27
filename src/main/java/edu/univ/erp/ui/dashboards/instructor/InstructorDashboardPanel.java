package edu.univ.erp.ui.dashboards.instructor;

import edu.univ.backend.controller.InstructorController;
import edu.univ.erp.service.AdminService;
import edu.univ.erp.service.InstructorService;
import edu.univ.erp.service.MaintenanceService;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.ui.dashboards.admin.AdminProfilePanel;
import edu.univ.erp.ui.dashboards.admin.MaintenancePanel;

import javax.swing.*;
import java.awt.*;

public class InstructorDashboardPanel extends JPanel {
    private final InstructorService instructorService;
    private final MaintenanceService maintenanceService;
    private final InstructorController instructorController; // Added Controller

    private CardLayout cardLayout;
    private JPanel contentArea;
    private String currentScreen = "Dashboard";

    // Hardcoded ID for the current session (replace with real session ID later)
    private final long CURRENT_INSTRUCTOR_ID = 2L;

    public InstructorDashboardPanel(InstructorService instructorService, MaintenanceService maintenanceService) {
        this.instructorService = instructorService;
        this.maintenanceService = maintenanceService;
        
        // Initialize the Controller using the service
        this.instructorController = new InstructorController(instructorService);
        
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        removeAll();
        
        // 1. Sidebar
        DashboardComponents.SidebarPanel sidebar = new DashboardComponents.SidebarPanel("INSTRUCTOR", e -> onNavigate(e.getActionCommand()));
        sidebar.addItem("Dashboard", "匠");
        sidebar.addItem("My Sections", "答");
        sidebar.addItem("Gradebook", "統");
        sidebar.addItem("Profile", "側");
        sidebar.addItem("Settings", "笞呻ｸ");
        add(sidebar, BorderLayout.WEST);

        // 2. Main Area
        JPanel mainArea = new JPanel(new BorderLayout());
        
        DashboardComponents.TopBarPanel topBar = new DashboardComponents.TopBarPanel(
            "Instructor Portal", 
            "DR", 
            e -> { /* Sidebar toggle logic */ },
            e -> onNavigate("Profile"),
            e -> toggleTheme()
        );
        mainArea.add(topBar, BorderLayout.NORTH);

        // 3. Content Cards
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(DashboardTheme.BG_MAIN);

        // --- DASHBOARD PANELS ---
        
        // Updated: Pass the Controller and ID to the Home Panel for stats
        contentArea.add(new InstructorHomePanel(instructorController, CURRENT_INSTRUCTOR_ID), "Dashboard");
        
        // Existing panels
        contentArea.add(new InstructorSectionsPanel(instructorService), "My Sections");
        contentArea.add(new GradebookPanel(instructorService), "Gradebook");
        
        // Shared Panels
        contentArea.add(new AdminProfilePanel(), "Profile");
        // AdminService is null for non-admins here
        contentArea.add(new MaintenancePanel(false, maintenanceService, null), "Settings"); 

        // Show default
        cardLayout.show(contentArea, currentScreen);

        mainArea.add(contentArea, BorderLayout.CENTER);
        add(mainArea, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }

    private void onNavigate(String screenName) {
        currentScreen = screenName;
        cardLayout.show(contentArea, screenName);
    }

    private void toggleTheme() {
        DashboardTheme.setTheme(!DashboardTheme.isDark);
        initUI();
    }
}