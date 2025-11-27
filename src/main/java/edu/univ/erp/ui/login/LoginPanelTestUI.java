package edu.univ.erp.ui.login;

import javax.swing.*;
import edu.univ.erp.auth.AuthenticationService;
import edu.univ.erp.domain.AuthClass;

public class LoginPanelTestUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPanelTestUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("IIITD ERP Login Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 800);
        frame.setLocationRelativeTo(null);

        // Mock Service
        AuthenticationService mockAuthService = new AuthenticationService() {
            @Override
            public AuthClass login(String username, String password) throws Exception {
                // Simulate DB Check
                if ("student".equalsIgnoreCase(username) && "123".equals(password)) 
                    return createMockUser("S01", "student", "STUDENT");
                if ("instructor".equalsIgnoreCase(username) && "123".equals(password)) 
                    return createMockUser("I01", "instructor", "INSTRUCTOR");
                if ("admin".equalsIgnoreCase(username) && "123".equals(password)) 
                    return createMockUser("A01", "admin", "ADMIN");
                return null;
            }
        };

        LoginController testController = new LoginController(mockAuthService);
        LoginPanel loginPanel = new LoginPanel(testController);

        // Success Listener
        loginPanel.addPropertyChangeListener("loginSuccess", evt -> {
            JOptionPane.showMessageDialog(frame, "✅ Login Success for role: " + evt.getNewValue());
        });

        frame.add(loginPanel);
        frame.setVisible(true);
        
        System.out.println("Test Running...");
        System.out.println("Make sure 'iiitd_bg_blurred.jpg' and 'iiitd_logo.png' are in the project folder or resources.");
    }

    private static AuthClass createMockUser(String id, String username, String role) {
        AuthClass user = new AuthClass();
        user.user_id = id; user.username = username; user.role = role;
        return user;
    }
}