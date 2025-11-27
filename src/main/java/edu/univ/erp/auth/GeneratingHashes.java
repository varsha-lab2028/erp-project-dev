package edu.univ.erp.auth;

public class GeneratingHashes {
    public static void main(String[] args) {
        System.out.println("admin1 / admin123 -> " +
                PasswordHasher.hash("admin123"));

        System.out.println("instructor1 / inst123 -> " +
                PasswordHasher.hash("inst123"));

        System.out.println("student1 / stud123 -> " +
                PasswordHasher.hash("stud123"));

        System.out.println("student2 / stud234 -> " +
                PasswordHasher.hash("stud234"));
    }
}
