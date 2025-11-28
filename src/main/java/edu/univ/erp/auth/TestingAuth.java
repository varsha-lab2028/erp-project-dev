package edu.univ.erp.auth;
import edu.univ.erp.domain.AuthClass;

public class TestingAuth {
    public static void main(String[] args) throws Exception {
        LoginManager svc = new LoginManager();

        tryLogin(svc, "admin1", "admin@123");
        tryLogin(svc, "inst1",  "inst@123");
        tryLogin(svc, "stu1",   "stu1@123");
        tryLogin(svc, "stu2",   "stu2@123");
        //to test if it fails
        tryLogin(svc, "stu2",   "wrong");
    }

    private static void tryLogin(LoginManager svc, String u, String p) {
        try {
            AuthClass ua = svc.login(u, p);
            System.out.println("OK: " + ua.username + " [" + ua.role + "] user_id=" + ua.user_id);
        } catch (Exception ex) {
            System.out.println("FAIL (" + u + "): " + ex.getMessage());
        }
    }
}
