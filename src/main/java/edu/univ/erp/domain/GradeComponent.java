package edu.univ.erp.domain;
import java.util.*;

//shows which assessments have what component in them
public class GradeComponent {
    private long section_id; //the section which uses this grading scheme
    private String assessment_name; //like quiz, mid-sem, end-sem
    private int weightage;

    public GradeComponent(long section_id, String assessment_name, int weightage){
        if (section_id <= 0) {
            throw new IllegalArgumentException("Appropriate Section ID should be mentioned");
        }
        if (assessment_name == null || assessment_name.isBlank()) {
            throw new IllegalArgumentException("Appropriate name should be mentioned");
        }
        if (weightage < 0 || weightage > 100) {
            throw new IllegalArgumentException("Should be in the range of 0 to 100");
        }
    }
}
