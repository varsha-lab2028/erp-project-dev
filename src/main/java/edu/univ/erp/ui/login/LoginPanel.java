package edu.univ.erp.ui.login;


import edu.univ.erp.util.RoundedButton;
import edu.univ.erp.util.Theme;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final LoginController controller;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel statusLabel;

    public LoginPanel(LoginController controller) {
        this.controller = controller;
        setLayout(new GridBagLayout());
        setBackground(Theme.PRIMARY_WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("ERP Login", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.DEEP_SEA);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(Theme.FONT_TEXT);
        userLabel.setForeground(Theme.DEEP_SEA);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(Theme.FONT_TEXT);
        passLabel.setForeground(Theme.DEEP_SEA);

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        RoundedButton loginButton = new RoundedButton("Login");
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; add(title, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++; gbc.gridx = 0; add(userLabel, gbc);
        gbc.gridx = 1; add(usernameField, gbc);
        gbc.gridy++; gbc.gridx = 0; add(passLabel, gbc);
        gbc.gridx = 1; add(passwordField, gbc);
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2; add(loginButton, gbc);
        gbc.gridy++; add(statusLabel, gbc);

        loginButton.addActionListener(e -> onLogin());
    }

    private void onLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        boolean success = controller.authenticate(username, password);
        if (success) {
            statusLabel.setForeground(Theme.DEEP_SEA);
            statusLabel.setText("Login successful!");
            firePropertyChange("loginSuccess", false, true);
        } else {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Invalid username or password.");
        }
    }
}
