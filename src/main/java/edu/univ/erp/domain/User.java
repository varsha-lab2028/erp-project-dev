package edu.univ.erp.domain;

public class User {
    private long userId;
    private String username;
    private String email;
    private String role;

    // Default constructor
    public User() {}

    // The Constructor used by AuthDAO
    public User(long userId, String username, String email, String role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // Getters
    public long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}