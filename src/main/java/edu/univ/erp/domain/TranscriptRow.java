package edu.univ.erp.domain;

public class TranscriptRow {
    private String courseCode;
    private String courseTitle;
    private int credits;
    private String finalGrade;

    public TranscriptRow(String courseCode, String courseTitle, int credits, String finalGrade) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.credits = credits;
        this.finalGrade = finalGrade;
    }

    public String getCourseCode() { return courseCode; }
    public String getCourseTitle() { return courseTitle; }
    public int getCredits() { return credits; }
    public String getFinalGrade() { return finalGrade; }
}
