package edu.univ.erp.service;

import edu.univ.erp.data.*;
import edu.univ.erp.domain.*;
import java.sql.SQLException;
import java.util.List;

/**
 * InstructorService acts as a bridge between instructor-related DB operations and the UI.
 */
public class InstructorService {
    private final SectionDAO sectionDAO = new SectionDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final GradeDAO2 gradeDAO = new GradeDAO2();

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

    // Additional instructor business logic methods can go here
}
