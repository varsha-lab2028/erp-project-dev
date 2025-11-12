package edu.univ.erp.domain;

public enum LetterGrade {
    A_PLUS("A+"), A("A"), A_MINUS("A-"),
    B_PLUS("B+"), B("B"), B_MINUS("B-"),
    C("C"), D("D"), F("F");

    public final String grade_letter;
    LetterGrade(String grade_letter) {
        this.grade_letter = grade_letter;
    }
}
