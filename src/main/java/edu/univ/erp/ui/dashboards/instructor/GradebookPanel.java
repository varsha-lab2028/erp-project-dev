package edu.univ.erp.ui.dashboards.instructor;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GradebookPanel extends JPanel {
    public GradebookPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_LIGHT);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Top Controls (Select Section)
        JPanel controls = new DashboardComponents.RoundedPanel(15, Color.WHITE, true);
        controls.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        
        JLabel lbl = new JLabel("Select Section:");
        lbl.setFont(DashboardTheme.FONT_LABEL);
        
        JComboBox<String> sectionCombo = new JComboBox<>(new String[]{"CSE101 - Section A", "CSE101 - Section B", "CSE202 - Section A"});
        sectionCombo.setPreferredSize(new Dimension(200, 30));
        
        JButton loadBtn = DashboardComponents.createPrimaryButton("Load Students");
        
        controls.add(lbl);
        controls.add(sectionCombo);
        controls.add(loadBtn);
        
        add(controls, BorderLayout.NORTH);

        // 2. Grading Table
        // We use our shared TablePanel, but for a real gradebook, you might want editable cells.
        // Here we simulate the look.
        String[] cols = {"Student ID", "Name", "Midsem (30)", "Endsem (50)", "Internal (20)", "Total", "Grade"};
        Object[][] data = {
            {"2024001", "Aarav Patel", "25", "40", "18", "83", "A"},
            {"2024002", "Diya Sharma", "22", "35", "15", "72", "B"},
            {"2024003", "Ishaan Kumar", "28", "45", "19", "92", "A+"},
            {"2024004", "Rohan Singh", "15", "20", "10", "45", "C"},
            {"2024005", "Sanya Malhotra", "24", "38", "17", "79", "B+"}
        };
        
        DashboardComponents.TablePanel gradeTable = new DashboardComponents.TablePanel("Grading Sheet", cols, data);
        add(gradeTable, BorderLayout.CENTER);

        // 3. Bottom Actions
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(DashboardTheme.BG_LIGHT);
        JButton saveBtn = DashboardComponents.createPrimaryButton("Save Grades");
        saveBtn.setBackground(DashboardTheme.ACCENT_ORANGE); // Orange for "Warning/Action"
        bottom.add(saveBtn);
        
        add(bottom, BorderLayout.SOUTH);
    }
}