package edu.univ.erp.domain;

public enum LetterGrade {
    A_PLUS("A+"), A("A"), A_MINUS("A-"),
    B_PLUS("B+"), B("B"), B_MINUS("B-"),
    C("C"), D("D"), F("F");

    public final String gradeLetter;
    LetterGrade(String gradeLetter) {
        this.gradeLetter = gradeLetter;
    }
}
