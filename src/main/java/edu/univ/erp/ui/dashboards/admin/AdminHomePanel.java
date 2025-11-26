package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminHomePanel extends JPanel {
    public AdminHomePanel() {
        setLayout(new BorderLayout());
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(DashboardTheme.BG_MAIN);

        // 1. Stats Grid
        JPanel grid = new JPanel(new GridLayout(1, 4, 20, 0));
        grid.setBackground(DashboardTheme.BG_MAIN);
        grid.setMaximumSize(new Dimension(2000, 110)); // Height constraint
        
        grid.add(new DashboardComponents.StatsCard("Total Students", "1,204", DashboardTheme.PRIMARY));
        grid.add(new DashboardComponents.StatsCard("Active Courses", "48", DashboardTheme.ACCENT));
        grid.add(new DashboardComponents.StatsCard("Instructors", "85", DashboardTheme.WARNING));
        grid.add(new DashboardComponents.StatsCard("System Status", "98%", DashboardTheme.SUCCESS));
        
        body.add(grid);
        body.add(Box.createVerticalStrut(30));

        // 2. Split View
        JPanel splitView = new JPanel(new GridLayout(1, 2, 25, 0));
        splitView.setBackground(DashboardTheme.BG_MAIN);
        
        // Left: Activity Table
        String[] cols = {"Activity Description", "User", "Time"};
        Object[][] data = {
            {"New Course Added (CSE101)", "Admin", "10:00 AM"},
            {"Student Registered (S. Gupta)", "System", "10:15 AM"},
            {"Maintenance Scheduled", "Admin", "01:00 PM"},
            {"Gradebook Locked", "Instructor", "02:30 PM"}
        };
        splitView.add(new DashboardComponents.TablePanel("Recent System Activity", cols, data));

        // Right: Quick Actions (FIXED LAYOUT)
        DashboardComponents.CardPanel actionsCard = new DashboardComponents.CardPanel();
        actionsCard.setLayout(new BorderLayout());
        
        JLabel actionTitle = new JLabel("Quick Actions");
        actionTitle.setFont(DashboardTheme.FONT_SUBTITLE);
        actionTitle.setBorder(new EmptyBorder(15, 20, 15, 0));
        actionsCard.add(actionTitle, BorderLayout.NORTH);
        
        // Use a Wrapper panel with BorderLayout to prevent vertical stretching
        JPanel actionsWrapper = new JPanel(new BorderLayout());
        actionsWrapper.setOpaque(false);
        actionsWrapper.setBorder(new EmptyBorder(0, 20, 20, 20));

        JPanel buttonGrid = new JPanel(new GridLayout(3, 1, 0, 15));
        buttonGrid.setOpaque(false);
        
        // Add buttons
        buttonGrid.add(DashboardComponents.createPrimaryButton("Add New Student"));
        buttonGrid.add(DashboardComponents.createPrimaryButton("Generate Term Reports"));
        buttonGrid.add(DashboardComponents.createPrimaryButton("Run System Backup"));
        
        // Add grid to NORTH so it doesn't expand to fill height
        actionsWrapper.add(buttonGrid, BorderLayout.NORTH);
        
        actionsCard.add(actionsWrapper, BorderLayout.CENTER);
        splitView.add(actionsCard);

        body.add(splitView);

        add(new JScrollPane(body) {
            { setBorder(null); getViewport().setBackground(DashboardTheme.BG_MAIN); }
        }, BorderLayout.CENTER);
    }
}