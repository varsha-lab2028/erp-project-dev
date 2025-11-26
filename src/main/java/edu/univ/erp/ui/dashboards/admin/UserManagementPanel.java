package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UserManagementPanel extends JPanel {

    public UserManagementPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_MAIN); // Dynamic Background
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // 1. Form Card
        DashboardComponents.CardPanel formCard = new DashboardComponents.CardPanel();
        formCard.setLayout(new BorderLayout());
        // REMOVED: formCard.setBackground(Color.WHITE); -> CardPanel now handles this automatically via Theme

        JLabel title = new JLabel("Add New User");
        title.setFont(DashboardTheme.FONT_SUBTITLE);
        title.setForeground(DashboardTheme.TEXT_PRIMARY); // Fix: Dynamic Text Color
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        formCard.add(title, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(2, 4, 15, 15));
        fields.setOpaque(false);
        
        // Helper for labels
        fields.add(createLabel("Username:"));
        JTextField userTxt = new JTextField();
        DashboardComponents.styleControl(userTxt); // Fix: Style Input
        fields.add(userTxt);
        
        fields.add(createLabel("Password:"));
        JPasswordField passTxt = new JPasswordField();
        DashboardComponents.styleControl(passTxt); // Fix: Style Input
        fields.add(passTxt);
        
        fields.add(createLabel("Role:"));
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Student", "Instructor", "Admin"});
        DashboardComponents.styleControl(roleBox); // Fix: Style Input
        fields.add(roleBox);
        
        fields.add(new JLabel("")); 
        fields.add(DashboardComponents.createPrimaryButton("Create User"));
        
        formCard.add(fields, BorderLayout.CENTER);

        // 2. Table Card
        String[] cols = {"ID", "Username", "Role", "Status"};
        Object[][] data = {
            {"101", "aman.gupta", "Student", "Active"},
            {"202", "suresh.kr", "Instructor", "Active"},
            {"999", "admin", "Admin", "Active"}
        };
        
        DashboardComponents.TablePanel tableCard = new DashboardComponents.TablePanel("All Users", cols, data);

        add(formCard, BorderLayout.NORTH);
        add(tableCard, BorderLayout.CENTER);
    }
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(DashboardTheme.FONT_BOLD);
        lbl.setForeground(DashboardTheme.TEXT_SECONDARY); // Fix: Gray text in both modes
        return lbl;
    }
}