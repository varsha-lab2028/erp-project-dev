package edu.univ.erp.service;

import edu.univ.erp.auth.AuthDAO;

//AdminService provides business logic for administrative functionalities.
public class AdminService {
    private final AuthDAO auth_dao = new AuthDAO();

    public AdminService() {
        // constructor code if needed
    }

    //to let the user login into the erp
    public void allowUserLogin(long user_id) throws Exception {
        auth_dao.updateStatus(user_id, "ACTIVE");
    }

    //to not let user login into the erp
    public void deactivateUser(long user_id) throws Exception {
        auth_dao.updateStatus(user_id, "INACTIVE");
    }

    // Example placeholder method
    public String getAdminDashboardInfo() {
        return "Admin dashboard information placeholder";
    }
}
