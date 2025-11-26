package edu.univ.erp;

import edu.univ.erp.auth.AuthenticationService;
import edu.univ.erp.auth.LoginManager;
import edu.univ.erp.domain.AuthClass;
import edu.univ.erp.service.InstructorService;
import edu.univ.erp.service.MaintenanceService;
import edu.univ.erp.service.StudentService;

// New Service Registry to hold backend services centrally
public class ServiceRegistry {
    public final StudentService student_service;
    public final MaintenanceService maintenance_service;
    public final AuthenticationService auth_service;
    public final InstructorService instructor_service;

    private static final boolean USE_DUMMY_LOGIN = true; //just for dummy

    public ServiceRegistry() {
        this.student_service = new StudentService();
        this.maintenance_service = new MaintenanceService();
        this.instructor_service = new InstructorService();
        //this.auth_service = new LoginManager(); //actual for login
        if(USE_DUMMY_LOGIN) {
            this.auth_service = (username, password) -> {
                AuthClass a = new AuthClass();
                a.username = username;
                a.auth_status = "ACTIVE";

                if ("admin1".equals(username) && "pass123".equals(password)) {
                    a.role = "ADMIN";
                    a.user_id = 1;
                    return a;
                } else if ("student1".equals(username) && "pass123".equals(password)) {
                    a.role = "STUDENT";
                    a.user_id = 3;
                    return a;
                } else if ("instructor1".equals(username) && "pass123".equals(password)) {
                    a.role = "INSTRUCTOR";
                    a.user_id = 2;
                    return a;
                } else {
                    throw new Exception("Invalid username or password");
                }
            };
        }
        else {
            //real login
            this.auth_service = new LoginManager();
        }
    }
}
