package edu.univ.erp.auth.session;
import edu.univ.erp.domain.User;

public final class Session {
    private static User current;
    public static void login(User u){
        current = u;
    }
    public static void logout(){
        current = null;
    }
    public static boolean isLoggedIn(){
        return current != null;
    }
    public static User user(){
        return current;
    }
    private Session(){}
}


