package edu.univ.erp.data;

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
}
