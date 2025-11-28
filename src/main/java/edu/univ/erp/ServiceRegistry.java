package edu.univ.erp;

import edu.univ.erp.auth.AuthenticationService;
import edu.univ.erp.service.InstructorService;
import edu.univ.erp.service.MaintenanceService;
import edu.univ.erp.service.StudentService;

public class ServiceRegistry {
    public AuthenticationService auth_service;
    public StudentService student_service;
    public InstructorService instructor_service;
    public MaintenanceService maintenance_service;

    public ServiceRegistry() {
      
        this.auth_service = new AuthenticationService();
        this.student_service = new StudentService();
        this.instructor_service = new InstructorService();
        this.maintenance_service = new MaintenanceService();
    }
}