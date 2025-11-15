package edu.univ.erp.domain;
import java.util.*;

//links to Auth DB
//keeps department, status
public class Instructor {
    private long user_id; //instructor will also have user_id
    private String instructor_name;
    private String department;
    OnlineStatus status;

    public Instructor(long user_id, String instructor_name, String department, OnlineStatus status){
        //constructor
        if (user_id <= 0){
            throw new IllegalArgumentException("Instructor's User ID should exist");
        }
        if(instructor_name == null || instructor_name.isBlank()){
            throw new IllegalArgumentException("Instructor name should exist");
        }
        if (department == null || department.isBlank()) {
            throw new IllegalArgumentException("Instructor's department should exist");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status should be visible");
        }

        this.user_id = user_id;
        this.instructor_name = instructor_name;
        this.department = department;
        this.status = status;
    }

    //getter functions
    public long getUser_id(){
        return user_id;
    }
    public String getInstructor_name(){
        return instructor_name;
    }
    public String getDepartment(){
        return department;
    }
    public OnlineStatus getStatus(){
        return status;
    }
}
