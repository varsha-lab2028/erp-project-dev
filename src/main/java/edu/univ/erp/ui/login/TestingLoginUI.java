package edu.univ.erp.ui.login;

import edu.univ.erp.auth.AuthenticationService;
import edu.univ.erp.domain.AuthClass;

import javax.swing.*;

public class TestingLoginUI {
    public static void main(String[] args) {
        // 1. Apply System Theme
        // Note: The aesthetic design is now primarily controlled by LoginPanel's custom colors.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // 2. Create the Dummy Service (Mocks successful login for testing UI flow)
        AuthenticationService dummyService = (username, password) -> {
            AuthClass a = new AuthClass();
            a.username = username;
            a.auth_status = "ACTIVE"; 

            // Mocked authentication logic:
            if ("admin1".equals(username) && "pass123".equals(password)) {
                a.role = "ADMIN";
                a.user_id = 100;
                return a;
            }
            else if ("student1".equals(username) && "pass123".equals(password)) {
                a.role = "STUDENT";
                a.user_id = 101;
                return a;
            }
            else if ("prof1".equals(username) && "pass123".equals(password)) {
                a.role = "INSTRUCTOR";
                a.user_id = 102;
                return a;
            } 
            else if ("admin".equalsIgnoreCase(username)) { 
                 a.role = "ADMIN";
                 return a;
            }
            else {
                throw new Exception("Invalid username or password");
            }
        };

        // 3. Pass the mock service to the controller
        LoginController controller = new LoginController(dummyService);

        // 4. Launch UI
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("University ERP - Login Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            LoginPanel panel = new LoginPanel(controller);
            
            // Success Listener (Shows a dialog instead of switching dashboards)
            panel.addPropertyChangeListener("loginSuccess", evt -> {
                JOptionPane.showMessageDialog(frame, "Login Successful!"); 
            });

            frame.add(panel);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}