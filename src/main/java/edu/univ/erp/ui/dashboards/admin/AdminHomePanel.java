package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class AdminHomePanel extends JPanel {
    public AdminHomePanel() {
        setLayout(new BorderLayout());
        setBackground(DashboardTheme.BG_MAIN);
        // FIX: Removed 'top:', 'left:', etc.
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(DashboardTheme.BG_MAIN);

        // 1. Stats Grid
        // FIX: Removed 'rows:', 'cols:', 'hgap:', 'vgap:'
        JPanel grid = new JPanel(new GridLayout(1, 4, 20, 0));
        grid.setBackground(DashboardTheme.BG_MAIN);
        // FIX: Removed 'width:', 'height:'
        grid.setMaximumSize(new Dimension(2000, 120));
        
        // FIX: Removed 'title:', 'value:', 'accent:'
        grid.add(new DashboardComponents.StatsCard("Total Students", "2", "👥", DashboardTheme.INFO));
        grid.add(new DashboardComponents.StatsCard("Active Courses", "5", "📚", DashboardTheme.SUCCESS));
        grid.add(new DashboardComponents.StatsCard("Instructors", "10", "👨‍🏫", DashboardTheme.WARNING));
        grid.add(new DashboardComponents.StatsCard("System Status", "Good", "⚡", DashboardTheme.DANGER));
        
        body.add(grid);
        // FIX: Removed 'height:'
        body.add(Box.createVerticalStrut(30));

        // 2. Split View: Activity & Quick Actions
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
        // FIX: Removed 'title:'
        splitView.add(new DashboardComponents.TablePanel("Recent System Activity", cols, data));

        // Right: Quick Actions
        JPanel actionsCard = new JPanel(new BorderLayout());
        actionsCard.setBackground(Color.WHITE);
        actionsCard.setBorder(BorderFactory.createCompoundBorder(
             // FIX: Removed 'r:', 'g:', 'b:'
             new LineBorder(new Color(226, 232, 240), 1),
             new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel actionTitle = new JLabel("Quick Actions");
        actionTitle.setFont(DashboardTheme.FONT_SUBTITLE);
        actionTitle.setBorder(new EmptyBorder(0,0,20,0));
        actionsCard.add(actionTitle, BorderLayout.NORTH);
        
        JPanel buttonGrid = new JPanel(new GridLayout(3, 1, 0, 15));
        buttonGrid.setBackground(Color.WHITE);
        buttonGrid.add(DashboardComponents.createPrimaryButton("Add New Student"));
        buttonGrid.add(DashboardComponents.createPrimaryButton("Generate Reports"));
        buttonGrid.add(DashboardComponents.createPrimaryButton("System Backup"));
        
        actionsCard.add(buttonGrid, BorderLayout.CENTER);
        splitView.add(actionsCard);

        body.add(splitView);

        add(new JScrollPane(body) {
            { 
                setBorder(null); 
                getViewport().setBackground(DashboardTheme.BG_MAIN); 
            }
        }, BorderLayout.CENTER);
    }
}