package edu.univ.erp.ui.dashboards.instructor;

import com.sun.tools.javac.Main;
import edu.univ.erp.domain.Instructor;
import edu.univ.erp.service.MaintenanceService;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.service.InstructorService;
import edu.univ.erp.ui.dashboards.admin.MaintenancePanel;

import javax.swing.*;
import java.awt.*;

public class InstructorDashboardPanel extends JPanel {
    private JPanel contentArea;
    private CardLayout cardLayout;
    private final InstructorService instructor_service;
    private final MaintenanceService maintenance_service;

    public InstructorDashboardPanel(InstructorService instructor_service, MaintenanceService maintenance_service) {
        this.instructor_service = instructor_service;
        this.maintenance_service = maintenance_service;
        setLayout(new BorderLayout());
        
        //side panel for the instructor
        DashboardComponents.SidebarPanel sidebar = new DashboardComponents.SidebarPanel("INSTRUCTOR", this::onNavigate);
        sidebar.addItem("Dashboard", "🏠");
        sidebar.addItem("My Sections", "📅");
        sidebar.addItem("Gradebook", "📝");
        sidebar.addItem("Analytics", "📊");
        sidebar.addItem("Settings", "⚙️");
        add(sidebar, BorderLayout.WEST);

        // 2. Content Area (CardLayout)
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(DashboardTheme.BG_LIGHT);

        // 3. Add Views
        contentArea.add(new InstructorHomePanel(), "Dashboard");
        contentArea.add(new MySectionsPanel(instructor_service), "My Sections");
        contentArea.add(new GradebookPanel(instructor_service), "Gradebook");
        contentArea.add(new MaintenancePanel(), "Settings");

        // 4. Top Bar
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.add(new DashboardComponents.TopBarPanel("Instructor Portal", "DR"), BorderLayout.NORTH);
        mainContainer.add(contentArea, BorderLayout.CENTER);
        add(mainContainer, BorderLayout.CENTER);
    }

    private void onNavigate(java.awt.event.ActionEvent e) {
        String screenName = e.getActionCommand();
        if (screenName.equals("Dashboard") || screenName.equals("My Sections") ||
            screenName.equals("Gradebook")) {
            cardLayout.show(contentArea, screenName);
        }
    }
}
