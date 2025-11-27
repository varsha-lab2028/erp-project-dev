package edu.univ.erp.access;

import edu.univ.erp.auth.session.Session;
import edu.univ.erp.domain.Role;
import edu.univ.erp.service.MaintenanceService;

//relates to the admin, when admin is logged in
public class AccessControl {
    private static final MaintenanceService maintenance = new MaintenanceService();

    public static void checkRole(String allowedRole) {
        if (!Session.isLoggedIn()) {
            throw new RuntimeException("Access denied - not logged in");
        }

        Role r = Session.role();
        if (r == null || !r.name().equalsIgnoreCase(allowedRole)) {
            throw new RuntimeException("Access denied - role not permitted");
        }
    }

    public static void checkWritable() {
        if (maintenance.isMaintenanceOn()) {
            throw new RuntimeException("System in maintenance mode - read only");
        }
    }

    public static boolean maintenanceOn() {
        return maintenance.isMaintenanceOn();
    }

    public static void requireStudentWriteAccess() {
        checkWritable();
    }


}
