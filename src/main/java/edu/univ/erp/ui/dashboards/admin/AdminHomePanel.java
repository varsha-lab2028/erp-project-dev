package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.service.AdminService;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
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

        // --- Fetch Real Data ---
        Map<String, String> stats = adminService.getDashboardStats();

        // 1. Stats Grid
        JPanel grid = new JPanel(new GridLayout(1, 4, 20, 0));
        grid.setBackground(DashboardTheme.BG_MAIN);
        grid.setMaximumSize(new Dimension(2000, 120));
        
        grid.add(new DashboardComponents.StatsCard("Total Students", stats.get("students"), "👥", DashboardTheme.INFO));
        grid.add(new DashboardComponents.StatsCard("Active Courses", stats.get("courses"), "📚", DashboardTheme.SUCCESS));
        grid.add(new DashboardComponents.StatsCard("Instructors", stats.get("instructors"), "🎓", DashboardTheme.WARNING));
        
        String status = stats.get("status");
        Color statusColor = "Good".equals(status) ? DashboardTheme.SUCCESS : DashboardTheme.DANGER;
        grid.add(new DashboardComponents.StatsCard("System Status", status, "🖥️", statusColor));
        
        body.add(grid);
        body.add(Box.createVerticalStrut(30));

        // 2. Split View
        JPanel splitView = new JPanel(new GridLayout(1, 2, 25, 0));
        splitView.setBackground(DashboardTheme.BG_MAIN);
        
        // Left: Recent Activity (Fetched from Service)
        String[] cols = {"Activity Description", "User", "Time"};
        Object[][] activityData = adminService.getRecentActivity();
        
        splitView.add(new DashboardComponents.TablePanel("Recent System Activity", cols, activityData));

        // Right: Quick Actions (Static UI controls)
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
        buttonGrid.add(DashboardComponents.createPrimaryButton("Add New Student")); // Can be linked later
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