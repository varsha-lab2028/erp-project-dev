package edu.univ.erp.domain;

//this class goes in depth of the assessment having a particular component in the course
public class AssessmentScore{
    private long course_id;
    private long section_id;
    private String assessment_name;
    private long enrollment_id;
    private int ass_weightage;
    private double ass_score;

    public AssessmentScore(long course_id, long section_id, String assessment_name,
                           long enrollment_id, int ass_weightage, double ass_score){
        if(course_id <= 0){
            throw new IllegalArgumentException("course id>=0");
        }
        if(section_id <= 0){
            throw new IllegalArgumentException("section_id>=0");
        }
        if(assessment_name == null || assessment_name.isBlank()){
            throw new IllegalArgumentException("assessment name cant be blank");
        }
        if(enrollment_id <= 0){
            throw new IllegalArgumentException("enrollment id>=0");
        }
        if(ass_weightage <=0 ){
            throw new IllegalArgumentException("weightage>=0");
        }
        if (ass_score < 0) {
            throw new IllegalArgumentException("score>=0");
        }

        this.course_id = course_id;
        this.section_id = section_id;
        this.assessment_name = assessment_name;
        this.enrollment_id = enrollment_id;
        this.ass_weightage = ass_weightage;
        this.ass_score = ass_score;
    }

    //getters
    public long getCourse_id(){
        return course_id;
    }
    public long getSection_id(){
        return section_id;
    }
    public String getAssessment_name(){
        return assessment_name;
    }
    public long getEnrollment_id(){
        return enrollment_id;
    }
    public int getAss_weightage(){
        return ass_weightage;
    }
    public double getAss_score() {
        return ass_score;
    }
}
