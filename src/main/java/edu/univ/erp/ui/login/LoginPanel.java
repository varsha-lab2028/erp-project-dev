package edu.univ.erp.ui.login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.util.PasswordUtils;

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
}