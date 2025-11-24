package edu.univ.erp.ui.login;

import edu.univ.erp.auth.AuthenticationService;
import edu.univ.erp.domain.AuthClass;
import edu.univ.erp.auth.AuthDAO;

//login controller acts as the middle man between the login ui and the authentication service
public class LoginController {
    private final AuthenticationService authService;
    private final AuthDAO authDAO;

    public LoginController(AuthenticationService authService) {
        this.authService = authService;
        this.authDAO = new edu.univ.erp.auth.AuthDAO();
    }

    /**
     * Authenticate user and return AuthClass on success or null on failure.
     * @param username
     * @param password
     * @param role
     * @return AuthClass object if authenticated and role matches, else null
     */
    public AuthClass authenticate(String username, String password, String role) {
        try {
            AuthClass authUser = authService.login(username, password);
            if (authUser == null) return null;
            if (authUser.role == null || !authUser.role.equalsIgnoreCase(role)) return null;

            // All good, return AuthClass for session setting
            return authUser;
        } catch (Exception e) {
            return null;
        }
    }
}
