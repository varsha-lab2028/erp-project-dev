package edu.univ.erp.data;

import edu.univ.erp.domain.GradeComponent;
import edu.univ.erp.domain.FinalGrade;
import edu.univ.erp.domain.LetterGrade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO2 {
    //fetches all the grade components of the courses a student has registered for during a semester
    public static List<Object[]> listGradeComponents(long student_id, int sem_no, String sem_season, int year) throws SQLException {
        String command = """
                SELECT c.course_code AS course_code,
                c.name AS  course_name,
                c.credits AS credits,
                MAX(CASE WHEN gc.assessment_name = 'Quizzes' THEN gc.weightage END) AS quiz_pct,
                 MAX(CASE WHEN gc.assessment_name = 'Assignments' THEN gc.weightage END) AS assignments_pct,
                 MAX(CASE WHEN gc.assessment_name = 'Midsem' THEN gc.weightage END) AS midsem_pct,
                 MAX(CASE WHEN gc.assessment_name = 'Endsem' THEN gc.weightage END) AS endsem_pct 
                 FROM enrollments e 
                 JOIN sections s ON e.section_id = s.section_id
                 JOIN courses c ON s.course_code = c.course_code
                 JOIN grade_components gc ON gc.section_id = s.section_id
                 WHERE e.student_id = ? AND e.e_status = 'REGISTERED'
                 AND s.sem_no = ?
                 AND s.sem_season = ?
                 AND s.year = ?
                 GROUP BY c.course_code, c.name, c.credits ORDER BY c.course_code
                """;

        List<Object[]> components = new ArrayList<>();
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setLong(1, student_id);
            ps.setInt(2, sem_no);
            ps.setString(3, sem_season);
            ps.setInt(4, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    components.add(new Object[]{
                            rs.getString("course_code"),
                            rs.getString("course_name"),
                            rs.getInt("credits"),
                            rs.getInt("quiz_pct"),
                            rs.getInt("assignments_pct"),
                            rs.getInt("midsem_pct"),
                            rs.getInt("endsem_pct")
                    });
                }
            }
        }
        return components;
    }

    //fetches the final grades of the student
    public static List<FinalGrade> listFinalGrades(long student_id, int sem_no, String sem_season, int year) throws SQLException {
        String sql = """
            SELECT fg.final_score, fg.letter_grade,
                   c.course_code, c.name AS course_name, c.credits
            FROM enrollments e
            JOIN sections s ON e.section_id = s.section_id
            JOIN courses  c ON s.course_code = c.course_code
            JOIN final_grades fg ON fg.enrollment_id = e.enrollment_id
            WHERE e.student_id = ? AND e.e_status='COMPLETED'
              AND s.sem_no = ? AND s.sem_season = ? AND s.year = ?
            ORDER BY c.course_code
        """;

        List<FinalGrade> final_grades = new ArrayList<>();
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, student_id);
            ps.setInt(2, sem_no);
            ps.setString(3, sem_season);
            ps.setInt(4, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    final_grades.add(new FinalGrade(
                            rs.getLong("course_id"),
                            rs.getLong("section_id"),
                            rs.getLong("enrollment_id"),
                            LetterGrade.valueOf(rs.getString("letter_grade").toUpperCase()),
                            rs.getDouble("course_cg")
                    ));
                }
            }
        }
        return final_grades;
    }
}


