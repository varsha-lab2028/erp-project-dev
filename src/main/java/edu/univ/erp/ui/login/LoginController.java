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
      
        User user = authService.login(username, password);

        if (user != null) {
      
            Session.login(user); 
            System.out.println("DEBUG: Logged in as " + user.getUsername() + " (" + user.getRole() + ")");
        } else {
            System.out.println("DEBUG: Login failed for " + username);
        }
        
        return user;
    }
}