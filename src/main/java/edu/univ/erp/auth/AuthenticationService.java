package edu.univ.erp.auth;

import edu.univ.erp.access.AccessControl;
import edu.univ.erp.domain.User;
import edu.univ.erp.auth.AuthDAO;

public class AuthenticationService {
    private AuthDAO authDAO = new AuthDAO();

    public User login(String username, String password) {
        try {
            // Simple bridge to your DAO logic
            return authDAO.login(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void logout() {
        // Clear session logic here
        // Session.clear(); 
    }
}