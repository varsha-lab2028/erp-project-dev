package edu.univ.erp.ui.admin;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel for admin to create/edit sections and assign instructors.
 */
public class SectionManagementPanel extends JPanel {

    private JTable sectionTable;
    private JScrollPane tableScrollPane;
    private JPanel formPanel;

    private JComboBox<String> courseComboBox; // TODO: Populate from CourseDAO
    private JComboBox<String> instructorComboBox; // TODO: Populate from InstructorDAO
    private JTextField dayTimeField;
    private JTextField roomField;
    private JTextField capacityField;
    private JTextField semesterField;
    private JButton addSectionButton;

    public SectionManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        initComponents();
    }

    private void initComponents() {
        // 1. Form for adding sections
        formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        formPanel.setBorder(new TitledBorder("Add New Section"));

        formPanel.add(new JLabel("Course:"));
        courseComboBox = new JComboBox<>(new String[]{"CS101", "MATH201"}); // Dummy
        formPanel.add(courseComboBox);

        formPanel.add(new JLabel("Instructor:"));
        instructorComboBox = new JComboBox<>(new String[]{"Dr. Ada", "Prof. K."}); // Dummy
        formPanel.add(instructorComboBox);

        formPanel.add(new JLabel("Day/Time:"));
        dayTimeField = new JTextField("MWF 10:00-10:50");
        formPanel.add(dayTimeField);

        formPanel.add(new JLabel("Room:"));
        roomField = new JTextField("A-101");
        formPanel.add(roomField);

        formPanel.add(new JLabel("Capacity:"));
        capacityField = new JTextField("50");
        formPanel.add(capacityField);

        formPanel.add(new JLabel("Semester/Year:"));
        semesterField = new JTextField("Fall 2025");
        formPanel.add(semesterField);
        
        formPanel.add(new JLabel("")); // Spacer
        addSectionButton = new JButton("Add Section");
        formPanel.add(addSectionButton);
        
        // TODO: Add ActionListener to button

        // 2. Table of existing sections
        String[] columnNames = {"Section ID", "Course", "Instructor", "Day/Time", "Room", "Cap.", "Enrolled"};
        Object[][] data = {
                {1, "CS101", "Dr. Ada", "MWF 10:00-10:50", "A-101", 50, 45},
                {2, "MATH201", "Prof. K.", "TTh 1:00-2:20", "B-205", 40, 30}
        };
        sectionTable = new JTable(data, columnNames);
        tableScrollPane = new JScrollPane(sectionTable);

        // 3. Add components to the main panel
        add(formPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }
}