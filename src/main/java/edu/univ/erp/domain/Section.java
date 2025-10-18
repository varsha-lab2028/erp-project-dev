package edu.univ.erp.domain;
import java.util.*;

public class Section{
    private final long id;
    private final String course_code;
    private final long instructor_userid;
    private final String day; //day when this section has a class
    private final String timings; //time when this section has class on that particular day
    private final String classroom;
    private final int capacity;
    private final String semester;
    private final int year;

    public Section(long id, String course_code, long instructor_userid,
                   String day, String timings, String classroom, int capacity,
                   String semester, int year){
        //constructor
        if(id<=0){
            throw new IllegalArgumentException("Appropriate ID should exist");
        }
        if(course_code == null || course_code.isBlank()){
            throw new IllegalArgumentException("Course code should be mentioned");
        }
        if(instructor_userid <= 0){
            throw new IllegalArgumentException("Appropriate User ID should be there");
        }
        if(day == null || day.isBlank()){
            throw new IllegalArgumentException("The class's day should be mentioned");
        }
        if(timings == null || timings.isBlank()){
            throw new IllegalArgumentException("Class timings field should be mentioned");
        }
        if(classroom == null || classroom.isBlank()){
            throw new IllegalArgumentException("Classroom number should be mentioned");
        }
        if(capacity <= 0){
            throw new IllegalArgumentException("Capacity of students in the course should be appropriate");
        }
        if(semester == null || semester.isBlank()){
            throw new IllegalArgumentException("Semester number should be mentioned");
        }
        if(year <= 2000){
            throw new IllegalArgumentException("Capacity of students in the course should be appropriate");
        }

        this.id = id;
        this.course_code = course_code;
        this.instructor_userid = instructor_userid;
        this.day = day;
        this.timings = timings;
        this.classroom = classroom;
        this.capacity = capacity;
        this.semester = semester;
        this.year = year;
    }

    //getters
    public long getId(){
        return id;
    }
    public String course_code(){
        return course_code;
    }
    public long getInstructor_userid(){
        return instructor_userid;
    }
    public String getDay(){
        return day;
    }
    public String getTimings(){
        return timings;
    }
    public String getClassroom(){
        return classroom;
    }
    public int getCapacity(){
        return capacity;
    }
    public String getSemester(){
        return semester;
    }
    public int getYear(){
        return year;
    }


}
