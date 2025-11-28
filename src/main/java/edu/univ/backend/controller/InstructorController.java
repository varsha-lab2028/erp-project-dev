package edu.univ.backend.controller;

import edu.univ.erp.service.InstructorService;
import edu.univ.erp.domain.Section;
import edu.univ.erp.domain.Enrollment;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    public List<Section> getSectionsByInstructor(long instructorId) throws SQLException {
        return instructorService.getSectionsByInstructor(instructorId);
    }

    public List<Enrollment> getEnrolledStudentsForSection(long sectionId) throws SQLException {
        return instructorService.getEnrolledStudentsForSection(sectionId);
    }

    public Map<String, Integer> getDashboardStats(long instructorId) throws SQLException {
        Map<String, Integer> stats = new HashMap<>();

        List<Section> sections = instructorService.getSectionsByInstructor(instructorId);
        stats.put("sections", sections.size());

        int totalStudents = 0;
        for (Section section : sections) {
            try {
                List<Enrollment> enrollments = instructorService.getEnrolledStudentsForSection(section.getSectionId());
                totalStudents += enrollments.size();
            } catch (SQLException ignored) {

            }
        }
        stats.put("students", totalStudents);

        stats.put("pending", 0); 

        return stats;
    }
}