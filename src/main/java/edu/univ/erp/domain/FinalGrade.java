package edu.univ.erp.domain;
import java.util.*;

public class FinalGrade {
    private long course_id;
    private long section_id;
    private long enrollment_id; //linking the result to a particular student in the section, it is like an identification
    private LetterGrade grade_letter; //getting an A, B
    private double course_cg;

    public FinalGrade(long course_id, long section_id, long enrollment_id, LetterGrade grade_letter, double course_cg){
        if (enrollment_id <= 0) {
            throw new IllegalArgumentException("Appropriate Enrollment ID should be mentioned");
        }
        if (grade_letter == null) {
            throw new IllegalArgumentException("Appropriate grade should be mentioned");
        }
        if(course_cg < 0.0 || course_cg > 10.0){
            throw new IllegalArgumentException("Course's cgpa must be in the range of 0-10");
        }
        this.enrollment_id = enrollment_id;
        this.grade_letter = grade_letter;
        this.course_cg = course_cg;
    }


}
