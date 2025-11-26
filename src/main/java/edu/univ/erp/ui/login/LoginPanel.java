package edu.univ.erp.ui.login;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.domain.AuthClass;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPanel extends JPanel {
    private final LoginController controller;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel statusLabel;
    
    private static final Color PAGE_BG = DashboardTheme.PRIMARY_DARK; // #003B36
    private String selectedRole = "STUDENT";
    private JPanel[] roleButtons;

    public LoginPanel(LoginController controller) {
        this.controller = controller;

        // 1. Full Page Background: IIITD Dark Green
        setLayout(new GridBagLayout());
        setBackground(PAGE_BG);

        // 2. Login Card (White, Size Increased)
        JPanel card = new DashboardComponents.RoundedPanel(14, Color.WHITE, true); 
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(500, 650));
        card.setBorder(new EmptyBorder(32, 32, 20, 32));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 0, 8, 0); 

        // --- 1. LOGO ---
        JLabel logo = new JLabel("IIITD ERP", SwingConstants.CENTER); 
        logo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        logo.setForeground(DashboardTheme.TEXT_PRIMARY);
        gbc.insets = new Insets(0, 0, 16, 0);
        card.add(logo, gbc);

        // --- 2. WELCOME TEXT ---
        JLabel title = new JLabel("Welcome Back!", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(DashboardTheme.TEXT_PRIMARY);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        card.add(title, gbc);

        JLabel subtitle = new JLabel("Select Your Profile Type", SwingConstants.CENTER);
        subtitle.setFont(DashboardTheme.FONT_REGULAR);
        subtitle.setForeground(DashboardTheme.TEXT_SECONDARY);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 24, 0);
        card.add(subtitle, gbc);
        
        // --- 3. PROFILE TYPE SELECTION (Fixed Icons) ---
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 30, 0);
        card.add(createProfileSelector(), gbc);

        // --- 4. INPUT FIELDS ---
        gbc.gridy++;
        card.add(createLabelFieldPair("Username:", usernameField = createStyledInputField(false)), gbc);

        gbc.gridy++;
        card.add(createLabelFieldPair("Password:", passwordField = createStyledPasswordField()), gbc);

        // --- 5. LOGIN BUTTON & FORGOT LINK ---
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 15, 0);
        JButton loginBtn = new JButton("LOGIN");
        styleLoginButton(loginBtn);
        loginBtn.addActionListener(e -> performLogin());
        card.add(loginBtn, gbc);

        // Forgot Password Link (Bottom right)
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false);

        statusLabel = new JLabel(" ", SwingConstants.LEFT);
        statusLabel.setFont(DashboardTheme.FONT_SMALL);
        statusLabel.setForeground(DashboardTheme.TEXT_SECONDARY);
        
        JLabel forgotLink = new JLabel("Forgot login details?", SwingConstants.RIGHT);
        forgotLink.setForeground(DashboardTheme.PRIMARY_DARK);
        forgotLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotLink.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { JOptionPane.showMessageDialog(card, "Contact the ERP Admin to recover your details."); }
        });
        
        bottomRow.add(statusLabel, BorderLayout.WEST);
        bottomRow.add(forgotLink, BorderLayout.EAST);
        card.add(bottomRow, gbc);

        add(card, new GridBagConstraints());
        
        // Initial selection
        SwingUtilities.invokeLater(() -> selectRole("STUDENT")); 
    }

    // --- UI FACTORIES ---
    
    private JPanel createLabelFieldPair(String labelText, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(DashboardTheme.FONT_LABEL);
        lbl.setForeground(DashboardTheme.TEXT_PRIMARY);
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        p.setBorder(new EmptyBorder(10, 0, 10, 0));
        return p;
    }

    private JTextField createStyledInputField(boolean isPassword) {
        JTextField field = isPassword ? new JPasswordField() : new JTextField();
        field.setFont(DashboardTheme.FONT_REGULAR.deriveFont(Font.BOLD, 15f));
        field.setForeground(Color.WHITE);
        field.setBackground(PAGE_BG); // Green Background
        field.setCaretColor(DashboardTheme.SECONDARY_GREEN);
        field.setPreferredSize(new Dimension(0, 45));
        
        // Black 2px Border
        field.setBorder(new CompoundBorder(
            new LineBorder(Color.BLACK, 2, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        // FIX: Casting is unnecessary and unsafe. The helper already returns the correct JPasswordField object.
        return (JPasswordField) createStyledInputField(true);
    }
    
    private void styleLoginButton(JButton btn) {
        btn.setBackground(DashboardTheme.SECONDARY_GREEN);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(0, 45));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel createProfileSelector() {
        JPanel selector = new JPanel(new GridLayout(1, 3, 10, 0));
        selector.setOpaque(false);
        
        roleButtons = new JPanel[3];
        
        roleButtons[0] = createRoleButton("Student", "S", "STUDENT");
        roleButtons[1] = createRoleButton("Instructor", "I", "INSTRUCTOR");
        roleButtons[2] = createRoleButton("Admin", "A", "ADMIN");
        
        for (JPanel btn : roleButtons) {
            selector.add(btn);
        }
        return selector;
    }

    private JPanel createRoleButton(String name, String icon, String role) {
        RoundPanel iconPanel = new RoundPanel();
        iconPanel.setLayout(new GridBagLayout());
        
        JLabel iconLbl = new JLabel(icon, SwingConstants.CENTER);
        iconLbl.setFont(new Font("Segoe UI", Font.BOLD, 40)); 
        iconPanel.add(iconLbl);

        JLabel nameLbl = new JLabel(name, SwingConstants.CENTER);
        nameLbl.setFont(DashboardTheme.FONT_SMALL);
        nameLbl.setForeground(DashboardTheme.TEXT_PRIMARY);
        nameLbl.setBorder(new EmptyBorder(5, 0, 0, 0));

        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.add(iconPanel, BorderLayout.NORTH);
        p.add(nameLbl, BorderLayout.CENTER);
        
        // Click Listener
        p.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { selectRole(role); }
            @Override public void mouseEntered(MouseEvent e) { if (!role.equals(selectedRole)) iconPanel.setHover(true); }
            @Override public void mouseExited(MouseEvent e) { if (!role.equals(selectedRole)) iconPanel.setHover(false); }
        });
        
        p.putClientProperty("iconPanel", iconPanel);
        p.putClientProperty("role", role);
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return p;
    }
    
    private void selectRole(String role) {
        this.selectedRole = role;
        // Update styling for all buttons
        for (JPanel btn : roleButtons) {
            String btnRole = (String) btn.getClientProperty("role");
            RoundPanel iconPanel = (RoundPanel) btn.getClientProperty("iconPanel");
            
            if (iconPanel != null) {
                 iconPanel.setSelected(btnRole.equals(role));
            }
        }
        usernameField.requestFocusInWindow();
    }
    
    // --- FIX 1: Missing Helper class definition added here ---
    private class RoundPanel extends JPanel {
        private boolean isSelected = false;
        private boolean isHover = false;
        private final int size = 80;
        private final int borderWidth = 3;

        public RoundPanel() { 
            setOpaque(false);
            setPreferredSize(new Dimension(90, 90));
        }

        public void setSelected(boolean selected) { isSelected = selected; repaint(); }
        public void setHover(boolean hover) { isHover = hover; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color borderColor = isSelected ? DashboardTheme.SECONDARY_GREEN : DashboardTheme.PRIMARY_DARK;
            Color fillColor = isSelected ? DashboardTheme.PRIMARY_DARK : (isHover ? new Color(240, 240, 240) : Color.WHITE);
            
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            // Border
            g2.setColor(borderColor);
            g2.fillOval(x, y, size, size);
            
            // Inner Fill
            g2.setColor(fillColor);
            g2.fillOval(x + borderWidth, y + borderWidth, size - 2 * borderWidth, size - 2 * borderWidth);
            
            // Update icon color based on selection
            Component[] comps = getComponents();
            if (comps.length > 0 && comps[0] instanceof JLabel) {
                ((JLabel)comps[0]).setForeground(isSelected ? Color.WHITE : DashboardTheme.PRIMARY_DARK);
            }
            
            super.paintComponent(g2);
            g2.dispose();
        }
    }


    // --- LOGIN LOGIC ---
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            setStatus("Please enter both username and password.", true);
            return;
        }

        setStatus("Authenticating...", false);
        // Changed from boolean to AuthClass response
        AuthClass authUser = controller.authenticate(username, password, selectedRole);

        if (authUser != null) {
            setStatus("Login successful! Redirecting...", false);
            // Notify login success with role
            firePropertyChange("loginSuccess", null, authUser.role);
        } else {
            setStatus("Invalid credentials for " + selectedRole + ".", true);
        }
    }
    
    private void setStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? Color.RED : DashboardTheme.TEXT_SECONDARY);
    }
}//new commit