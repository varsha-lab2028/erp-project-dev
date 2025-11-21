package edu.univ.erp.ui.login;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
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

    // IIITD Specific Colors
    private static final Color IIITD_TEAL = new Color(0, 128, 128); // The main brand color
    private static final Color BG_COLOR = new Color(240, 244, 248); // Professional Light Grey-Blue
    private static final Color TEXT_LABEL = new Color(100, 116, 139); // Muted Blue-Grey for labels

    public LoginPanel(LoginController controller) {
        this.controller = controller;

        // 1. Main Background Setup
        setLayout(new GridBagLayout());
        setBackground(BG_COLOR);

        // 2. The "Card" (The floating white box)
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        
        // create a "Shadow" effect using a double border
        card.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230), 1),
            new EmptyBorder(40, 60, 50, 60) // Generous internal padding
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- 1. LOGO ---
        JLabel logoLabel = new JLabel("", SwingConstants.CENTER);
        try {
            ImageIcon originalIcon = new ImageIcon("src/main/resources/IIITD LOGO VERTICAL.png");
            if (originalIcon.getIconWidth() > 0) {
                Image scaled = originalIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaled));
            } else {
                // Fallback Text Logo
                logoLabel.setText("<html><div style='text-align: center;'>IIITD<br><span style='font-size:10px'>ERP SYSTEM</span></div></html>");
                logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
                logoLabel.setForeground(IIITD_TEAL);
            }
        } catch (Exception e) {
            logoLabel.setText("IIITD ERP");
        }
        gbc.insets = new Insets(0, 0, 20, 0);
        card.add(logoLabel, gbc);

        // --- 2. TITLE ---
        JLabel titleLabel = new JLabel("Welcome Back", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(30, 41, 59)); // Dark Slate
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);
        card.add(titleLabel, gbc);

        // --- 3. PROFILE TYPE SELECTOR (Cleaned Up) ---
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 30, 0);
        card.add(createProfileSelector(), gbc);

        // --- 4. INPUTS ---
        
        // Username
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        card.add(createLabel("USERNAME"), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 20, 0);
        usernameField = createStyledField(false);
        card.add(usernameField, gbc);

        // Password
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 8, 0);
        card.add(createLabel("PASSWORD"), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 30, 0);
        passwordField = (JPasswordField) createStyledField(true);
        card.add(passwordField, gbc);

        // --- 5. LOGIN BUTTON ---
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        
        // Custom Button Styling to match IIITD Teal
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(IIITD_TEAL);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(100, 45)); // Taller, modern button
        
        // Add hover effect
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { loginButton.setBackground(IIITD_TEAL.darker()); }
            public void mouseExited(MouseEvent evt) { loginButton.setBackground(IIITD_TEAL); }
        });
        
        loginButton.addActionListener(e -> onLogin());
        card.add(loginButton, gbc);

        // --- 6. STATUS LABEL ---
        gbc.gridy++;
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        card.add(statusLabel, gbc);

        add(card);
    }

    // --- Helper: The "Profile Type" Dropdown ---
    private JPanel createProfileSelector() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Profile Type:");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_LABEL);

        String[] users = {"Select Profile...", "Student (student1)", "Instructor (prof1)", "Admin (admin1)"};
        JComboBox<String> combo = new JComboBox<>(users);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setBackground(Color.WHITE);
        combo.setFocusable(false);
        
        combo.addActionListener(e -> {
            String s = (String) combo.getSelectedItem();
            if (s != null) {
                if (s.contains("student1")) fillCreds("student1", "pass123");
                else if (s.contains("prof1")) fillCreds("prof1", "pass123");
                else if (s.contains("admin1")) fillCreds("admin1", "pass123");
            }
        });

        panel.add(lbl);
        panel.add(combo);
        return panel;
    }

    private void fillCreds(String u, String p) {
        usernameField.setText(u);
        passwordField.setText(p);
        statusLabel.setText(" ");
    }

    private void onLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setForeground(new Color(220, 38, 38)); // Red
            statusLabel.setText("Please enter your credentials.");
            return;
        }

        statusLabel.setText("Authenticating...");
        statusLabel.setForeground(Color.GRAY);

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return controller.authenticate(username, password);
            }
            @Override
            protected void done() {
                try {
                    if (get()) {
                        statusLabel.setForeground(IIITD_TEAL);
                        statusLabel.setText("Login successful!");
                        firePropertyChange("loginSuccess", false, true);
                    } else {
                        statusLabel.setForeground(new Color(220, 38, 38));
                        statusLabel.setText("Invalid username or password.");
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }

    // --- Helper: Labels ---
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(TEXT_LABEL);
        label.setBorder(new EmptyBorder(0, 2, 0, 0)); // Tiny left padding
        return label;
    }

    // --- Helper: Input Fields (Material Design Style) ---
    private JTextField createStyledField(boolean isPassword) {
        JTextField field = isPassword ? new JPasswordField(20) : new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        
        // Default border: Light Grey Bottom Border
        field.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 2, 0, new Color(226, 232, 240)), 
            new EmptyBorder(5, 5, 5, 5)
        ));

        // Focus border: IIITD Teal Bottom Border
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 0, 2, 0, IIITD_TEAL),
                    new EmptyBorder(5, 5, 5, 5)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 0, 2, 0, new Color(226, 232, 240)),
                    new EmptyBorder(5, 5, 5, 5)
                ));
            }
        });
        return field;
    }
}