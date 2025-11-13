package edu.univ.erp.ui.login;

import edu.univ.erp.auth.LoginManager;

public class LoginController {
    private final LoginManager loginManager;

    public LoginController(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public boolean authenticate(String username, String password) {
        return loginManager.validateCredentials(username, password);
    }
}
