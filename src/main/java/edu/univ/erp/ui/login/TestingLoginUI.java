package edu.univ.erp.ui.login;

import edu.univ.erp.auth.AuthenticationService;
import edu.univ.erp.domain.AuthClass;
import edu.univ.erp.util.LoginTheme;

import javax.swing.*;

public class TestingLoginUI {
    public static void main(String[] args) {
        // 1. Apply System Theme
        LoginTheme.applyTheme();

        // 2. Create the Dummy Service (ONLY ONCE)
        AuthenticationService dummyService = (username, password) -> {
            AuthClass a = new AuthClass();
            a.username = username;
            a.auth_status = "ACTIVE"; 

            // CHECK 1: Admin
            if ("admin1".equals(username) && "pass123".equals(password)) {
                a.role = "ADMIN";
                a.user_id = 100;
                return a;
            }
            // CHECK 2: Student
            else if ("student1".equals(username) && "pass123".equals(password)) {
                a.role = "STUDENT";
                a.user_id = 101;
                return a;
            }
            // CHECK 3: Instructor
            else if ("prof1".equals(username) && "pass123".equals(password)) {
                a.role = "INSTRUCTOR";
                a.user_id = 102;
                return a;
            } 
            // CHECK 4: Fallback (or generic test)
            else if ("admin".equalsIgnoreCase(username)) { 
                 a.role = "ADMIN";
                 return a;
            }
            else {
                throw new Exception("Invalid credentials");
            }
        };

        // 3. Pass service to controller
        LoginController controller = new LoginController(dummyService);

        // 4. Launch UI
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("University ERP - Login Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            LoginPanel panel = new LoginPanel(controller);
            
            // Success Listener
            panel.addPropertyChangeListener("loginSuccess", evt -> {
                JOptionPane.showMessageDialog(frame, "Login Successful! Role: " + 
                    ((AuthenticationService)dummyService).getClass().getSimpleName()); 
                // In a real app, you would close this frame and open Dashboard
            });

            frame.add(panel);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}