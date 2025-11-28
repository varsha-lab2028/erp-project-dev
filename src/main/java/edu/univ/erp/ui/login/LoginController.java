package edu.univ.erp.ui.login;

import edu.univ.erp.auth.AuthenticationService;
import edu.univ.erp.domain.User;
import edu.univ.erp.auth.session.Session;

public class LoginController {
    private final AuthenticationService authService;

    public LoginController(AuthenticationService authService) {
        this.authService = authService;
    }

    public User authenticate(String username, String password) {
        // 1. Call DB
        User user = authService.login(username, password);
        
        // 2. Save to Session (CRITICAL STEP)
        if (user != null) {
            // CRITICAL: This saves your login state so AccessControl works!
            Session.login(user); 
            System.out.println("DEBUG: Logged in as " + user.getUsername() + " (" + user.getRole() + ")");
        } else {
            System.out.println("DEBUG: Login failed for " + username);
        }
        
        return user;
    }
}