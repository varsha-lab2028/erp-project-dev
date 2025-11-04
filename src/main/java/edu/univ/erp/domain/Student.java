package edu.univ.erp.domain;
import java.util.*;

//going to link this to Auth through user_id
//keep program, year and status
public class Student {
    private final long user_id;
    private final String roll_no;
    private final String degree; //like BTech, MTech, PhD
    private final String branch;
    private final int term_year; //like if student is 1st year, 2nd year, etc.
    OnlineStatus status; //checking admin actions

    public Student(long user_id, String roll_no, String degree, String branch,
                   int term_year, OnlineStatus status){
        //constructor
        if(user_id<=0){
            throw new IllegalArgumentException("Appropriate User ID should exist");
        }
        if(roll_no == null || roll_no.isBlank()){
            throw new IllegalArgumentException("Roll Number Field should exist");
        }
        if(degree == null || degree.isBlank()){
            throw new IllegalArgumentException("Degree Field should exist");
        }
        if(branch == null || branch.isBlank()){
            throw new IllegalArgumentException("Branch Field should exist");
        }
        if(term_year <= 0){
            throw new IllegalArgumentException("Appropriate year should be entered");
        }
        if(status == null){
            throw new IllegalArgumentException("Status should be visible");
        }

        this.user_id = user_id;
        this.roll_no = roll_no;
        this.degree = degree;
        this.branch = branch;
        this.term_year = term_year;
        this.status = status;
    }

    //getters
    public long getUser_id(){
        return user_id;
    }
    public String getRoll_no(){
        return roll_no;
    }
    public String getDegree(){
        return degree;
    }
    public String getBranch(){
        return branch;
    }
    public int getTerm_year(){
        return term_year;
    }
    public OnlineStatus getStatus(){
        return status;
    }

}
