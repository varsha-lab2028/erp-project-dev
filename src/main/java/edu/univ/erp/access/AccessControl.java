package edu.univ.erp.access;
import edu.univ.erp.auth.session.Session;
import edu.univ.erp.domain.Role;
import edu.univ.erp.service.MaintenanceService;

//relates to the admin, when admin is logged in
public class AccessControl {
    private static final MaintenanceService maintenance = new MaintenanceService();

    public static boolean isReadOnlyNow(){ return maintenance.isMaintenanceOn(); }

    public static boolean canAccess(String action){
        if (!Session.isLoggedIn()) return false;
        Role r = Session.user().getRole();
        return switch (action) {
            case "ADMIN_MANAGE" -> r == Role.ADMIN && !isReadOnlyNow();
            case "INSTR_EDIT_GRADES" -> r == Role.INSTRUCTOR && !isReadOnlyNow();
            case "STU_REGISTER" -> r == Role.STUDENT && !isReadOnlyNow();
            default -> true; // read-only actions
        };
    }
}
