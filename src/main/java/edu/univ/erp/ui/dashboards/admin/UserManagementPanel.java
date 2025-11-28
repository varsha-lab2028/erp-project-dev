package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.domain.AuthClass;
import edu.univ.erp.service.AdminService;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementPanel extends JPanel {

    private final AdminService adminService = new AdminService();

    private JTextField userTxt;
    private JPasswordField passTxt;
    private JComboBox<String> roleBox;
    private DashboardComponents.TablePanel tablePanel;

    public UserManagementPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_MAIN); // Dynamic Background
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // 1. Form Card
        DashboardComponents.CardPanel formCard = new DashboardComponents.CardPanel();
        formCard.setLayout(new BorderLayout());

        JLabel title = new JLabel("Add New User");
        title.setFont(DashboardTheme.FONT_SUBTITLE);
        title.setForeground(DashboardTheme.TEXT_PRIMARY);
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
        Object[][] data = new Object[0][4];   // start empty, load from DB
        
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

    private void onCreateUser() {
        String username = userTxt.getText().trim();
        String password = new String(passTxt.getPassword());
        String roleLabel = ((String) roleBox.getSelectedItem()).toUpperCase(); // STUDENT / INSTRUCTOR / ADMIN

        try {
            adminService.createAuthUser(username, password, roleLabel);

            JOptionPane.showMessageDialog(
                    this,
                    "User created successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            userTxt.setText("");
            passTxt.setText("");

            loadUsersTable();   // refresh table from DB

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to create user: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // --- TABLE LOAD ---------------------------------------------------------
    private void loadUsersTable() {
        try {
            List<AuthClass> users = adminService.listAuthUsers();

            // Get the JTable inside TablePanel (same trick you used elsewhere)
            JScrollPane scroll = (JScrollPane) tablePanel.getComponent(1);
            JTable table = (JTable) scroll.getViewport().getView();
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            model.setRowCount(0);
            for (AuthClass u : users) {
                model.addRow(new Object[]{
                        u.user_id,
                        u.username,
                        u.role,
                        u.auth_status
                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load users: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}