package edu.univ.erp.ui.login;

import edu.univ.erp.auth.AuthenticationService;
import edu.univ.erp.domain.AuthClass;

//acts as a middle bridge between login UI and AuthenticationService
public class LoginController {
    // Change type from LoginManager to AuthenticationService
    private final AuthenticationService auth_service;

    public LoginController(AuthenticationService auth_service) {
        this.auth_service = auth_service;
    }

    //returns the authenticated user if login is successful
    public AuthClass authenticate(String username, String password, String selected_role) {
        try {
            //verifying the user + password
            AuthClass user = auth_service.login(username, password);
            if (user == null) {
                return null; //login failed
            }
            //check whether selected_role matches the user.role from DB
            if (user.role == null || !user.role.equalsIgnoreCase(selected_role)) {
                return null; // role mismatch
            }
            return user; //will return if everything is correct
        } catch (Exception e) {
            return null;
        }
    }
}
