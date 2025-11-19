package edu.univ.erp.data;

import edu.univ.erp.domain.GradeComponent;
import edu.univ.erp.domain.FinalGrade;
import edu.univ.erp.domain.LetterGrade;

import java.sql.*;
import java.util.*;

public class GradeDAO {
    /*fetches all the grade components of the courses a student has registered
    *for during a semester*/
    public static List<GradeComponent> listGradeComponents(long studentId, int semNo, String semSeason, int year) throws SQLException {
        String sql = """
            SELECT gc.component_name, gc.weight_percent, gc.score,
                   c.course_code, c.name AS course_name, s.section_id
            FROM enrollments e
            JOIN sections s ON e.section_id = s.section_id
            JOIN courses  c ON s.course_code = c.course_code
            JOIN grade_components gc ON gc.enrollment_id = e.enrollment_id
            WHERE e.student_id = ? AND s.sem_no = ? AND s.sem_season = ? AND s.year = ?
            ORDER BY c.course_code, gc.component_name
        """;

        List<GradeComponent> components = new ArrayList<>();
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, studentId);
            ps.setInt(2, semNo);
            ps.setString(3, semSeason);
            ps.setInt(4, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    components.add(new GradeComponent(
                            rs.getLong("course_id"),
                            rs.getLong("section_id"),
                            rs.getLong("instructor_id"),
                            rs.getString("assessment_name"),
                            rs.getInt("weightage")
                    ));
                }
            }
        }
        return components;
    }

    //fetches the final grades of the student
    public static List<FinalGrade> listFinalGrades(long studentId, int semNo, String semSeason, int year) throws SQLException {
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
            ps.setLong(1, studentId);
            ps.setInt(2, semNo);
            ps.setString(3, semSeason);
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

