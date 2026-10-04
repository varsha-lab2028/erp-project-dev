package edu.univ.erp.auth;

// Prints the 4 account lines for src/db/seed.sql.
public class GeneratingHashes {
    public static void main(String[] args) {
        String[][] accounts = {
                {"admin1", "ADMIN",      "admin123"},
                {"inst1",  "INSTRUCTOR", "inst123"},
                {"stu1",   "STUDENT",    "stud123"},
                {"stu2",   "STUDENT",    "stud234"}
        };
        for (int i = 0; i < accounts.length; i++) {
            String[] a = accounts[i];
            String end = (i == accounts.length - 1) ? ";" : ",";
            System.out.println("    ('" + a[0] + "', '" + a[1] + "', '"
                    + PasswordHasher.hash(a[2]) + "')" + end);
        }
    }
}