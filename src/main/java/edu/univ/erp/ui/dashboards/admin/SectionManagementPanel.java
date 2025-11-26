package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SectionManagementPanel extends JPanel {
    public SectionManagementPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // 1. Form Card
        DashboardComponents.CardPanel formCard = new DashboardComponents.CardPanel();
        formCard.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("Manage Sections");
        title.setFont(DashboardTheme.FONT_SUBTITLE);
        title.setForeground(DashboardTheme.TEXT_PRIMARY); // Fix
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        formCard.add(title, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(2, 4, 15, 15));
        fields.setOpaque(false);
        
        fields.add(createLabel("Course Code:"));
        JTextField cCode = new JTextField();
        DashboardComponents.styleControl(cCode);
        fields.add(cCode);
        
        fields.add(createLabel("Instructor:"));
        JComboBox<String> instrBox = new JComboBox<>(new String[]{"Dr. Suresh", "Prof. Gupta"});
        DashboardComponents.styleControl(instrBox);
        fields.add(instrBox);
        
        fields.add(createLabel("Room / Time:"));
        JTextField roomTxt = new JTextField("e.g. C-01, Mon 10AM");
        DashboardComponents.styleControl(roomTxt);
        fields.add(roomTxt);
        
        fields.add(new JLabel("")); // Spacer
        fields.add(DashboardComponents.createPrimaryButton("Add Section"));
        
        formCard.add(fields, BorderLayout.CENTER);

        // 2. Table Card
        String[] cols = {"ID", "Course", "Instructor", "Room", "Time", "Enrolled"};
        Object[][] data = {
            {"101-A", "CSE101", "Dr. Suresh", "C-01", "Mon 10:00", "45/50"},
            {"101-B", "CSE101", "Prof. Gupta", "C-02", "Tue 11:30", "30/50"},
            {"201-A", "MTH201", "Dr. Rao", "S-10", "Wed 09:00", "60/60"}
        };
        
        DashboardComponents.TablePanel table = new DashboardComponents.TablePanel("Active Sections", cols, data);
        
        add(formCard, BorderLayout.NORTH);
        add(table, BorderLayout.CENTER);
    }
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(DashboardTheme.FONT_BOLD);
        lbl.setForeground(DashboardTheme.TEXT_SECONDARY);
        return lbl;
    }
}