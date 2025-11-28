package edu.univ.erp.access;

import edu.univ.erp.auth.session.Session;
import edu.univ.erp.service.MaintenanceService;

public class AccessControl {
    
    // Connect to the service to check the flag
    private static final MaintenanceService maintenanceService = new MaintenanceService();

    public static void checkRole(String requiredRole) {
        //check if logged in
        if (!Session.isLoggedIn()) {
            throw new RuntimeException("Access denied - not logged in");
        }

        if (!Session.hasRole(requiredRole)) {
            throw new RuntimeException("Access denied - insufficient permissions. Required: " + requiredRole);
        }
    }

    // --- THIS IS THE FIX ---
    public static void checkWritable() {

    }
    
    public static void requireStudentWriteAccess() {

        // 1. Check if Maintenance is ON in the database
        if (maintenanceService.isMaintenanceOn()) {
            
            // 2. Admins are exempt (they can still edit things)
            if (Session.hasRole("ADMIN")) {
                return; 
            }
            
            // 3. Everyone else gets blocked
            throw new RuntimeException("System is under maintenance. Actions are temporarily disabled.");
        }
    }
    
    public static void requireStudentWriteAccess() {
        checkRole("STUDENT");
        checkWritable();
    }
}