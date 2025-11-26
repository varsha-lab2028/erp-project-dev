package edu.univ.erp.ui.dashboards.instructor;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GradebookPanel extends JPanel {
    private final edu.univ.erp.service.InstructorService instructorService;

    public GradebookPanel(edu.univ.erp.service.InstructorService instructorService) {
        this.instructorService = instructorService;
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // 1. Top Controls (Using CardPanel for style)
        DashboardComponents.CardPanel controls = new DashboardComponents.CardPanel();
        controls.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        
        JLabel lbl = new JLabel("Select Section:");
        lbl.setFont(DashboardTheme.FONT_BOLD);
        lbl.setForeground(DashboardTheme.TEXT_PRIMARY);
        
        JComboBox<String> sectionCombo = new JComboBox<>(new String[]{"CSE101 - Section A", "CSE101 - Section B", "CSE202 - Section A"});
        sectionCombo.setPreferredSize(new Dimension(250, 35));
        DashboardComponents.styleControl(sectionCombo); // <--- Styles for Dark Mode
        
        JButton loadBtn = DashboardComponents.createPrimaryButton("Load Data");
        
        controls.add(lbl);
        controls.add(sectionCombo);
        controls.add(loadBtn);
        
        add(controls, BorderLayout.NORTH);

        // 2. Grading Table
        String[] cols = {"Student ID", "Name", "Midsem (30)", "Endsem (50)", "Internal (20)", "Total", "Grade"};
        Object[][] data = {
            {"2024001", "Aarav Patel", "25", "40", "18", "83", "A"},
            {"2024002", "Diya Sharma", "22", "35", "15", "72", "B"},
            {"2024003", "Ishaan Kumar", "28", "45", "19", "92", "A+"},
            {"2024004", "Rohan Singh", "15", "20", "10", "45", "C"},
            {"2024005", "Sanya Malhotra", "24", "38", "17", "79", "B+"}
        };
        
        DashboardComponents.TablePanel gradeTable = new DashboardComponents.TablePanel("Student Grades", cols, data);
        add(gradeTable, BorderLayout.CENTER);

        // 3. Bottom Actions
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(DashboardTheme.BG_MAIN);
        
        JButton exportBtn = new JButton("Export CSV"); 
        exportBtn.setFont(DashboardTheme.FONT_BOLD);
        exportBtn.setForeground(DashboardTheme.TEXT_PRIMARY);
        exportBtn.setBackground(DashboardTheme.SURFACE);
        exportBtn.setBorder(BorderFactory.createLineBorder(DashboardTheme.BORDER_COLOR));
        exportBtn.setPreferredSize(new Dimension(120, 40));
        exportBtn.setFocusPainted(false);
        
        JButton saveBtn = DashboardComponents.createPrimaryButton("Publish Grades");
        saveBtn.setBackground(DashboardTheme.SUCCESS); 
        
        bottom.add(exportBtn);
        bottom.add(Box.createHorizontalStrut(10));
        bottom.add(saveBtn);
        
        add(bottom, BorderLayout.SOUTH);
    }
}