package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.service.AdminService;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AdminHomePanel extends JPanel {
    
    private final AdminService adminService;

    public AdminHomePanel(AdminService adminService) {
        this.adminService = adminService;
        setLayout(new BorderLayout());
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        
        initUI();
    }

    private void initUI() {
        removeAll();
        
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(DashboardTheme.BG_MAIN);

        // --- Fetch Data with Error Handling ---
        Map<String, String> stats = new HashMap<>();
        Object[][] activityData;

        try {
            // Try to fetch real stats (requires active session)
            stats = adminService.getDashboardStats();
            activityData = adminService.getRecentActivity();
        } catch (RuntimeException e) {
            // Fallback for Testing/No Session: prevent crash
            System.err.println("AdminHomePanel: Could not fetch stats (likely not logged in). Using placeholders.");
            stats.put("students", "-");
            stats.put("courses", "-");
            stats.put("instructors", "-");
            stats.put("status", "Offline");
            
            activityData = new Object[][]{ {"No data (Login required)", "-", "-"} };
        }

        // 1. Stats Grid
        JPanel grid = new JPanel(new GridLayout(1, 4, 20, 0));
        grid.setBackground(DashboardTheme.BG_MAIN);
        grid.setMaximumSize(new Dimension(2000, 120));
        
        grid.add(new DashboardComponents.StatsCard("Total Students", stats.getOrDefault("students", "-"), "👥", DashboardTheme.INFO));
        grid.add(new DashboardComponents.StatsCard("Active Courses", stats.getOrDefault("courses", "-"), "📚", DashboardTheme.SUCCESS));
        grid.add(new DashboardComponents.StatsCard("Instructors", stats.getOrDefault("instructors", "-"), "🎓", DashboardTheme.WARNING));
        
        String status = stats.getOrDefault("status", "Unknown");
        Color statusColor = "Good".equals(status) ? DashboardTheme.SUCCESS : DashboardTheme.DANGER;
        grid.add(new DashboardComponents.StatsCard("System Status", status, "🖥️", statusColor));
        
        body.add(grid);
        body.add(Box.createVerticalStrut(30));

        // 2. Split View
        JPanel splitView = new JPanel(new GridLayout(1, 2, 25, 0));
        splitView.setBackground(DashboardTheme.BG_MAIN);
        
        // Left: Recent Activity
        String[] cols = {"Activity Description", "User", "Time"};
        splitView.add(new DashboardComponents.TablePanel("Recent System Activity", cols, activityData));

        // Right: Quick Actions
        JPanel actionsCard = new JPanel(new BorderLayout());
        actionsCard.setBackground(Color.WHITE);
        actionsCard.setBorder(BorderFactory.createCompoundBorder(
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
            { setBorder(null); getViewport().setBackground(DashboardTheme.BG_MAIN); }
        }, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }
}