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
        setLayout(null);
        setBackground(Theme.PRIMARY_WHITE);

        //creating a particular sized panel
        Dimension preference = new Dimension(500, 420);
        setPreferredSize(preference);

        //to center the entire login panel in the window
        int box_width = 440;
        int box_height = 380;
        int panel_width = preference.width;
        int panel_height = preference.height;
        JPanel box = new JPanel(null);
        box.setBackground(Theme.PRIMARY_WHITE);
        box.setBounds(30, 20, box_width, box_height);
        add(box);

        /*
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;*/

        //adding the IIITD logo to the login panel
        ImageIcon iiitd_logo = new ImageIcon("src/main/resources/IIITD LOGO VERTICAL.png");
        Image scaled = iiitd_logo.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);
        logo.setBounds((box_width - 140) / 2, 10, 140, 140); //to position the logo
        box.add(logo);

        //username label
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(Theme.FONT_TEXT);
        userLabel.setForeground(Theme.DEEP_SEA);
        userLabel.setBounds(40, 180, 100, 25);
        box.add(userLabel);

        //password label
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(Theme.FONT_TEXT);
        passLabel.setForeground(Theme.DEEP_SEA);
        passLabel.setBounds(40, 220, 100, 25);
        box.add(passLabel);

        //the box to put your username or password in
        usernameField = new JTextField(15);
        usernameField.setBounds(150, 180, 230, 25);
        box.add(usernameField);
        passwordField = new JPasswordField(15);
        passwordField.setBounds(150, 220, 230, 25);
        box.add(passwordField);

        //adding the login button at the bottom
        RoundedButton loginButton = new RoundedButton("Login");
        loginButton.setBounds(40, 265, 340, 35);
        box.add(loginButton);

        //adding the login status label
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setBounds(40, 310, 340, 20);
        box.add(statusLabel);

        /*
        JLabel title = new JLabel("ERP Login", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.DEEP_SEA); */

        // Layout
        /*
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; add(title, gbc);
        gbc.gridwidth = 1;
        gbc.gridy++; gbc.gridx = 0; add(logo, gbc);
        gbc.gridx = 1; add(usernameField, gbc);
        gbc.gridy++; gbc.gridx = 0; add(passLabel, gbc);
        gbc.gridx = 1; add(passwordField, gbc);
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2; add(loginButton, gbc);
        gbc.gridy++; add(statusLabel, gbc); */

        //for performing the login on clicking the action button
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
