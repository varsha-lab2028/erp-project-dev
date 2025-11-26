package edu.univ.backend.controller;

import edu.univ.erp.service.InstructorService;
import edu.univ.erp.domain.Section;
import edu.univ.erp.domain.Enrollment;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller for instructor dashboard backend operations.
 * Acts as a bridge between the UI and InstructorService.
 */
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    public InstructorController() {
        this.instructorService = new InstructorService();
    }

    public List<Section> getSectionsByInstructor(long instructorId) throws SQLException {
        return instructorService.getSectionsByInstructor(instructorId);
    }

    public List<Enrollment> getEnrolledStudentsForSection(long sectionId) throws SQLException {
        return instructorService.getEnrolledStudentsForSection(sectionId);
    }

    // Additional methods for instructor-specific functionalities, e.g., grade management, etc.
}
