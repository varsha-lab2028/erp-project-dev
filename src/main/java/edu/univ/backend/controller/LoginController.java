package edu.univ.backend.controller;

import edu.univ.erp.auth.LoginManager;
import edu.univ.erp.auth.AuthenticationService;
import edu.univ.erp.domain.AuthClass;

public class LoginController {
    private final LoginManager loginManager;

    public LoginController(AuthenticationService authService) {
        this.loginManager = (LoginManager) authService;
    }

    public LoginController() {
        this.loginManager = new LoginManager();
    }

    /**
     * Authenticates user by username, password, and role.
     * @param username
     * @param password
     * @param role - selected role from login UI
     * @return true if authentication successful and role matches, false otherwise
     */
    public boolean authenticate(String username, String password, String role) {
        try {
            AuthClass authUser = loginManager.login(username, password);
            if (authUser == null) {
                return false;
            }
            String userRole = authUser.role;
            // Basic role match check
            if (userRole == null || !userRole.equalsIgnoreCase(role)) {
                return false;
            }
            // TODO: Save authenticated user/session info if needed
            // Authentication successful and role matches
            return true;
        } catch (Exception e) {
            // Log or handle specific auth exceptions if needed
            return false;
        }
    }
}
