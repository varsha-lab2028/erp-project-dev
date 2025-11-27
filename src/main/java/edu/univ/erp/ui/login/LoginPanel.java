package edu.univ.erp.ui.login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.util.PasswordUtils;
import edu.univ.erp.auth.session.Session;
import edu.univ.erp.domain.AuthClass;
import edu.univ.erp.domain.Role;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
//import javax.swing.border.MatteBorder;
import java.awt.*;
//import java.awt.event.FocusAdapter;
//import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPanel extends JPanel {
    private final Runnable onLoginSuccess;
    private final JTextField userField;
    private final JPasswordField passField;
    private final JLabel errorLabel;

    public LoginPanel(MainFrameController controller) {
        this.onLoginSuccess = null; // Unused in this pattern, controller handles it
        setLayout(new GridBagLayout());
        setBackground(DashboardTheme.BG_MAIN);

        // --- Login Card ---
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(40, 40, 40, 40)
        // 2. Login Card (White, Size Increased)
        JPanel card = new DashboardComponents.CardPanel();
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
        lbl.setFont(DashboardTheme.FONT_REGULAR);
        lbl.setForeground(DashboardTheme.TEXT_PRIMARY);
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        p.setBorder(new EmptyBorder(10, 0, 10, 0));
        return p;
    }

    private JTextField createStyledInputField(boolean isPassword) {
        JTextField field = isPassword ? new JPasswordField() : new JTextField();

        // Make absolutely sure it can receive input
        field.setEditable(true);
        field.setEnabled(true);
        field.setFocusable(true);

        field.setFont(DashboardTheme.FONT_REGULAR.deriveFont(Font.BOLD, 15f));
        field.setForeground(Color.WHITE);
        field.setBackground(PAGE_BG); // Green Background
        field.setCaretColor(DashboardTheme.TEXT_PRIMARY); //MAKING THE CURSOR WHITE
        field.setPreferredSize(new Dimension(0, 45));
        
        // Black 2px Border
        field.setBorder(new CompoundBorder(
            new LineBorder(Color.BLACK, 2, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Logo / Icon
        JLabel logo = new JLabel("🎓");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel title = new JLabel("University ERP");
        title.setFont(DashboardTheme.FONT_TITLE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("Sign in to your account");
        subtitle.setFont(DashboardTheme.FONT_REGULAR);
        subtitle.setForeground(DashboardTheme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fields
        userField = new JTextField(20);
        passField = new JPasswordField(20);
        
        // Login Button
        JButton loginBtn = DashboardComponents.createPrimaryButton("Sign In");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Error Message (Hidden by default)
        errorLabel = new JLabel("Invalid credentials");
        errorLabel.setForeground(DashboardTheme.DANGER);
        errorLabel.setFont(DashboardTheme.FONT_SMALL);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setVisible(false);

        // Inputs Wrapper
        JPanel inputs = new JPanel(new GridLayout(4, 1, 0, 10));
        inputs.setBackground(Color.WHITE);
        inputs.setBorder(new EmptyBorder(20, 0, 20, 0));
        inputs.add(createLabel("Username"));
        inputs.add(userField);
        inputs.add(createLabel("Password"));
        inputs.add(passField);
        inputs.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Action Logic
        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            
            // TODO: Replace with Real DB Check
            if (user.equals("admin") && pass.equals("admin")) {
                controller.loginSuccess("ADMIN");
            } else if (user.equals("student") && pass.equals("student")) {
                controller.loginSuccess("STUDENT");
            } else if (user.equals("prof") && pass.equals("prof")) {
                controller.loginSuccess("INSTRUCTOR");
            } else {
                showError("Invalid Username or Password");
            }
        });

        card.add(logo);
        card.add(Box.createVerticalStrut(10));
        card.add(title);
        card.add(subtitle);
        card.add(inputs);
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(loginBtn);

        add(card);
    }
    
    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(DashboardTheme.FONT_BOLD);
        l.setForeground(DashboardTheme.TEXT_SECONDARY);
        return l;
    }
    
    // Interface for callback
    public interface MainFrameController {
        void loginSuccess(String role);
    }

    //logic for performing login
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            setStatus("Please enter both username and password.", true);
            return;
        }

        setStatus("Authenticating...", false);
        AuthClass authUser = controller.authenticate(username, password, selectedRole);

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
            firePropertyChange("loginSuccess", null, authUser.role);
        } else {
            setStatus("Invalid credentials for " + selectedRole + ".", true);
        }
    }
    
    private void setStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? Color.RED : DashboardTheme.TEXT_SECONDARY);
    }
}