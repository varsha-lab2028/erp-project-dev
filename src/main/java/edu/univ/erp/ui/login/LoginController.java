package edu.univ.erp.ui.login;

import edu.univ.erp.auth.LoginManager;

//this class acts as an API which acts as an interface between the UI and backend logic
public class LoginController {
    private final LoginManager loginManager;

    public LoginController(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public boolean authenticate(String username, String password) {
        try {
            loginManager.login(username, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
