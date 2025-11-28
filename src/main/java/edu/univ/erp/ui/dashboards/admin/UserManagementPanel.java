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
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        
        //form card
        DashboardComponents.CardPanel formCard = new DashboardComponents.CardPanel();
        formCard.setLayout(new BorderLayout());

        JLabel title = new JLabel("Add New User");
        title.setFont(DashboardTheme.FONT_SUBTITLE);
        title.setForeground(DashboardTheme.TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        formCard.add(title, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(2, 4, 15, 15));
        fields.setOpaque(false);
        
       

        fields.add(createLabel("Username:"));
        userTxt = new JTextField();
        DashboardComponents.styleControl(userTxt);
        fields.add(userTxt);
        
        fields.add(createLabel("Password:"));
        passTxt = new JPasswordField();
        DashboardComponents.styleControl(passTxt);
        fields.add(passTxt);
        
        fields.add(createLabel("Role:"));
        roleBox = new JComboBox<>(new String[]{"Student", "Instructor", "Admin"});
        DashboardComponents.styleControl(roleBox);
        fields.add(roleBox);
        
        fields.add(new JLabel("")); 
        
        
        fields.add(new JLabel("")); // Spacer

        JButton createBtn = DashboardComponents.createPrimaryButton("Create User");
        createBtn.addActionListener(e -> onCreateUser());
        fields.add(createBtn);
        
        formCard.add(fields, BorderLayout.CENTER);

        
        String[] cols = {"ID", "Username", "Role", "Status"};
        Object[][] data = new Object[0][4]; 
        
        
        String[] cols = {"ID", "Username", "Role", "Status"};
        Object[][] data = new Object[0][4];

        this.tablePanel = new DashboardComponents.TablePanel("All Users", cols, data);

        add(formCard, BorderLayout.NORTH);
        add(this.tablePanel, BorderLayout.CENTER);
        
      

        loadUsersTable();
    }
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(DashboardTheme.FONT_BOLD);
        lbl.setForeground(DashboardTheme.TEXT_SECONDARY);
        return lbl;
    }

    private void onCreateUser() {
        String username = userTxt.getText().trim();
        String password = new String(passTxt.getPassword());
        String roleLabel = ((String) roleBox.getSelectedItem()).toUpperCase(); 

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Password required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

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

            loadUsersTable();   

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Input", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to create user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadUsersTable() {
        try {
            List<AuthClass> users = adminService.listAuthUsers();

            
            if (tablePanel.getComponentCount() > 1 && tablePanel.getComponent(1) instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) tablePanel.getComponent(1);
                JTable table = (JTable) scroll.getViewport().getView();
                DefaultTableModel model = (DefaultTableModel) table.getModel();

                model.setRowCount(0); 
                model.setRowCount(0);
                for (AuthClass u : users) {
                    model.addRow(new Object[]{
                            u.user_id,
                            u.username,
                            u.role,
                            u.auth_status
                    });
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}