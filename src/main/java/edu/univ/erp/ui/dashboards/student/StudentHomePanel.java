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
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(DashboardTheme.BG_MAIN);

        // 1. Stats Grid
        JPanel grid = new JPanel(new GridLayout(1, 4, 20, 0));
        grid.setBackground(DashboardTheme.BG_MAIN);
        grid.setMaximumSize(new Dimension(2000, 110));
        
        grid.add(new DashboardComponents.StatsCard("Current CGPA", "8.92", DashboardTheme.PRIMARY));
        grid.add(new DashboardComponents.StatsCard("Credits Earned", "64", DashboardTheme.ACCENT));
        grid.add(new DashboardComponents.StatsCard("Attendance", "92%", DashboardTheme.SUCCESS));
        grid.add(new DashboardComponents.StatsCard("Active Courses", "5", new Color(23, 162, 184)));
        
        body.add(grid);
        body.add(Box.createVerticalStrut(30));

        // 2. Upcoming Classes (Using TablePanel)
        String[] cols = {"Time", "Course", "Classroom", "Type"};
        Object[][] data = {
            {"10:00 AM", "CSE101: Intro to Programming", "C-01", "Lecture"},
            {"01:00 PM", "MTH201: Calculus", "S-10", "Tutorial"},
            {"03:00 PM", "DES301: Design Thinking", "Lab-2", "Lab"}
        };
        
        body.add(new DashboardComponents.TablePanel("Today's Schedule", cols, data));

        add(new JScrollPane(body) {
             { setBorder(null); getViewport().setBackground(DashboardTheme.BG_MAIN); }
        }, BorderLayout.CENTER);
    }
}