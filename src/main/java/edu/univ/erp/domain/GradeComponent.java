package edu.univ.erp.domain;
import java.util.*;

//shows which assessments have what component in them
//making this class for a specific course and specific section
public class GradeComponent {
    private long course_id; //the course which uses a particular grading scheme
    private long section_id; //the section which uses that course's grading scheme
    private long instructor_id; //the instructor who is using this grading scheme
    private String assessment_name; //like 'quiz', 'mid-sem', 'end-sem'
    private int weightage;

    public GradeComponent(long course_id, long section_id, long instructor_id, String assessment_name, int weightage){
        if(course_id <= 0){
            throw new IllegalArgumentException("Appropriate course ID should be mentioned");
        }
        if (section_id <= 0) {
            throw new IllegalArgumentException("Appropriate Section ID should be mentioned");
        }
        if(instructor_id <= 0){
            throw new IllegalArgumentException("Appropriate Instructor ID should be mentioned");
        }
        if (assessment_name == null || assessment_name.isBlank()) {
            throw new IllegalArgumentException("Appropriate name should be mentioned");
        }
        if (weightage < 0 || weightage > 100) {
            throw new IllegalArgumentException("Should be in the range of 0 to 100");
        }
        this.course_id = course_id;
        this.section_id = section_id;
        this.instructor_id = instructor_id;
        this.assessment_name = assessment_name;
        this.weightage = weightage;
    }

    //getters
    public long getCourseId() {
        return course_id;
    }
    public long getSectionId() {
        return section_id;
    }
    public long getInstructorId(){
        return instructor_id;
    }
    public String getAssessmentName(){
        return assessment_name;
    }
    public int getWeightage(){
        return weightage;
    }
}
