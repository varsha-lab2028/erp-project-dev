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
    private final InstructorController instructorController; 

    private CardLayout cardLayout;
    private JPanel contentArea;
    private String currentScreen = "Dashboard";

    
    private final long CURRENT_INSTRUCTOR_ID = 2L;

    public InstructorDashboardPanel(InstructorService instructorService, MaintenanceService maintenanceService) {
        this.instructorService = instructorService;
        this.maintenanceService = maintenanceService;
        
       
        this.instructorController = new InstructorController(instructorService);
        
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        removeAll();
        
        
        DashboardComponents.SidebarPanel sidebar = new DashboardComponents.SidebarPanel("INSTRUCTOR", e -> onNavigate(e.getActionCommand()));
        sidebar.addItem("Dashboard", "⣿");
        sidebar.addItem("My Sections", "📅");
        sidebar.addItem("Gradebook", "📖");
        sidebar.addItem("Settings", "⚙️");
        add(sidebar, BorderLayout.WEST);

       
        JPanel mainArea = new JPanel(new BorderLayout());
        
        DashboardComponents.TopBarPanel topBar = new DashboardComponents.TopBarPanel(
            "Instructor Portal", 
            "DR", 
            e -> { /* Sidebar toggle logic */ },
            e -> onNavigate("Profile"),
            e -> toggleTheme()
        );
        mainArea.add(topBar, BorderLayout.NORTH);

        
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(DashboardTheme.BG_MAIN);

        // --- DASHBOARD PANELS ---
        
  
        contentArea.add(new InstructorHomePanel(instructorController, CURRENT_INSTRUCTOR_ID), "Dashboard");
        

        contentArea.add(new InstructorSectionsPanel(instructorService), "My Sections");
        contentArea.add(new GradebookPanel(instructorService), "Gradebook");
        

        contentArea.add(new AdminProfilePanel(), "Profile");

        contentArea.add(new MaintenancePanel(false, maintenanceService, null), "Settings"); 


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