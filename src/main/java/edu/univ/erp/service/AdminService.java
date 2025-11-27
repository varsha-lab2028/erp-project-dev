package edu.univ.erp.service;

import edu.univ.erp.data.SectionDAO;
import edu.univ.erp.data.UserDAO;
import edu.univ.erp.data.CourseDAO;
import edu.univ.erp.domain.Course;
import edu.univ.erp.domain.Section;
import edu.univ.erp.domain.User;
import edu.univ.erp.auth.AuthDAO;
import edu.univ.erp.access.AccessControl;
import edu.univ.erp.service.MaintenanceService;

import java.sql.SQLException;
import java.util.List;

//AdminService provides business logic for administrative functionalities.
public class AdminService {
    private final UserDAO userDAO = new UserDAO();
    private final SectionDAO sectionDAO = new SectionDAO();
    private final AuthDAO auth_dao = new AuthDAO();
    private final MaintenanceService maintenanceService = new MaintenanceService();
    private final CourseDAO course_dao = new CourseDAO();

    public AdminService() {
        // constructor code if needed
    }

    public List<User> getAllUsers() throws SQLException {
        //for maintenance
        AccessControl.checkRole("ADMIN");
        return userDAO.listAllUsers();
    }

    public java.util.List<Course> getAllCourses() throws java.sql.SQLException {
        AccessControl.checkRole("ADMIN");
        return course_dao.listCourses();
    }

    public List<Section> getAllSections() throws SQLException {
        //for maintenance
        AccessControl.checkRole("ADMIN");
        return sectionDAO.listAllSections();
    }

    // to let the user login into the ERP
    public void allowUserLogin(long user_id) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();
        auth_dao.updateStatus(user_id, "ACTIVE");
    }

    // to not let user login into the ERP
    public void deactivateUser(long user_id) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();
        auth_dao.updateStatus(user_id, "INACTIVE");
    }

    public String getAdminDashboardInfo() {
        AccessControl.checkRole("ADMIN");
        return "Admin dashboard information placeholder";
    }

    //maintenance mode controls
    public boolean isMaintenanceOn() {
        AccessControl.checkRole("ADMIN");
        return maintenanceService.isMaintenanceOn();
    }

    public void setMaintenanceMode(boolean on) {
        AccessControl.checkRole("ADMIN");
        // no checkWritable() here – this is what flips maintenance!
        maintenanceService.toggleMaintenance(on);
    }

    //Managing the courses
    // CREATE course
    public void createCourse(String courseCode, String name, int credits) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();

        course_dao.insertCourse(courseCode, name, credits);
    }

    // EDIT course
    public void editCourse(String courseCode, String name, int credits) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();

        course_dao.updateCourse(courseCode, name, credits);
    }

    // DELETE course
    public void deleteCourse(String courseCode) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();

        course_dao.deleteCourse(courseCode);
    }

    //Managing the sections
    // CREATE section
    public void createSection(Section s) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();

        sectionDAO.insertSection(s);
    }

    // EDIT section
    public void editSection(Section s) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();

        sectionDAO.updateSection(s);
    }

    // DELETE section
    public void deleteSection(long sectionId) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();

        sectionDAO.deleteSection(sectionId);
    }

    // ASSIGN instructor to section
    public void assignInstructor(long sectionId, long instructorUserId) throws Exception {
        AccessControl.checkRole("ADMIN");
        AccessControl.checkWritable();

        sectionDAO.assignInstructor(sectionId, instructorUserId);
    }

}
