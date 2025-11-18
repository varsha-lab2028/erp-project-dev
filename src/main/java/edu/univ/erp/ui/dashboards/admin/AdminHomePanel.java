package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminHomePanel extends JPanel {
    public AdminHomePanel() {
        setLayout(new BorderLayout());
        setBackground(DashboardTheme.BG_LIGHT);
        setBorder(new EmptyBorder(24, 24, 24, 24));
        
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(DashboardTheme.BG_LIGHT);

        // Stats Grid
        JPanel grid = new JPanel(new GridLayout(1, 4, 20, 0));
        grid.setBackground(DashboardTheme.BG_LIGHT);
        grid.setMaximumSize(new Dimension(2000, 140));
        grid.add(new DashboardComponents.StatsCard("Total Students", "1,204", "👥", DashboardTheme.ACCENT_YELLOW));
        grid.add(new DashboardComponents.StatsCard("Active Courses", "48", "📚", DashboardTheme.SECONDARY_GREEN));
        grid.add(new DashboardComponents.StatsCard("Instructors", "85", "👨‍🏫", DashboardTheme.ACCENT_PURPLE));
        grid.add(new DashboardComponents.StatsCard("Pending", "12", "⏳", DashboardTheme.ACCENT_ORANGE));
        body.add(grid);
        
        body.add(Box.createVerticalStrut(30));

        // Activity Table
        String[] cols = {"Activity", "User", "Time"};
        Object[][] data = {
            {"New Course Added (CSE101)", "Admin", "10:00 AM"},
            {"Student Registered (S. Gupta)", "System", "10:15 AM"},
            {"Maintenance Scheduled", "Admin", "1:00 PM"}
        };
        DashboardComponents.TablePanel activity = new DashboardComponents.TablePanel("Recent System Activity", cols, data);
        body.add(activity);

        add(new JScrollPane(body), BorderLayout.CENTER);
    }
}