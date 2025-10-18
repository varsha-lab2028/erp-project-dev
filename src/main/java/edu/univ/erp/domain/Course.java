package edu.univ.erp.domain;
import java.util.*;

public class Course {
    private final String name;
    private final String course_code;
    private final int credits;
    public Course(String name, String course_code, int credits) {
        //constructor
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("Course name field should exist");
        }
        if(course_code == null || course_code.isBlank()){
            throw new IllegalArgumentException("Course code field should exist");
        }
        if(credits < 1 || credits > 4){
            throw new IllegalArgumentException("Course credits should be appropriate");
        }
        this.name = name;
        this.course_code = course_code;
        this.credits = credits;
    }

    //getters
    public String getName(){
        return name;
    }
    public String getCourseCode(){
        return course_code;
    }
    public int getCredits(){
        return credits;
    }
}


