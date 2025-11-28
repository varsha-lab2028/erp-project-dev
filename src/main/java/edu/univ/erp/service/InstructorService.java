package edu.univ.erp.service;

import edu.univ.erp.data.*;
import edu.univ.erp.domain.*;
import edu.univ.erp.access.AccessControl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// InstructorService acts as a bridge between instructor-related DB operations and the UI.
public class InstructorService {
    private final SectionDAO sectionDAO = new SectionDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final GradeDAO2 gradeDAO = new GradeDAO2();

    // 1. READ OPERATIONS
    
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

    // --- HELPER: Security Check ---
    // Makes sure an instructor doesn't tamper with a section they are not teaching
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
                return rs.next(); // Returns true if a match is found
            }
        }
    }

    // 2. WRITE OPERATIONS

    // Update weightages (e.g., Midsem = 30%)
    public void updateComponentScores(long section_id, long instructor_id, String assessment_name, int new_weightage) throws SQLException {
        // 1. Check Role & Maintenance
        AccessControl.checkRole("INSTRUCTOR");
        AccessControl.checkWritable();

        // 2. Check Ownership
        if (!sectionBelongsToInstructor(instructor_id, section_id)) {
            throw new SecurityException("Access Denied: You are not the instructor for this section.");
        }
        
        // 3. Validation
        if (new_weightage < 0 || new_weightage > 100) {
            throw new IllegalArgumentException("Weightage must be between 0 and 100.");
        }

        String command = """
                UPDATE grade_components
                SET weightage = ?
                WHERE section_id = ?
                AND assessment_name = ?
                """;

        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setInt(1, new_weightage);
            ps.setLong(2, section_id);
            ps.setString(3, assessment_name);

            // Note: We don't check instructor_id in SQL here because we already validated ownership above
            // and grade_components table might not store instructor_id directly (it links via section)
            ps.executeUpdate();
        }
    }

    // Enter/Update a specific student's score
    public void updateScore(long sectionId, long instructorId, long studentId, String assessmentName, double score) throws SQLException {
        // 1. Check Role & Maintenance
        AccessControl.checkRole("INSTRUCTOR");
        AccessControl.checkWritable();

        // 2. Check Ownership
        if (!sectionBelongsToInstructor(instructorId, sectionId)) {
            throw new SecurityException("Access Denied: You are not the instructor for this section.");
        }
        
        // 3. Edge Case: Negative Score Check
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative.");
        }

        gradeDAO.upsertComponentScore(sectionId, studentId, assessmentName, score);
    }

    // Calculate Final Grades
    public void computeFinalGrades(long section_id, long instructor_id) throws Exception {
        // 1. Check Role & Maintenance
        AccessControl.checkRole("INSTRUCTOR");
        AccessControl.checkWritable();

        // 2. Check Ownership
        if (!sectionBelongsToInstructor(instructor_id, section_id)) {
            throw new SecurityException("Access Denied: You are not the instructor for this section.");
        }

        gradeDAO.calculateFinalGrades(section_id);
    }
}