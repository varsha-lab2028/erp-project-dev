package edu.univ.erp.domain;
import java.time.LocalDateTime;

//authentication class
public class AuthClass {
    public long user_id; //this is the id which will be assigned to a user when they log in
    public String username; //username of the user when they log in
    public String role; //student, instructor or admin
    public String auth_status; //a user has two standard states - ACTIVE or DISABLED
    public String password_hash; //the password the user enters
    public LocalDateTime last_login; //the last time someone (whatever the role is) logged in
}
