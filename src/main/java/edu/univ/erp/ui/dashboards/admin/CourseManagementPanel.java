package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.service.AdminService;
import edu.univ.erp.domain.Course;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CourseManagementPanel extends JPanel {
    private final AdminService adminService;
    private DashboardComponents.TablePanel tablePanel;

    public CourseManagementPanel(AdminService adminService) {
        this.adminService = adminService;

        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        //Top Card: Course Form
        DashboardComponents.CardPanel formCard = new DashboardComponents.CardPanel();
        formCard.setLayout(new BorderLayout());

        JLabel title = new JLabel("Manage Courses");
        title.setFont(DashboardTheme.FONT_SUBTITLE);
        title.setForeground(DashboardTheme.TEXT_PRIMARY);
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

        JButton addBtn = DashboardComponents.createPrimaryButton("Add Course");
        addBtn.addActionListener(e -> onAddCourse(codeTxt, titleTxt, creditTxt));
        fields.add(addBtn);

        formCard.add(fields, BorderLayout.CENTER);

        //put the Course List Table in the center
        String[] columnNames = {"Course Code", "Course Name", "Credits"};  // match DB
        Object[][] data = loadCourseData();

        tablePanel = new DashboardComponents.TablePanel("Existing Courses", columnNames, data);

        // Add buttons for Edit and Delete
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);

        JButton editBtn = DashboardComponents.createSecondaryButton("Edit Selected");
        editBtn.addActionListener(e -> onEditSelectedCourse());
        buttonPanel.add(editBtn);

        JButton deleteBtn = DashboardComponents.createDangerButton("Delete Selected");
        deleteBtn.addActionListener(e -> onDeleteSelectedCourse());
        buttonPanel.add(deleteBtn);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(formCard, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(DashboardTheme.FONT_BOLD);
        lbl.setForeground(DashboardTheme.TEXT_SECONDARY);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        return lbl;
    }

    //helper methods
    private Object[][] loadCourseData() {
        try {
            List<Course> courses = adminService.getAllCourses();
            Object[][] rows = new Object[courses.size()][3];
            for (int i = 0; i < courses.size(); i++) {
                Course c = courses.get(i);
                rows[i][0] = c.getCourseCode();
                rows[i][1] = c.getName();
                rows[i][2] = c.getCredits();
            }
            return rows;
        } catch (RuntimeException e){
            // Not logged in yet → show empty table, no popup
            if (e.getMessage() != null &&
                    e.getMessage().toLowerCase().contains("not logged in")) {
                return new Object[0][3];
            }
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load courses: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return new Object[0][3];
        } catch (Exception e) {
            // if something fails, show error and return empty table
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load courses: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return new Object[0][3];
        }
    }

    // Called when Add button is clicked
    private void onAddCourse(JTextField codeTxt, JTextField titleTxt, JTextField creditTxt) {
        String code = codeTxt.getText().trim();
        String title = titleTxt.getText().trim();
        String creditsStr = creditTxt.getText().trim();

        if (code.isEmpty() || title.isEmpty() || creditsStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        int credits;
        try {
            credits = Integer.parseInt(creditsStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credits must be an integer.");
            return;
        }

        try {
            adminService.createCourse(code, title, credits);

            // Clear inputs
            codeTxt.setText("");
            titleTxt.setText("");
            creditTxt.setText("");

            // Refresh table
            refreshTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add course: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void onEditSelectedCourse() {
        // Get selected row from table
        JTable table = getTableFromTablePanel(tablePanel);
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to edit.");
            return;
        }

        String courseCode = (String) table.getValueAt(selectedRow, 0);
        String currentName = (String) table.getValueAt(selectedRow, 1);
        int currentCredits = (Integer) table.getValueAt(selectedRow, 2);

        // Create dialog for editing
        JTextField nameField = new JTextField(currentName);
        JTextField creditsField = new JTextField(String.valueOf(currentCredits));
        DashboardComponents.styleControl(nameField);
        DashboardComponents.styleControl(creditsField);

        Object[] message = {
            "Course Code: " + courseCode,
            "New Name:", nameField,
            "New Credits:", creditsField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Course", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String creditsStr = creditsField.getText().trim();

            if (newName.isEmpty() || creditsStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            int newCredits;
            try {
                newCredits = Integer.parseInt(creditsStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credits must be an integer.");
                return;
            }

            try {
                adminService.editCourse(courseCode, newName, newCredits);
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to edit course: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onDeleteSelectedCourse() {
        // Get selected row from table
        JTable table = getTableFromTablePanel(tablePanel);
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to delete.");
            return;
        }

        String courseCode = (String) table.getValueAt(selectedRow, 0);
        String courseName = (String) table.getValueAt(selectedRow, 1);

        int option = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete course '" + courseCode + " - " + courseName + "'?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            try {
                adminService.deleteCourse(courseCode);
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to delete course: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshTable() {
        // Remove old table panel from center panel
        JPanel centerPanel = (JPanel) getComponent(1); // Assuming centerPanel is the second component
        centerPanel.remove(tablePanel);

        // Create new table panel
        Object[][] data = loadCourseData();
        String[] cols = {"Course Code", "Course Name", "Credits"};
        tablePanel = new DashboardComponents.TablePanel("Existing Courses", cols, data);

        // Add back to center panel
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JTable getTableFromTablePanel(DashboardComponents.TablePanel tablePanel) {
        // Access the JScrollPane and then the JTable
        JScrollPane scrollPane = (JScrollPane) tablePanel.getComponent(1); // Assuming table is second component
        return (JTable) scrollPane.getViewport().getView();
    }
}
