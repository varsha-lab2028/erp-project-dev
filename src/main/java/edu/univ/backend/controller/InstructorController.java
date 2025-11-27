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

    /**
     * Aggregates statistics for the dashboard.
     * @param instructorId The ID of the logged-in instructor.
     * @return A map containing stats: "sections", "students", "pending".
     */
    public Map<String, Integer> getDashboardStats(long instructorId) throws SQLException {
        Map<String, Integer> stats = new HashMap<>();
        
        // 1. Get Sections Count
        List<Section> sections = instructorService.getSectionsByInstructor(instructorId);
        stats.put("sections", sections.size());

        // 2. Get Total Students (Sum of enrollments across all sections)
        int totalStudents = 0;
        for (Section section : sections) {
            try {
                List<Enrollment> enrollments = instructorService.getEnrolledStudentsForSection(section.getSectionId());
                totalStudents += enrollments.size();
            } catch (SQLException ignored) {
                // Continue if one section fails
            }
        }
        stats.put("students", totalStudents);

        // 3. Pending Grades (Placeholder logic: usually requires checking null grades in DB)
        // For now, we assume a method exists or return a mock value based on student count
        // In a real scenario: instructorService.countPendingGrades(instructorId)
        stats.put("pending", 0); 

        return stats;
    }
}