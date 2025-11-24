package edu.univ.erp.auth;
import edu.univ.erp.domain.AuthClass;

/*this class contains the logic on how the authentication/logging in process
is going to be*/

public class LoginManager {
    private final AuthDAO auth_dao = new AuthDAO();

    public AuthClass login(String username, String raw_password) throws Exception {
        AuthClass u = auth_dao.findByUsername(username);
        if(u == null){
            throw new IllegalArgumentException("incorrect username");
        }
        if (!"ACTIVE".equals(u.auth_status)) {
            throw new IllegalStateException("account is likely disabled");
        }
        if (!PasswordHasher.verifyHash(raw_password, u.password_hash)) {
            throw new IllegalArgumentException("incorrect username or password");
        }
        auth_dao.updateLastLogin(u.user_id);
        return u; // contains the user_id + role, will be used later to load ERP profile
    }
}
