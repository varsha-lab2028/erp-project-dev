package edu.univ.erp.service;

import edu.univ.erp.data.SectionDAO;
import edu.univ.erp.data.UserDAO;
import edu.univ.erp.domain.Section;
import edu.univ.erp.domain.User;

import java.sql.SQLException;
import java.util.List;

/**
 * AdminService provides business logic for administrative functionalities.
 */
public class AdminService {
    private final UserDAO userDAO = new UserDAO();
    private final SectionDAO sectionDAO = new SectionDAO();

    public AdminService() {
        // constructor code if needed
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.listAllUsers();
    }

    public List<Section> getAllSections() throws SQLException {
        return sectionDAO.listAllSections();
    }

    // Additional admin-related business logic methods for user management,
    // course and section management to be added here.

}
