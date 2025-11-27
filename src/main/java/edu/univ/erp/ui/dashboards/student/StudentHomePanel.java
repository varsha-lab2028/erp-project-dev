package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StudentHomePanel extends JPanel {
    public StudentHomePanel() {
        setLayout(new BorderLayout());
        setBackground(DashboardTheme.BG_MAIN);
        
        // Clean border definition without "top:", "left:", etc.
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(DashboardTheme.BG_MAIN);

        // 1. Stats Grid (Student Specific)
        JPanel grid = new JPanel(new GridLayout(1, 3, 20, 0));
        grid.setBackground(DashboardTheme.BG_MAIN);
        grid.setMaximumSize(new Dimension(2000, 120));
        
        // Fixed: Added the missing 4th argument (Color) for all cards
        grid.add(new DashboardComponents.StatsCard("Current CGPA", "3.8", "🎓", DashboardTheme.PRIMARY));
        grid.add(new DashboardComponents.StatsCard("Credits Earned", "85", "⭐", DashboardTheme.WARNING));
        grid.add(new DashboardComponents.StatsCard("Attendance", "92%", "✅", DashboardTheme.SUCCESS));
        
        body.add(grid);
        body.add(Box.createVerticalStrut(30));

        // 2. Timetable Section
        // Fixed: Removed "title:" label from constructor
        JPanel timetableCard = new DashboardComponents.TablePanel(
            "My Class Schedule", 
            new String[]{"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"},
            new Object[][]{
                {"09:00", "CSE101 (C-01)", "-", "CSE101 (C-01)", "-", "Lab"},
                {"10:00", "MTH202 (S-10)", "PHY101 (S-02)", "MTH202 (S-10)", "PHY101", "-"},
                {"11:30", "-", "ENG101 (L-05)", "-", "ENG101", "Sports"}
            }
        );
        
        body.add(timetableCard);
        
        // Wrap in ScrollPane
        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(DashboardTheme.BG_MAIN);
        
        add(scroll, BorderLayout.CENTER);
    }
}