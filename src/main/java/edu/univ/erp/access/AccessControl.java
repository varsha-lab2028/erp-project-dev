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

    //blocks every change while maintenance is ON. Admin is an exception to this though
    public static void checkWritable() {
        if(maintenanceService.isMaintenanceOn() && !Session.hasRole("ADMIN")){
            throw new RuntimeException("System is under maintenance. Actions are disabled");
        }
    }
    
    public static void requireStudentWriteAccess() {
        checkRole("STUDENT");
        checkWritable();
    }
}