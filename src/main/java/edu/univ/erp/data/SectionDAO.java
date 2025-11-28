package edu.univ.erp.data;

import edu.univ.erp.domain.Section;
import java.sql.*;
import java.util.*;

public class SectionDAO {
    private static final String BASE_QUERY = """
        SELECT s.section_id, s.course_code, s.instructor_id, i.name as instructor_name,
               s.day, s.timings, s.classroom, s.capacity, 
               s.sem_no, s.sem_season, s.year
        FROM sections s
        JOIN instructors i ON s.instructor_id = i.instructor_id
    """;

    //all the required methods for section showing
    public List<Section> listAllSections() throws SQLException {
        String command = BASE_QUERY + " ORDER BY s.course_code, s.section_id";
        
        List<Section> sections = new ArrayList<>();
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(command)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sections.add(mapRowToSection(rs));
                }
            }
        }
        return sections;
    }

    public int capacityOfSection(long sectionId) throws SQLException {
        String command = "SELECT capacity FROM sections WHERE section_id = ?";
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setLong(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("capacity");
                }
                return 0;
            }
        }
    }

    public List<Section> searchSection(String keyword) throws SQLException {
        String command = BASE_QUERY + " WHERE s.course_code LIKE ? ORDER BY s.course_code, s.section_id";
        
        List<Section> searched_section = new ArrayList<>();
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(command)) {
            
            String like = "%" + keyword + "%";
            ps.setString(1, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    searched_section.add(mapRowToSection(rs));
                }
            }
        }
        return searched_section;
    }

    public Section findBySectionId(long section_id) throws SQLException {
        String sql = BASE_QUERY + " WHERE s.section_id = ?";

        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, section_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToSection(rs);
                }
                return null;
            }
        }
    }

    public List<Section> listSectionsByInstructor(long instructorId) throws SQLException {
        String command = BASE_QUERY + " WHERE s.instructor_id = ? ORDER BY s.course_code, s.section_id";

        List<Section> sections = new ArrayList<>();
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(command)) {
            ps.setLong(1, instructorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sections.add(mapRowToSection(rs));
                }
            }
        }
        return sections;
    }

    //admin write methods for section
    public void insertSection(Section section) throws SQLException {
        // FIXED: Column names in INSERT
        String sql = """
            INSERT INTO sections (course_code, instructor_id, classroom, day, timings, 
                                  capacity, sem_no, sem_season, `year`)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, section.getCourseCode());
            ps.setLong(2, section.getInstructorUserID());
            ps.setString(3, section.getClassroom());
            ps.setString(4, section.getDay());
            ps.setString(5, section.getTimings());
            ps.setInt(6, section.getCapacity());
            ps.setInt(7, section.getSemNumber());
            ps.setString(8, section.getSemSeason());
            ps.setInt(9, section.getYear());
            ps.executeUpdate();
        }
    }

    public void updateSection(Section section) throws SQLException {
        String sql = """
            UPDATE sections
            SET course_code = ?, instructor_id = ?, classroom = ?, day = ?, timings = ?,
                capacity = ?, sem_no = ?, sem_season = ?, `year` = ?
            WHERE section_id = ?
        """;
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, section.getCourseCode());
            ps.setLong(2, section.getInstructorUserID());
            ps.setString(3, section.getClassroom());
            ps.setString(4, section.getDay());
            ps.setString(5, section.getTimings());
            ps.setInt(6, section.getCapacity());
            ps.setInt(7, section.getSemNumber());
            ps.setString(8, section.getSemSeason());
            ps.setInt(9, section.getYear());
            ps.setLong(10, section.getSectionId());
            ps.executeUpdate();
        }
    }

    public void deleteSection(long sectionId) throws SQLException {
        String deleteGrades = "DELETE FROM grade_components WHERE section_id = ?";
        String deleteEnrollments = "DELETE FROM enrollments WHERE section_id = ?";
        String deleteSection = "DELETE FROM sections WHERE section_id = ?";

        try (Connection conn = ServerConnector.ERPConnection()) {
            conn.setAutoCommit(false);
            try {
                try(PreparedStatement ps = conn.prepareStatement(deleteGrades)){
                    ps.setLong(1, sectionId);
                    ps.executeUpdate();
                }
                try(PreparedStatement ps = conn.prepareStatement(deleteEnrollments)){
                    ps.setLong(1, sectionId);
                    ps.executeUpdate();
                }
                try(PreparedStatement ps = conn.prepareStatement(deleteSection)){
                    ps.setLong(1, sectionId);
                    ps.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private Section mapRowToSection(ResultSet rs) throws SQLException {
        return new Section(
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
        );
    }
}