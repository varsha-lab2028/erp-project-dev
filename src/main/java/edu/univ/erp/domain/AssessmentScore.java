package edu.univ.erp.domain;

//this class goes in depth of the assessment having a particular component in the course
public class AssessmentScore{
    private long enrollment_id;
    private String component;
    private double score;
    public AssessmentScore(long enrollment_id, String component, double score){
        if(enrollment_id <= 0){
            throw new IllegalArgumentException("enrollment id>=0");
        }
        if (component == null || component.isBlank()) {
            throw new IllegalArgumentException("component");
        }
        if (score < 0) {
            throw new IllegalArgumentException("score>=0");
        }
    }

}
