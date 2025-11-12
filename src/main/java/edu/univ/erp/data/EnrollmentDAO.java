/*this DAO has been made to perform student-section registration.
* this includes reading the record, inserting into the record, deleting from the
* record and checking the record*/
package edu.univ.erp.data;

import edu.univ.erp.domain.Enrollment;
import edu.univ.erp.domain.EnrollmentStatus;

import java.sql.*;
import java.util.*;

//user_id is the student's id here

public class EnrollmentDAO {
    //checking if a student has already been enrolled in the section
    public boolean checkRecordExistence(long user_id, long section_id) throws SQLException{
        String command = "SELECT 1 FROM enrollments WHERE user_id=? AND section_id=? AND e_status='REGISTERED'";
        try(Connection connection = ServerConnector.ERPConnection();
            PreparedStatement ps = connection.prepareStatement(command)){
            ps.setLong(1, user_id);
            ps.setLong(2, section_id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    //returning the number of students in a course's section
    public int countEnrolledInSection(long section_id) throws SQLException{
        String command = "SELECT COUNT(*) FROM enrollments WHERE section_id=? AND e_status='REGISTERED'";
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setLong(1, section_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }
        }
    }

    //method to insert a student into the enrollment record
    public void insertStudentEnrollment(long user_id, long section_id) throws SQLException{
        String command = """
            INSERT INTO enrollments(student_id, section_id, e_status, registered_when)
            VALUES (?, ?, 'REGISTERED', NOW())
        """;
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setLong(1, user_id);
            ps.setLong(2, section_id);
            ps.executeUpdate();
        }
    }

    //removing a student if they have dropped from the course's section
    public static void removeStudentEnrollment(long user_id, long section_id) throws SQLException{
        String command = """
            UPDATE enrollments
            SET e_status='DROPPED', dropped_when=NOW()
            WHERE student_id=? AND section_id=? AND e_status='REGISTERED'
        """;
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setLong(1, user_id);
            ps.setLong(2, section_id);
            ps.executeUpdate();
        }
    }

    //if a student has completed the course in a section
    //this info will be used by the instructor or admin later
    public void markCourseComplete(long user_id, long section_id) throws SQLException{
        String command = """
            UPDATE enrollments
            SET e_status='COMPLETED', completed_when=NOW()
            WHERE student_id=? AND section_id=? AND e_status='REGISTERED'
        """;
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setLong(1, user_id);
            ps.setLong(2, section_id);
            ps.executeUpdate();
        }
    }

    //method to list all the students that have been enrolled in the section so far
    public List<Enrollment> listByStudent(long user_id) throws SQLException {
        String command = """
            SELECT enrollment_id, student_id, section_id, e_status,
            registered_when, dropped_when, completed_when
            FROM enrollments
            WHERE student_id=?
            ORDER BY registered_when DESC
        """;

        List<Enrollment> enrollments = new ArrayList<>();
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(command)) {
            ps.setLong(1, user_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(new Enrollment(
                            rs.getLong("enrollment_id"),
                            rs.getLong("student_id"),
                            rs.getLong("section_id"),
                            EnrollmentStatus.valueOf(rs.getString("e_status")),
                            rs.getTimestamp("registered_when") != null ? rs.getTimestamp("registered_when").toLocalDateTime() : null,
                            rs.getTimestamp("dropped_when") != null ? rs.getTimestamp("dropped_when").toLocalDateTime() : null,
                            rs.getTimestamp("completed_when") != null ? rs.getTimestamp("completed_when").toLocalDateTime() : null
                    ));
                }
            }
        }
        return enrollments;
    }
}
