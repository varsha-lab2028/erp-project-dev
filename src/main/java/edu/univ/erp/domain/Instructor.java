package edu.univ.erp.domain;
import java.util.*;

//links to Auth DB
//keeps department, status
public class Instructor {
    private long user_id; //instructor will also have user_id
    private String department;
    OnlineStatus status;

    public Instructor(long user_id, String department, OnlineStatus status){
        //constructor
        if (user_id <= 0){
            throw new IllegalArgumentException("Instructor's User ID should exist");
        }
        if (department == null || department.isBlank()) {
            throw new IllegalArgumentException("Instructor's department should exist");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status should be visible");
        }
    }
}
