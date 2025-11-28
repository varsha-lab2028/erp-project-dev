package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.service.AdminService;
import edu.univ.erp.service.MaintenanceService;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardPanel extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel contentArea;
    private final MaintenanceService maintenanceService;
    private final AdminService adminService;

    public AdminDashboardPanel(MaintenanceService maintenanceService, AdminService adminService) {
        this.maintenanceService = maintenanceService;
        this.adminService = adminService;
        setLayout(new BorderLayout());

     
        DashboardComponents.SidebarPanel sidebar = new DashboardComponents.SidebarPanel("ADMIN", e -> onNavigate(e.getActionCommand()));
        sidebar.addItem("Dashboard", "🏠");
        sidebar.addItem("Courses", "📚");
        sidebar.addItem("Sections", "📝");
        sidebar.addItem("Users", "👥");
        sidebar.addItem("Settings", "⚙️");
        add(sidebar, BorderLayout.WEST);

       
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(DashboardTheme.BG_MAIN);

       
        DashboardComponents.TopBarPanel topBar = new DashboardComponents.TopBarPanel(
                "Admin Portal",
                "AD",
                e -> { /* Sidebar toggle logic */ },
                e -> onNavigate("Profile"),
                e -> { /* Theme toggle logic */ }
        );
        mainArea.add(topBar, BorderLayout.NORTH);

        
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setOpaque(false);

      
        contentArea.add(createPlaceholderPanel("Admin Dashboard"), "Dashboard");
        contentArea.add(createPlaceholderPanel("Course Management"), "Courses");
        contentArea.add(createPlaceholderPanel("Section Management"), "Sections");
        contentArea.add(createPlaceholderPanel("User Management"), "Users");
        contentArea.add(new MaintenancePanel(true, maintenanceService, this::onMaintenanceToggle), "Settings");
        contentArea.add(new AdminProfilePanel(), "Profile");

        mainArea.add(contentArea, BorderLayout.CENTER);
        add(mainArea, BorderLayout.CENTER);

        cardLayout.show(contentArea, "Dashboard");
    }

    private void onNavigate(String screenName) {
        cardLayout.show(contentArea, screenName);
    }

    private void onMaintenanceToggle(boolean isEnabled) {
   
        System.out.println("Maintenance mode toggled to: " + isEnabled);
    }

    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(new JLabel(text + " - Coming Soon!"));
        return panel;
    }
}