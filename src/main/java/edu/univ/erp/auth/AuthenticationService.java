package edu.univ.erp.auth;

import edu.univ.erp.auth.AuthDAO;
import edu.univ.erp.domain.User;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {
    private final AuthDAO authDAO = new AuthDAO();

    private static final Map<String, Integer> failedAttempts = new HashMap<>();
    private static final int MAX_ATTEMPTS = 5;

    public User login(String username, String password) {
        int attempts = failedAttempts.getOrDefault(username, 0);
        if (attempts >= MAX_ATTEMPTS) {
            System.err.println("Security Alert: Account " + username + " is temporarily locked.");
            return null; //blocks the login
        }

        try {
            //trying to log in
            User user = authDAO.login(username, password);
            
            if (user != null) {
                //reset counter
                failedAttempts.remove(username);
                return user;
            } else {
                //if failure, then increment counter applied
                failedAttempts.put(username, attempts + 1);
                int remaining = MAX_ATTEMPTS - (attempts + 1);
                System.out.println("Login failed for " + username + ". Attempts remaining: " + remaining);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void logout() {
    }
}