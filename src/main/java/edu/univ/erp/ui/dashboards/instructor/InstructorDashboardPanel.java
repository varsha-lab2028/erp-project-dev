package edu.univ.erp.ui.dashboards.instructor;

import edu.univ.erp.service.MaintenanceService;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.service.InstructorService;
import edu.univ.erp.ui.dashboards.admin.MaintenancePanel;
import edu.univ.erp.service.AdminService; // Import AdminService
// Ensure AdminProfilePanel exists, or use the placeholder logic below
import edu.univ.erp.ui.dashboards.admin.AdminProfilePanel;

import javax.swing.*;
import java.awt.*;

public class InstructorDashboardPanel extends JPanel {
    private final InstructorService instructorService;
    private final MaintenanceService maintenanceService;

    private CardLayout cardLayout;
    private JPanel contentArea;
    private String currentScreen = "Dashboard";

    public InstructorDashboardPanel(InstructorService instructorService, MaintenanceService maintenanceService) {
        this.instructorService = instructorService;
        this.maintenanceService = maintenanceService;
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        removeAll();
        
        // 1. Sidebar
        DashboardComponents.SidebarPanel sidebar = new DashboardComponents.SidebarPanel("INSTRUCTOR", e -> onNavigate(e.getActionCommand()));
        sidebar.addItem("Dashboard", "🏠");
        sidebar.addItem("My Sections", "📚");
        sidebar.addItem("Gradebook", "📝");
        sidebar.addItem("Profile", "👤");
        sidebar.addItem("Settings", "⚙️");
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

        // --- FIX: REMOVED "constraints:" FROM ALL LINES BELOW ---
        
        contentArea.add(new InstructorHomePanel(), "Dashboard");
        
        contentArea.add(new InstructorSectionsPanel(instructorService), "My Sections");
        
        contentArea.add(new GradebookPanel(instructorService), "Gradebook");
        
        // Shared Panels
        contentArea.add(new AdminProfilePanel(), "Profile");
        contentArea.add(new MaintenancePanel(false, maintenanceService, null), "Settings"); // AdminService is null for non-admins

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