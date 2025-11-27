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


    /*
    public static boolean isReadOnlyNow(){ return maintenance.isMaintenanceOn(); }

    public static boolean canAccess(String action){
        if (!Session.isLoggedIn()) {
            return false;
        }
        Role role = Session.user().getRole(); //to know the role of the currently logged-in user
        boolean maintenanceOn = isReadOnlyNow();
        //admin managing
        if (action.equals("ADMIN_MANAGE")) {
            return role == Role.ADMIN;
        }
        //instructor editing the grades
        if (action.equals("INSTR_EDIT_GRADES")) {
            return role == Role.INSTRUCTOR && !maintenanceOn;
        }
        //student who is registering and dropping the courses
        if (action.equals("STU_REGISTER")) {
            return role == Role.STUDENT && !maintenanceOn;
        }
        return true;
    }
     */
}
