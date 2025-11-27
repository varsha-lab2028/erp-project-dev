package edu.univ.erp.service;

import edu.univ.erp.data.*;
import edu.univ.erp.domain.*;
import edu.univ.erp.access.AccessControl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//InstructorService acts as a bridge between instructor-related DB operations and the UI.
public class InstructorService {
    private final SectionDAO sectionDAO = new SectionDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final GradeDAO2 gradeDAO = new GradeDAO2();
    //private final MaintenanceService maintenanceService = new MaintenanceService();

    // List sections assigned to the instructor by instructorId
    public List<Section> getSectionsByInstructor(long instructorId) throws SQLException {
        return sectionDAO.listSectionsByInstructor(instructorId);
    }

    // Get enrolled students for a specific section
    public List<Enrollment> getEnrolledStudentsForSection(long sectionId) throws SQLException {
        return enrollmentDAO.listEnrolledStudentsBySection(sectionId);
    }

    // Get grades for students in a section
    public List<Object[]> getGradesForSection(long sectionId) throws SQLException {
        return gradeDAO.listGradesBySection(sectionId);
    }

    //making sure an instructor doesn't tamper with a section they are not teaching
    private boolean sectionBelongsToInstructor(long instructor_id, long section_id) throws SQLException {
        String command = """
        SELECT 1
        FROM sections
        WHERE section_id = ? AND instructor_id = ?
        LIMIT 1
    """;

        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(command)) {

            ps.setLong(1, section_id);
            ps.setLong(2, instructor_id);

            try (ResultSet rs = ps.executeQuery()) {
                //this will work only if a section belongs to that instructor
                return rs.next();
            }
        }
    }

    //saving or updating quiz, assignment, midsem, endsem scores
    public void updateComponentScores(long section_id, long instructor_id, String assessment_name, int new_weightage) throws SQLException {
        //for maintenance mode
        AccessControl.checkRole("INSTRUCTOR");
        AccessControl.checkWritable();

        if (!sectionBelongsToInstructor(instructor_id, section_id)) {
            throw new IllegalStateException("This is not your section.");
        }

        String command = """
                UPDATE grade_components
                        SET weightage = ?
                        WHERE section_id = ?
                        AND instructor_id = ?
                        AND assessment_name = ?
                """;

        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setInt(1, new_weightage);
            ps.setLong(2, section_id);
            ps.setLong(3, instructor_id);
            ps.setString(4, assessment_name);

            if (ps.executeUpdate() == 0) {
                throw new SQLException(
                        "No grade component found for section=" + section_id +
                                ", instructor=" + instructor_id +
                                ", assessment='" + assessment_name + "'"
                );
            }
        }
    }

    //saving or updating quiz, assignment, midsem, endsem scores
    public void updateScore(long sectionId, long instructorId, long studentId, String assessmentName, double score) throws SQLException {
        AccessControl.checkRole("INSTRUCTOR");
        AccessControl.checkWritable();

        if (!sectionBelongsToInstructor(instructorId, sectionId)) {
            throw new IllegalStateException("This is not your section.");
        }

        gradeDAO.upsertComponentScore(sectionId, studentId, assessmentName, score);
    }

    //calculating the final grade using weightage
    public void computeFinalGrades(long section_id, long instructor_id) throws Exception {
        //for maintenance
        AccessControl.checkRole("INSTRUCTOR");
        AccessControl.checkWritable();

        if (!sectionBelongsToInstructor(instructor_id, section_id)) {
            throw new IllegalStateException("This is not your section.");
        }

        gradeDAO.calculateFinalGrades(section_id);
    }

    // Get section statistics
    public SectionStats getSectionStats(long sectionId, long instructorId) throws SQLException {
        AccessControl.checkRole("INSTRUCTOR");

        if (!sectionBelongsToInstructor(instructorId, sectionId)) {
            throw new IllegalStateException("This is not your section.");
        }

        return gradeDAO.getSectionStats(sectionId);
    }

    //if an instructor wants to add a component in the course
    //if an instructor wants to remove a component in the course
}

