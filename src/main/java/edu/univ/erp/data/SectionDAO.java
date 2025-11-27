package edu.univ.erp.data;

import edu.univ.erp.domain.Section;
//import edu.univ.erp.domain.SemesterSeason;
import java.sql.*;
import java.util.*;

public class SectionDAO {
    //fetches all sections for a given course
    public List<Section> listAllSections() throws SQLException {
        String command = """
        SELECT section_id, course_code, instructor_id, instructor_name, day, timings, classroom,
               capacity, sem_no, sem_season, year
        FROM sections
        ORDER BY course_code, section_id
    """;
        List<Section> sections = new ArrayList<>();
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(command)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sections.add(new Section(
                            rs.getLong("section_id"),
                            rs.getString("course_code"),
                            rs.getLong("instructor_id"),
                            rs.getString("instructor_name"),
                            rs.getString("day").toUpperCase(),
                            rs.getString("timings"),
                            rs.getString("classroom"),
                            rs.getInt("capacity"),
                            rs.getInt("sem_no"),
                            rs.getString("sem_season").toUpperCase(),
                            rs.getInt("year")
                    ));
                }
            }
        }
        return sections;
    }

    //get the capacity, maximum number of students that can be a section
    public int capacityOfSection(long sectionId) throws SQLException {
        String command = "SELECT capacity FROM sections WHERE section_id = ?";
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setLong(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("capacity");
                } else {
                    // no section found
                    return 0;
                }
            }
        }
    }

    //searching a section
    public List<Section> searchSection(String keyword) throws SQLException {
        String command = """
        SELECT s.section_id, s.course_code, s.instructor_id, s.instructor_name, s.day, s.timings, s.classroom,
               s.capacity, s.sem_no, s.sem_season, s.year
        FROM sections s
        WHERE s.course_code LIKE ? ORDER BY s.course_code, s.section_id
    """;
        List<Section> searched_section = new ArrayList<>();
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(command)) {

            //searching by just the course code
            String like = "%" + keyword + "%";
            ps.setString(1, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    searched_section.add(new Section(
                            rs.getLong("section_id"),
                            rs.getString("course_code"),
                            rs.getLong("instructor_id"),
                            rs.getString("instructor_name"),
                            rs.getString("day").toUpperCase(),
                            rs.getString("timings"),
                            rs.getString("classroom"),
                            rs.getInt("capacity"),
                            rs.getInt("sem_no"),
                            rs.getString("sem_season").toUpperCase(),
                            rs.getInt("year")
                    ));
                }
            }
        }
        return searched_section;
    }

    //finding the section id
    public Section findBySectionId(long section_id) throws SQLException {
        String sql = """
        SELECT section_id, course_code, instructor_id, instructor_name, day, timings, classroom,
               capacity, sem_no, sem_season, year FROM sections WHERE section_id = ? """;

        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, section_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
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
                return null;
            }
        }
    }

    // fetches all sections assigned to a specific instructor
    public List<Section> listSectionsByInstructor(long instructorId) throws SQLException {
        String command = """
        SELECT section_id, course_code, instructor_id, instructor_name, day, timings, classroom,
               capacity, sem_no, sem_season, year
        FROM sections
        WHERE instructor_id = ?
        ORDER BY course_code, section_id
        """;

        List<Section> sections = new ArrayList<>();
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(command)) {
            ps.setLong(1, instructorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sections.add(new Section(
                            rs.getLong("section_id"),
                            rs.getString("course_code"),
                            rs.getLong("instructor_id"),
                            rs.getString("instructor_name"),
                            rs.getString("day").toUpperCase(),
                            rs.getString("timings"),
                            rs.getString("classroom"),
                            rs.getInt("capacity"),
                            rs.getInt("sem_no"),
                            rs.getString("sem_season").toUpperCase(),
                            rs.getInt("year")
                    ));
                }
            }
        }
        return sections;
    }

    // --- ADMIN: INSERT new section ---
    public void insertSection(Section section) throws SQLException {
        String sql = """
            INSERT INTO sections (course_code, instructor_id, instructor_name, day, timings, classroom,
                                  capacity, sem_no, sem_season, year)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, section.getCourseCode());
            ps.setLong(2, section.getInstructorUserID());
            ps.setString(3, section.getInstructorName());
            ps.setString(4, section.getDay());
            ps.setString(5, section.getTimings());
            ps.setString(6, section.getClassroom());
            ps.setInt(7, section.getCapacity());
            ps.setInt(8, section.getSemNumber());
            ps.setString(9, section.getSemSeason());
            ps.setInt(10, section.getYear());
            ps.executeUpdate();
        }
    }

    // --- ADMIN: UPDATE existing section ---
    public void updateSection(Section section) throws SQLException {
        String sql = """
            UPDATE sections
            SET course_code = ?, instructor_id = ?, instructor_name = ?, day = ?, timings = ?,
                classroom = ?, capacity = ?, sem_no = ?, sem_season = ?, year = ?
            WHERE section_id = ?
        """;
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, section.getCourseCode());
            ps.setLong(2, section.getInstructorUserID());
            ps.setString(3, section.getInstructorName());
            ps.setString(4, section.getDay());
            ps.setString(5, section.getTimings());
            ps.setString(6, section.getClassroom());
            ps.setInt(7, section.getCapacity());
            ps.setInt(8, section.getSemNumber());
            ps.setString(9, section.getSemSeason());
            ps.setInt(10, section.getYear());
            ps.setLong(11, section.getSectionId());
            ps.executeUpdate();
        }
    }

    // --- ADMIN: DELETE section ---
    public void deleteSection(long sectionId) throws SQLException {
        String sql = "DELETE FROM sections WHERE section_id = ?";
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sectionId);
            ps.executeUpdate();
        }
    }

    // --- ADMIN: ASSIGN instructor to section ---
    public void assignInstructor(long sectionId, long instructorUserId) throws SQLException {
        UserDAO userDAO = new UserDAO();
        //String instructorName = userDAO.findByUserId(instructorUserId).getName();
        String sql = "UPDATE sections SET instructor_id = ?, instructor_name = ? WHERE section_id = ?";
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, instructorUserId);
            //ps.setString(2, instructorName);
            ps.setLong(2, sectionId);
            ps.executeUpdate();
        }
    }
}
