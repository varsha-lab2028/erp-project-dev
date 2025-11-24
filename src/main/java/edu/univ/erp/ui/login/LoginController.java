package edu.univ.erp.ui.login;

import edu.univ.erp.auth.AuthenticationService;

public class LoginController {
    // Change type from LoginManager to AuthenticationService
    private final AuthenticationService authService;

    public LoginController(AuthenticationService authService) {
        this.authService = authService;
    }

    public boolean authenticate(String username, String password, String role) {
        try {
            authService.login(username, password);
            return true;
        } catch (Exception e) {
            // e.printStackTrace(); // Uncomment for debugging
            return false;
        }
    }
}//new commit