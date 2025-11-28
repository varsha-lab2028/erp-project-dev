package edu.univ.erp.data;

import edu.univ.erp.domain.TranscriptRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TranscriptDAO {
    //transcript for student
    public List<Object[]> listCurrentRegistrations(long student_id) throws SQLException{
        String command = """
                SELECT c.course_code AS course_code,
                       c.name AS course_name,
                       c.credits AS credits,
                       s.sem_season AS sem_season,
                       s.sem_no AS sem_no,
                       s.year AS year
                       FROM enrollments e
                       JOIN sections s ON e.section_id = s.section_id
                       JOIN courses c ON s.course_code  = c.course_code
                       WHERE e.student_id = ? ORDER BY c.course_code """;

        List<Object[]> rows = new ArrayList<>();
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(command)) {
            ps.setLong(1, student_id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[] {
                            rs.getString("course_code"),
                            rs.getString("course_name"),
                            rs.getInt("credits"),
                            rs.getString("sem_season"),
                            rs.getInt("sem_no"),
                            rs.getInt("year")
                    });
                }
            }
        }
        return rows;
    }

    public List<TranscriptRow> fetchTranscript(long studentUserId) throws SQLException {
        String sql = """
        SELECT
            c.course_code       AS course_code,
            c.name              AS course_title,
            c.credits           AS credits,
            fg.grade_letter     AS final_grade
        FROM enrollments e
        JOIN sections s        ON e.section_id = s.section_id
        JOIN courses  c        ON s.course_code = c.course_code
        LEFT JOIN final_grades fg ON fg.enrollment_id = e.enrollment_id
        WHERE e.student_id = ?
          AND e.e_status = 'COMPLETED'
        ORDER BY c.course_code
        """;


        List<TranscriptRow> rows = new ArrayList<>();
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, studentUserId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new TranscriptRow(
                        rs.getString("course_code"),
                        rs.getString("course_title"),
                        rs.getInt("credits"),
                        rs.getString("final_grade")
                    ));
                }
            }
        }
        return rows;
    }
}
