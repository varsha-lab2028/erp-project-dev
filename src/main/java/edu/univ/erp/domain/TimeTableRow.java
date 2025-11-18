package edu.univ.erp.domain;

import java.sql.Time;

/*this class has been created to help in displaying the timetable.
this class will contain the data of one row which will go into timetable later*/
public class TimeTableRow {
    private final String day;
    private final String timings;    //like "10:00 - 11:00"
    private final String course_code;
    private final String name; //name of the course
    private final String classroom; //like LHC C101

    //constructor
    public TimeTableRow(String day, String timings, String course_code, String name, String classroom) {
        this.day = day.trim().toUpperCase();
        this.timings = timings;
        this.course_code = course_code;
        this.name = name;
        this.classroom = classroom;
    }

    //getters
    public String getDay() {
        return day;
    }
    public String getTimings() {
        return timings;
    }
    public String getCourseCode() {
        return course_code;
    }
    public String getName() {
        return name;
    }
    public String getClassroom() {
        return classroom;
    }

    //method for the sake of formatting
    @Override
    public String toString() {
        return String.format("%-10s | %-12s | %-8s | %-25s | %s",
                day, timings, course_code, name, classroom);
    }
}
