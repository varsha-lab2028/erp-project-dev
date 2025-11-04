package edu.univ.erp.data;

import edu.univ.erp.domain.DayOfTheWeek;
import edu.univ.erp.domain.Section;
import java.sql.*;
import java.util.*;

public class SectionDAO {
    public List<Section> listByCourse(String courseCode) throws SQLException {
        String sql = """
            SELECT section_id, course_code, instructor_userid, day, timings, classroom,
                   capacity, semester, year
            FROM sections
            WHERE course_code = ?
        """;

        List<Section> sections = new ArrayList<>();
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseCode);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sections.add(new Section(
                            rs.getLong("section_id"),
                            rs.getString("course_code"),
                            rs.getLong("instructor_id"),
                            DayOfTheWeek.valueOf(rs.getString("day").toUpperCase()),
                            rs.getString("timings"),
                            rs.getString("classroom"),
                            rs.getInt("capacity"),
                            rs.getString("semester"),
                            rs.getInt("year")
                    ));
                }
            }
        }
        return sections;
    }
}
