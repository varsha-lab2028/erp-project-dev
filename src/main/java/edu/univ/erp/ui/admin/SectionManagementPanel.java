package edu.univ.erp.ui.admin;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SectionManagementPanel extends JPanel {
    public SectionManagementPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_LIGHT);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Form Card (Top)
        JPanel formCard = new DashboardComponents.RoundedPanel(15, Color.WHITE, true);
        formCard.setLayout(new BorderLayout());
        formCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("Manage Sections");
        title.setFont(DashboardTheme.FONT_LABEL);
        formCard.add(title, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(2, 4, 15, 15));
        fields.setOpaque(false);
        fields.add(new JLabel("Course Code:"));
        fields.add(new JTextField());
        fields.add(new JLabel("Instructor:"));
        fields.add(new JComboBox<>(new String[]{"Dr. Suresh", "Prof. Gupta"}));
        fields.add(new JLabel("Room / Time:"));
        fields.add(new JTextField("e.g. C-01, Mon 10AM"));
        fields.add(new JLabel("")); // Spacer
        fields.add(DashboardComponents.createPrimaryButton("Add Section"));
        
        formCard.add(fields, BorderLayout.CENTER);

        // 2. Table Card (Center)
        JPanel tableCard = new DashboardComponents.RoundedPanel(15, Color.WHITE, true);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        String[] cols = {"ID", "Course", "Instructor", "Room", "Time", "Enrolled"};
        Object[][] data = {
            {"101-A", "CSE101", "Dr. Suresh", "C-01", "Mon 10:00", "45/50"},
            {"101-B", "CSE101", "Prof. Gupta", "C-02", "Tue 11:30", "30/50"},
            {"201-A", "MTH201", "Dr. Rao", "S-10", "Wed 09:00", "60/60"}
        };
        // Using the shared TablePanel helper would be even cleaner, but raw table works too:
        JTable table = new JTable(data, cols);
        table.setRowHeight(30);
        table.setShowVerticalLines(false);
        table.setGridColor(DashboardTheme.BG_LIGHT);
        tableCard.add(new JScrollPane(table), BorderLayout.CENTER);

        add(formCard, BorderLayout.NORTH);
        add(tableCard, BorderLayout.CENTER);
    }
}