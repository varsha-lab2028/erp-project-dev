package edu.univ.erp.domain;

public enum LetterGrade {
    A_PLUS("A+"), A("A"), A_MINUS("A-"),
    B_PLUS("B+"), B("B"), B_MINUS("B-"),
    C("C"), C_MINUS("C-"),
    D("D"), D_MINUS("D-"),
    F("F");

    public final String grade_letter;
    LetterGrade(String grade_letter) {
        this.grade_letter = grade_letter;
    }

    // "A+" -> A_PLUS. valueOf("A+") fails because Java names can't contain "+"
    public static LetterGrade fromLabel(String label) {
        for (LetterGrade g : values()) {
            if (g.grade_letter.equals(label)) {
                return g;
            }
        }
        throw new IllegalArgumentException("Unknown grade: " + label);
    }
}