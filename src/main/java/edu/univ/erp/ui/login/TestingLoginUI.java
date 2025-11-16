package edu.univ.erp.ui.login;

import edu.univ.erp.auth.LoginManager;
import edu.univ.erp.domain.AuthClass;

import javax.swing.*;

public class TestingLoginUI {
    public static void main(String[] args) {
        // Dummy LoginManager ONLY for UI testing
        LoginManager dummyManager = new LoginManager() {
            @Override
            public AuthClass login(String username, String password) throws Exception {

                // ---------- OPTION 1: ALWAYS FAIL (to test error message) ----------
                // uncomment this to always show "Invalid username or password."
                // throw new Exception("Dummy failure for UI testing");

                // ---------- OPTION 2: ALWAYS SUCCEED (to test success path) ----------
                AuthClass a = new AuthClass();
                a.user_id = 1;
                a.username = username;
                a.role = "STUDENT";
                a.password_hash = "";
                a.auth_status = "ACTIVE";
                return a;
            }
        };

        // Use the dummy manager in the controller
        LoginController controller = new LoginController(dummyManager);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Login UI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);

            frame.add(new LoginPanel(controller));
            frame.setVisible(true);
        });
    }
}
