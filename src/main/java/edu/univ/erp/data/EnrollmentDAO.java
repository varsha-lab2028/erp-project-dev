package edu.univ.erp.data;

import edu.univ.erp.domain.Enrollment;
import edu.univ.erp.domain.EnrollmentStatus;
import edu.univ.erp.domain.Section;

import java.sql.*;
import java.util.*;

//this DAO is the backend for performing registrations
public class EnrollmentDAO {
    //checking if a student has already been enrolled in the section
    public boolean checkRecordExistence(long student_id, long section_id) throws SQLException{
        String command = "SELECT 1 FROM enrollments WHERE student_id=? AND section_id=? AND e_status='REGISTERED'";
        try(Connection connection = ServerConnector.ERPConnection();
            PreparedStatement ps = connection.prepareStatement(command)){
            ps.setLong(1, student_id);
            ps.setLong(2, section_id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    //counting the number of students who are currently registered in a section
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

    //inserting a student who has registered for a section
    public void insertStudentEnrollment(long student_id, long section_id) throws SQLException{
        String command = """
            INSERT INTO enrollments(student_id, section_id, e_status, registered_when)
            VALUES (?, ?, 'REGISTERED', NOW())
        """;
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setLong(1, student_id);
            ps.setLong(2, section_id);
            ps.executeUpdate();
        }
    }

    //removing a student if they have dropped from the course's section
    public static void removeStudentEnrollment(long student_id, long section_id) throws SQLException{
        String command = """
            UPDATE enrollments SET e_status='DROPPED', dropped_when=NOW()
            WHERE student_id=? AND section_id=? AND e_status='REGISTERED'
        """;
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setLong(1, student_id);
            ps.setLong(2, section_id);
            ps.executeUpdate();
        }
    }

    //if a student has completed the course in a section
    //this info will be used by the instructor or admin later
    public void markCourseComplete(long student_id, long section_id) throws SQLException{
        String command = """
            UPDATE enrollments SET e_status='COMPLETED', completed_when=NOW()
            WHERE student_id=? AND section_id=? AND e_status='REGISTERED'
        """;
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setLong(1, student_id);
            ps.setLong(2, section_id);
            ps.executeUpdate();
        }
    }

    //listing all the students that have been enrolled in the section so far
    public List<Enrollment> listEnrolledStudents(long student_id) throws SQLException {
        String command = """
            SELECT enrollment_id, student_id, section_id, e_status,
            registered_when, dropped_when, completed_when
            FROM enrollments
            WHERE student_id=?
            ORDER BY registered_when DESC
        """;

        List<Enrollment> enrollments = new ArrayList<>();
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setLong(1, student_id);
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

    //listing all actively registered sections of a student
    public List<Section> listRegisteredSections(long student_id) throws SQLException {
        String sql = """
            SELECT s.section_id, s.course_code, s.instructor_id, s.instructor_name,
                   s.day, s.timings, s.classroom,
                   s.capacity, s.sem_no, s.sem_season, s.year
            FROM enrollments e
            JOIN sections s ON e.section_id = s.section_id
            WHERE e.student_id = ? AND e.e_status = 'REGISTERED'
            ORDER BY s.course_code, s.section_id
        """;

        List<Section> registered_sections = new ArrayList<>();

        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, student_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    registered_sections.add(new Section(
                            rs.getLong("section_id"),
                            rs.getString("course_code"),
                            rs.getLong("instructor_id"),
                            rs.getString("instructor_name"),
                            rs.getString("day"),
                            rs.getString("timings"),
                            rs.getString("classroom"),
                            rs.getInt("capacity"),
                            rs.getInt("sem_no"),
                            rs.getString("sem_season"),
                            rs.getInt("year")
                    ));
                }
            }
        }
        return registered_sections;
    }
}
