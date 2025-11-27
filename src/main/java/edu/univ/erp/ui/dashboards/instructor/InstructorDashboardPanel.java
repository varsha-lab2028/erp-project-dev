package edu.univ.erp.ui.dashboards.instructor;

import edu.univ.erp.service.MaintenanceService;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.service.InstructorService;
import edu.univ.erp.ui.dashboards.admin.MaintenancePanel;
// Ensure AdminProfilePanel exists, or use the placeholder logic below
import edu.univ.erp.ui.dashboards.admin.AdminProfilePanel;

import javax.swing.*;
import java.awt.*;

public class InstructorDashboardPanel extends JPanel {
    private final InstructorService instructorService;
    private final MaintenanceService maintenanceService;

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
        CardLayout cardLayout = new CardLayout();
        JPanel contentArea = new JPanel(cardLayout);
        contentArea.setBackground(DashboardTheme.BG_MAIN);

        // --- FIX: REMOVED "constraints:" FROM ALL LINES BELOW ---
        
        contentArea.add(new InstructorHomePanel(), "Dashboard");
        
        contentArea.add(new MySectionsPanel(instructorService), "My Sections");
        
        contentArea.add(new GradebookPanel(instructorService), "Gradebook");
        
        // Placeholder for Profile
        JPanel profilePlaceholder = new JPanel();
        profilePlaceholder.add(new JLabel("Profile Page Coming Soon"));
        contentArea.add(profilePlaceholder, "Profile");
        
        contentArea.add(new MaintenancePanel(), "Settings");

        // Show default
        cardLayout.show(contentArea, currentScreen);
        
        // --- FIX: REMOVED "key:" BELOW ---
        contentArea.putClientProperty("cardLayout", cardLayout);
        
        mainArea.add(contentArea, BorderLayout.CENTER);
        add(mainArea, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }

    private void onNavigate(String screenName) {
        currentScreen = screenName;
        Component centerComp = ((BorderLayout)getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (centerComp instanceof JPanel) {
            JPanel mainArea = (JPanel) centerComp;
            Component contentComp = ((BorderLayout)mainArea.getLayout()).getLayoutComponent(BorderLayout.CENTER);
            if (contentComp instanceof JPanel) {
                JPanel contentArea = (JPanel) contentComp;
                CardLayout cl = (CardLayout) contentArea.getLayout();
                cl.show(contentArea, screenName);
            }
        }
    }

    private void toggleTheme() {
        DashboardTheme.setTheme(!DashboardTheme.isDark);
        initUI();
    }
}