/*this DAO contains the info of the timetable of a particular student. Based on
* the courses, section the student has been enrolled in.*/

package edu.univ.erp.data;

import edu.univ.erp.domain.TimeTableRow;
import java.sql.*;
import java.util.*;

public class TimeTableDAO {
    public List<TimeTableRow> getTimetableForStudent(long student_id) throws SQLException {
        String command = """
        SELECT s.day, s.timings, c.course_code, c.name, s.classroom
        FROM enrollments e
        JOIN sections s ON e.section_id = s.id
        JOIN courses  c ON s.course_code = c.code
        WHERE e.student_id = ?
        """;

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
                            rs.getString("code"),
                            rs.getString("title"),
                            rs.getString("classroom")
                    ));
                }
            }
        }
        return rows;
    }
}
