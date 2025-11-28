package edu.univ.erp.access;

import edu.univ.erp.auth.session.Session;

public class AccessControl {

    public static void checkRole(String requiredRole) {
        // 1. Check if logged in
        if (!Session.isLoggedIn()) {
            throw new RuntimeException("Access denied - not logged in");
        }

        // 2. Check if role matches (Case insensitive)
        if (!Session.hasRole(requiredRole)) {
            throw new RuntimeException("Access denied - insufficient permissions. Required: " + requiredRole);
        }
    }

    public static void checkWritable() {
        // Optional maintenance check logic
    }
    
    public static void requireStudentWriteAccess() {
        // Optional logic
    }
}