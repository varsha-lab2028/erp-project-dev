package edu.univ.erp.service;

import edu.univ.erp.data.SectionDAO;
import edu.univ.erp.data.UserDAO;
import edu.univ.erp.domain.Section;
import edu.univ.erp.domain.User;
import edu.univ.erp.auth.AuthDAO;

import java.sql.SQLException;
import java.util.List;

/*
 * AdminService provides business logic for administrative functionalities.
 */
public class AdminService {
    private final UserDAO userDAO = new UserDAO();
    private final SectionDAO sectionDAO = new SectionDAO();
    private final AuthDAO auth_dao = new AuthDAO();

    public AdminService() {
        // constructor code if needed
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.listAllUsers();
    }

    public List<Section> getAllSections() throws SQLException {
        return sectionDAO.listAllSections();
    }

    // to let the user login into the ERP
    public void allowUserLogin(long user_id) throws Exception {
        auth_dao.updateStatus(user_id, "ACTIVE");
    }

    // to not let user login into the ERP
    public void deactivateUser(long user_id) throws Exception {
        auth_dao.updateStatus(user_id, "INACTIVE");
    }

    // Example placeholder method
    public String getAdminDashboardInfo() {
        return "Admin dashboard information placeholder";
    }

    // Additional admin-related business logic methods for user management,
    // course and section management to be added here.

}
