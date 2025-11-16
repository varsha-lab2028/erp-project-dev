package edu.univ.erp.data;

import edu.univ.erp.domain.DayOfTheWeek;
import edu.univ.erp.domain.Section;
import edu.univ.erp.domain.SemesterSeason;

import java.sql.*;
import java.util.*;

public class SectionDAO {
    //fetches all sections for a given course
    public List<Section> listAllSections() throws SQLException {
        String command = """
        SELECT section_id, course_code, instructor_id, day, timings, classroom,
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
                            DayOfTheWeek.valueOf(rs.getString("day").toUpperCase()),
                            rs.getString("timings"),
                            rs.getString("classroom"),
                            rs.getInt("capacity"),
                            rs.getInt("sem_no"),
                            SemesterSeason.valueOf(rs.getString("sem_season").toUpperCase()),
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
        String sql = """
        SELECT s.section_id, s.course_code, s.instructor_id, s.day, s.timings, s.classroom,
               s.capacity, s.sem_no, s.sem_season, s.year
        FROM sections s
        JOIN instructors i ON s.instructor_id = i.instructor_id
        WHERE s.course_code LIKE ? OR i.name LIKE ?
        ORDER BY s.course_code
    """;
        List<Section> searched_section = new ArrayList<>();
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    searched_section.add(new Section(
                            rs.getLong("section_id"),
                            rs.getString("course_code"),
                            rs.getLong("instructor_id"),
                            rs.getString("instructor_name"),
                            DayOfTheWeek.valueOf(rs.getString("day").toUpperCase()),
                            rs.getString("timings"),
                            rs.getString("classroom"),
                            rs.getInt("capacity"),
                            rs.getInt("sem_no"),
                            SemesterSeason.valueOf(rs.getString("sem_season").toUpperCase()),
                            rs.getInt("year")
                    ));
                }
            }
        }
        return searched_section;
    }
}
