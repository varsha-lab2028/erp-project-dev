package edu.univ.erp.ui.dashboards.instructor;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import java.awt.*;

public class InstructorDashboardPanel extends JPanel {
    private JPanel contentArea;
    private CardLayout cardLayout;

    public InstructorDashboardPanel() {
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
        contentArea.add(new MySectionsPanel(), "My Sections");
        contentArea.add(new GradebookPanel(), "Gradebook");
        // contentArea.add(new SettingsPanel(), "Settings"); // Placeholder

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
