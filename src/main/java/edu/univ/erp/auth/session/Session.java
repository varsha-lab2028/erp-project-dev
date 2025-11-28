package edu.univ.erp.auth.session;

import edu.univ.erp.domain.SemesterSeason;
import edu.univ.erp.domain.User;

public final class Session {
    private static User current_user;
    
    // Store auth context
    private static long current_user_id = -1;
    private static String current_role = null; // Changed to String to match Database

    // --- GETTERS ---
    public static User user() {
        return current_user;
    }
    
    // Alias for compatibility with other controllers
    public static User getCurrentUser() {
        return current_user;
    }

    // --- LOGIN LOGIC ---
    public static void login(User user) {
        current_user = user;
        if(user != null) {
            current_user_id = user.getUserId();
            current_role = user.getRole(); // Assuming User.getRole() returns String
        } else {
            current_user_id = -1;
            current_role = null;
        }
    }
    
    // Alias for compatibility
    public static void setCurrentUser(User user) {
        login(user);
    }

    public static void logout() {
        current_user = null;
        current_user_id = -1;
        current_role = null;
    }

    public static boolean isLoggedIn() {
        return current_user_id != -1 || current_user != null;
    }

    // --- ROLE CHECKING ---
    public static boolean hasRole(String role) {
        if (current_role == null) return false;
        return current_role.equalsIgnoreCase(role);
    }

    public static long userId() {
        if (current_user_id != -1) {
            return current_user_id;
        } else if (current_user != null) {
            return current_user.getUserId();
        } else {
            return -1;
        }
    }

    // --- SEMESTER CONTEXT (Preserved from your code) ---
    private static int semester_number;
    private static SemesterSeason semester_season;
    private static int year;

    public static void setSemesterContext(int sem_no, SemesterSeason sem_season, int term_year) {
        semester_number = sem_no;
        semester_season = sem_season;
        year = term_year;
    }
    public static int getSemesterNumber() { return semester_number; }
    public static SemesterSeason getSemesterSeason() { return semester_season; }
    public static int getTermYear() { return year; }

    private Session(){}
}