package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UserManagementPanel extends JPanel {

    public UserManagementPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_LIGHT);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Form Card
        JPanel formCard = new DashboardComponents.RoundedPanel(15, Color.WHITE, true);
        formCard.setLayout(new BorderLayout());
        formCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("Add New User");
        title.setFont(DashboardTheme.FONT_LABEL);
        formCard.add(title, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(2, 4, 10, 10));
        fields.setOpaque(false);
        fields.add(new JLabel("Username:"));
        fields.add(new JTextField());
        fields.add(new JLabel("Password:"));
        fields.add(new JPasswordField());
        fields.add(new JLabel("Role:"));
        fields.add(new JComboBox<>(new String[]{"Student", "Instructor", "Admin"}));
        fields.add(new JLabel("")); 
        fields.add(DashboardComponents.createPrimaryButton("Create User"));
        
        formCard.add(fields, BorderLayout.CENTER);

        // 2. Table Card
        JPanel tableCard = new DashboardComponents.RoundedPanel(15, Color.WHITE, true);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        String[] cols = {"ID", "Username", "Role", "Status"};
        Object[][] data = {
            {"101", "aman.gupta", "Student", "Active"},
            {"202", "suresh.kr", "Instructor", "Active"},
            {"999", "admin", "Admin", "Active"}
        };
        JTable table = new JTable(data, cols);
        table.setRowHeight(30);
        tableCard.add(new JScrollPane(table), BorderLayout.CENTER);

        add(formCard, BorderLayout.NORTH);
        add(tableCard, BorderLayout.CENTER);
    }
}