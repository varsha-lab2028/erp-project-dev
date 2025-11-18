package edu.univ.erp.data;

import edu.univ.erp.domain.TimeTableRow;
import java.sql.*;
import java.util.*;

public class TimeTableDAO {
    //this will retrieve the required info regarding the student's courses
    public static List<TimeTableRow> getTimetableForStudent(long student_id) throws SQLException {
        String command = """
        SELECT s.day,s.timings, c.course_code AS course_code,c.name AS name,
            s.classroom FROM enrollments e JOIN sections s ON e.section_id = s.section_id
        JOIN courses c ON s.course_code = c.course_code WHERE e.student_id = ?
        ORDER BY FIELD(s.day, 'MON','TUE','WED','THU','FRI','SAT','SUN'),
            s.timings""";

        //list to store the results
        List<TimeTableRow> rows = new ArrayList<>();

        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setLong(1, student_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new TimeTableRow(
                            rs.getString("day"),
                            rs.getString("timings"),
                            rs.getString("course_code"),
                            rs.getString("name"),
                            rs.getString("classroom")
                    ));
                }
            }
        }
        return rows;
    }
}
