package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CourseManagementPanel extends JPanel {

    public CourseManagementPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // 1. Top Card: Course Form
        DashboardComponents.CardPanel formCard = new DashboardComponents.CardPanel();
        formCard.setLayout(new BorderLayout());

        JLabel title = new JLabel("Manage Courses");
        title.setFont(DashboardTheme.FONT_SUBTITLE);
        title.setForeground(DashboardTheme.TEXT_PRIMARY); // Fix: White in Dark Mode
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        formCard.add(title, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(1, 7, 10, 0));
        fields.setOpaque(false);
        
        fields.add(createLabel("Code:"));
        JTextField codeTxt = new JTextField();
        DashboardComponents.styleControl(codeTxt); // Fix
        fields.add(codeTxt);
        
        fields.add(createLabel("Title:"));
        JTextField titleTxt = new JTextField();
        DashboardComponents.styleControl(titleTxt); // Fix
        fields.add(titleTxt);
        
        fields.add(createLabel("Credits:"));
        JTextField creditTxt = new JTextField();
        DashboardComponents.styleControl(creditTxt); // Fix
        fields.add(creditTxt);

        fields.add(DashboardComponents.createPrimaryButton("Add Course"));

        formCard.add(fields, BorderLayout.CENTER);

        // 2. Center: Course List Table
        String[] columnNames = {"Course Code", "Title", "Credits", "Department"};
        Object[][] data = {
                {"CS101", "Intro to Programming", "3", "CSE"},
                {"MATH201", "Multivariable Calculus", "4", "Math"},
                {"PHY101", "Mechanics", "4", "Physics"}
        };
        
        DashboardComponents.TablePanel tablePanel = new DashboardComponents.TablePanel("Existing Courses", columnNames, data);

        add(formCard, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(DashboardTheme.FONT_BOLD);
        lbl.setForeground(DashboardTheme.TEXT_SECONDARY);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        return lbl;
    }
}