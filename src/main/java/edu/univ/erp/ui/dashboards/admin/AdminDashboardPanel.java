package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import edu.univ.erp.service.MaintenanceService;

public class AdminDashboardPanel extends JPanel {
    
    private JPanel contentArea;
    private CardLayout cardLayout;

    private final MaintenanceService maintenanceService;

    public AdminDashboardPanel(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;

        setLayout(new BorderLayout());
        
        // 1. Initialize Sidebar with Navigation Logic
        DashboardComponents.SidebarPanel sidebar = new DashboardComponents.SidebarPanel("ADMIN", e -> onNavigate(e.getActionCommand()));
        sidebar.addItem("Dashboard", "🏠");
        sidebar.addItem("Students", "👨‍🎓");
        sidebar.addItem("Courses", "📚");
        sidebar.addItem("Sections", "🧩");
        sidebar.addItem("Settings", "⚙️");
        add(sidebar, BorderLayout.WEST);

        // 2. Content Area (CardLayout to swap views)
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(DashboardTheme.BG_LIGHT);
        
        // 3. Add Views to CardLayout
        contentArea.add(new AdminHomePanel(), "Dashboard");
        contentArea.add(new UserManagementPanel(), "Students"); // Reusing User Panel for Students
        contentArea.add(new CourseManagementPanel(), "Courses");
        contentArea.add(new SectionManagementPanel(), "Sections"); // Ensure you have this file or generic
        contentArea.add(new MaintenancePanel(), "Settings");

        // 4. Top Bar
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.add(new DashboardComponents.TopBarPanel("Admin Portal", "AD"), BorderLayout.NORTH);
        mainContainer.add(contentArea, BorderLayout.CENTER);
        
        add(mainContainer, BorderLayout.CENTER);
    }

    private void onNavigate(String screenName) {
        // Simple switch based on name. In real app, use constants.
        // If screenName matches "Settings", show Settings panel, etc.
        if (screenName.equals("Dashboard") || screenName.equals("Students") || 
            screenName.equals("Courses") || screenName.equals("Sections") || 
            screenName.equals("Settings")) {
            cardLayout.show(contentArea, screenName);
        }
    }
}
