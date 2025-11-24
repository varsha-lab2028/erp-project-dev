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
     * @return AuthClass object on successful authentication and role matches, null otherwise
     */
    public AuthClass authenticate(String username, String password, String role) {
        try {
            AuthClass authUser = loginManager.login(username, password);
            if (authUser == null) {
                return null;
            }
            String userRole = authUser.role;
            // Basic role match check
            if (userRole == null || !userRole.equalsIgnoreCase(role)) {
                return null;
            }
            // Authentication successful and role matches
            return authUser;
        } catch (Exception e) {
            // Log or handle specific auth exceptions if needed
            return null;
        }
    }
}
