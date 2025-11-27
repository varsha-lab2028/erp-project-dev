package edu.univ.erp.ui.login;

import javax.swing.*;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.auth.session.Session;
import edu.univ.erp.domain.AuthClass;
import edu.univ.erp.domain.Role;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPanel extends JPanel {
    private final MainFrameController controller;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel statusLabel;
    private String selectedRole = "STUDENT"; // Default role

    public LoginPanel(MainFrameController controller) {
        this.controller = controller;
        setLayout(new GridBagLayout());
        setBackground(DashboardTheme.BG_MAIN);

        // 2. Login Card (White, Size Increased)
        JPanel card = new DashboardComponents.CardPanel();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(500, 650));
        card.setBorder(new EmptyBorder(32, 32, 20, 32));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
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
        card.add(createProfileSelector(), gbc); // Assuming this method exists and works

        // --- 4. INPUT FIELDS ---
        gbc.gridy++;
        usernameField = createStyledInputField(false);
        card.add(createLabelFieldPair("Username:", usernameField), gbc);

        gbc.gridy++;
        passwordField = createStyledPasswordField();
        card.add(createLabelFieldPair("Password:", passwordField), gbc);

        // --- 5. LOGIN BUTTON & FORGOT LINK ---
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 15, 0);
        JButton loginBtn = new JButton("LOGIN");
        styleLoginButton(loginBtn);
        loginBtn.addActionListener(e -> performLogin(controller));
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
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(card, "Contact the ERP Admin to recover your details.");
            }
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
        lbl.setFont(DashboardTheme.FONT_REGULAR);
        lbl.setForeground(DashboardTheme.TEXT_PRIMARY);
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        p.setBorder(new EmptyBorder(10, 0, 10, 0));
        return p;
    }

    private JTextField createStyledInputField(boolean isPassword) { // Assuming this is what was intended
        JTextField field = new JTextField();
        // Make absolutely sure it can receive input
        field.setEditable(true);
        field.setEnabled(true);
        field.setFocusable(true);

        field.setFont(DashboardTheme.FONT_REGULAR.deriveFont(Font.BOLD, 15f));
        field.setForeground(DashboardTheme.TEXT_PRIMARY);
        field.setBackground(DashboardTheme.BG_MAIN);
        field.setCaretColor(DashboardTheme.TEXT_PRIMARY);
        field.setPreferredSize(new Dimension(0, 45));

        // Black 2px Border
        field.setBorder(new CompoundBorder(
                new LineBorder(DashboardTheme.BORDER_COLOR, 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setEditable(true);
        field.setEnabled(true);
        field.setFocusable(true);
        field.setFont(DashboardTheme.FONT_REGULAR.deriveFont(Font.BOLD, 15f));
        field.setForeground(DashboardTheme.TEXT_PRIMARY);
        field.setBackground(DashboardTheme.BG_MAIN);
        field.setCaretColor(DashboardTheme.TEXT_PRIMARY);
        field.setPreferredSize(new Dimension(0, 45));
        field.setBorder(new CompoundBorder(
                new LineBorder(DashboardTheme.BORDER_COLOR, 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));
        return field;
    }

    private void styleLoginButton(JButton button) {
        button.setFont(DashboardTheme.FONT_BOLD.deriveFont(16f));
        button.setBackground(DashboardTheme.PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(15, 0, 15, 0));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Placeholder for methods that were missing but referenced
    private JPanel createProfileSelector() {
        // This should contain the logic for student/admin/instructor role selection
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(new JLabel("Profile selector placeholder"));
        return panel;
    }

    private void selectRole(String role) {
        this.selectedRole = role;
        // This should update the UI to show which role is selected
    }

    // Interface for callback
    public interface MainFrameController {
        void loginSuccess(String role);
        // This was added to make the login logic work
        AuthClass authenticate(String username, String password, String selectedRole);
    }

    //logic for performing login
    private void performLogin(MainFrameController controller) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            setStatus("Please enter both username and password.", true);
            return;
        }

        setStatus("Authenticating...", false);
        AuthClass authUser = this.controller.authenticate(username, password, selectedRole);

        if (authUser != null) {
            setStatus("Login successful! Redirecting...", false);

            Role roleEnum;
            try {
                roleEnum = Role.valueOf(authUser.role.toUpperCase());
            } catch (IllegalArgumentException ex) {
                setStatus("Login succeeded but role is invalid: " + authUser.role, true);
                return;
            }
            Session.login(authUser.user_id, roleEnum);

            // Notify MainApp to switch dashboard
            this.controller.loginSuccess(authUser.role);
        } else {
            setStatus("Invalid credentials for " + selectedRole + ".", true);
        }
    }

    private void setStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? Color.RED : DashboardTheme.TEXT_SECONDARY);
    }
}