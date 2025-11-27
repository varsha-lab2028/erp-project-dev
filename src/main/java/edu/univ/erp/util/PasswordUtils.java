package edu.univ.erp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtils {
    
    // Simulating BCrypt for now to avoid dependency errors until you add the JAR.
    // When you add jBCrypt, replace this with BCrypt.hashpw(password, BCrypt.gensalt());
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        // In real BCrypt: return BCrypt.checkpw(plainPassword, hashedPassword);
        String newHash = hashPassword(plainPassword);
        return newHash.equals(hashedPassword);
    }
}