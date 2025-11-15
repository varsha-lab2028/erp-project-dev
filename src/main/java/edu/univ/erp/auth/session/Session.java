package edu.univ.erp.auth.session;
import edu.univ.erp.domain.Role;
import edu.univ.erp.domain.SemesterSeason;
import edu.univ.erp.domain.User;

public final class Session {
    private static User current_user;
    public static User user(){
        return current_user;
    }
    public static void login(User user){
        current_user = user;
    }
    public static void logout(){
        current_user = null;
    }
    public static boolean isLoggedIn(){
        return current_user != null;
    }

    //for student flow (week 4)
    public static long userId() {
        if (current_user != null) {
            return current_user.getUserId();
        } else {
            return -1; //no user has logged in yet
        }
    }
    public static Role role() {
        if (current_user != null) {
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


