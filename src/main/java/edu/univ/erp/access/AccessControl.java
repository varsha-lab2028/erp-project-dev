package edu.univ.erp.access;

import edu.univ.erp.auth.session.Session;

public class AccessControl {

    public static void checkRole(String requiredRole) {
        //check if logged in
        if (!Session.isLoggedIn()) {
            throw new RuntimeException("Access denied - not logged in");
        }

        if (!Session.hasRole(requiredRole)) {
            throw new RuntimeException("Access denied - insufficient permissions. Required: " + requiredRole);
        }
    }

    public static void checkWritable() {

    }
    
    public static void requireStudentWriteAccess() {

    }
}