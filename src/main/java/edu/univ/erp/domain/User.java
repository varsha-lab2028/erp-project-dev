package edu.univ.erp.domain;


//refers to any logged in person
public class User {
    private final long user_id;
    private final String username;
    private final Role role;
    private final String status;


    public User(long userId, String username, Role role, String status) {
        this.user_id = userId;
        this.username = username;
        this.role = role;
        this.status = status;
    }


    public long getUserId() { return user_id; }
    public String getUsername() { return username; }
    public Role getRole() { return role; }
    public String getStatus() { return status; }
}
