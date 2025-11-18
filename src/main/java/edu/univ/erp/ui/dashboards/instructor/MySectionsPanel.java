package edu.univ.erp.ui.dashboards.instructor;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MySectionsPanel extends JPanel {
    public MySectionsPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_LIGHT);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Reuse the TablePanel for a clean look
        String[] cols = {"Course Code", "Title", "Section", "Room", "Schedule", "Capacity", "Enrolled"};
        Object[][] data = {
            {"CSE101", "Intro to Programming", "A", "C-01", "Mon/Wed 10:00", "50", "48"},
            {"CSE101", "Intro to Programming", "B", "C-02", "Tue/Thu 11:30", "50", "45"},
            {"CSE202", "Data Structures", "A", "S-10", "Wed/Fri 09:00", "60", "60"},
            {"DES301", "Design Thinking", "C", "Lab-1", "Fri 14:00", "30", "28"}
        };

        DashboardComponents.TablePanel table = new DashboardComponents.TablePanel("All Assigned Sections", cols, data);
        add(table, BorderLayout.CENTER);
    }
}