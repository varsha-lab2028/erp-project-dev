package edu.univ.erp.ui.admin;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel for admin to add new users (students, instructors, admins).
 */
public class UserManagementPanel extends JPanel {

    private JTable userTable;
    private JScrollPane tableScrollPane;
    private JPanel formPanel;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton addUserButton;

    public UserManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        initComponents();
    }

    private void initComponents() {
        // 1. Create the form panel for adding new users
        formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(new TitledBorder("Add New User"));

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(20);
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Role:"));
        roleComboBox = new JComboBox<>(new String[]{"Student", "Instructor", "Admin"}); // [cite: 6, 7, 8]
        formPanel.add(roleComboBox);

        formPanel.add(new JLabel("")); // Spacer
        addUserButton = new JButton("Add User");
        formPanel.add(addUserButton);
        
        // TODO: Add ActionListener to addUserButton to call AdminService

        // 2. Create the table to display existing users
        // (Dummy data for now, this will come from the database)
        String[] columnNames = {"User ID", "Username", "Role", "Status"};
        Object[][] data = {
                {"101", "stu1", "Student", "Active"},
                {"201", "inst1", "Instructor", "Active"},
                {"901", "admin1", "Admin", "Active"}
        };
        userTable = new JTable(data, columnNames);
        tableScrollPane = new JScrollPane(userTable);
        
        // 3. Add components to the main panel
        add(formPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
    }
}