package edu.univ.erp.ui.admin;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel for admin to create and edit courses (code, title, credits)[cite: 42, 51].
 */
public class CourseManagementPanel extends JPanel {

    private JTable courseTable;
    private JScrollPane tableScrollPane;
    private JPanel formPanel;

    private JTextField courseCodeField;
    private JTextField titleField;
    private JTextField creditsField;
    private JButton addCourseButton;
    private JButton updateCourseButton;

    public CourseManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        initComponents();
    }

    private void initComponents() {
        // 1. Form for adding/editing courses
        formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(new TitledBorder("Manage Course"));

        formPanel.add(new JLabel("Course Code:"));
        courseCodeField = new JTextField(10);
        formPanel.add(courseCodeField);

        formPanel.add(new JLabel("Title:"));
        titleField = new JTextField(30);
        formPanel.add(titleField);

        formPanel.add(new JLabel("Credits:"));
        creditsField = new JTextField(5);
        formPanel.add(creditsField);

        addCourseButton = new JButton("Add Course");
        formPanel.add(addCourseButton);
        
        updateCourseButton = new JButton("Update Selected");
        formPanel.add(updateCourseButton);
        
        // TODO: Add ActionListeners to buttons

        // 2. Table of existing courses
        String[] columnNames = {"Course Code", "Title", "Credits"};
        Object[][] data = {
                {"CS101", "Intro to Programming", 3},
                {"MATH201", "Multivariable Calculus", 4}
        };
        courseTable = new JTable(data, columnNames);
        tableScrollPane = new JScrollPane(courseTable);
        
        // TODO: Add a ListSelectionListener to the table to populate the form fields for editing

        // 3. Add components to the main panel
        add(formPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }
}