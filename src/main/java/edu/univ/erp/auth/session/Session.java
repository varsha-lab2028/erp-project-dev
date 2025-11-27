package edu.univ.erp.auth.session;

import edu.univ.erp.domain.Role;
import edu.univ.erp.domain.SemesterSeason;
import edu.univ.erp.domain.User;

public final class Session {
    private static User current_user;

    //store the auth context
    private static long current_user_id = -1;
    private static Role current_role;

    public static User user(){
        return current_user;
    }

    //one for erp_db
    public static void login(User user){
        current_user = user;
        if(user!=null){
            current_user_id = user.getUserId();
            current_role = user.getRole();
        } else {
            current_user_id = -1;
            current_role = null;
        }
    }

    //one for auth_db
    public static void login(long user_id, Role role) {
        current_user = null; // we don't have ERP User yet
        current_user_id = user_id;
        current_role = role;
    }

    public static void logout(){
        current_user = null;
        current_user_id = -1;
        current_role = null;
    }

    public static boolean isLoggedIn(){
        return current_user_id!=-1 || current_user != null;
    }

    //For student flows
    public static long userId() {
        if (current_user_id != -1) {
            return current_user_id;
        } else if (current_user != null) {
            return current_user.getUserId();
        } else {
            return -1;
        }
    }

    public static Role role() {
        if (current_role != null) {
            return current_role;
        } else if (current_user != null) {
            return current_user.getRole();
        } else {
            return null;
        }
    }

    //semester context = tells the system which semester is going on
    private static int semester_number;
    private static SemesterSeason semester_season;
    private static int year;

    public static void setSemesterContext(int sem_no, SemesterSeason sem_season, int term_year) {
        semester_number = sem_no;
        semester_season = sem_season;
        year = term_year;
    }
    public static int getSemesterNumber() {
        return semester_number;
    }
    public static SemesterSeason getSemesterSeason() {
        return semester_season;
    }
    public static int getTermYear() {
        return year;
    }
    private Session(){}
}


