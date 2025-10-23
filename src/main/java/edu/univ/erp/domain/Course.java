package edu.univ.erp.domain;
import java.util.*;

public class Course {
    private final long course_id; //id of the course
    private final String name;
    private final String course_code;
    private final int credits;
    public Course(long course_id, String name, String course_code, int credits) {
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
        this.course_id = course_id;
        this.name = name;
        this.course_code = course_code;
        this.credits = credits;
    }

    //getters
    public long getCourseId(){
        return course_id;
    }
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


