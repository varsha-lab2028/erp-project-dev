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

    //computing the final grades of the student, method is used by the Instructor
    public void calculateFinalGrades(long section_id) throws SQLException{
        String aggregate_command = """
        SELECT
            a.course_id,
            a.section_id,
            a.enrollment_id,
            SUM(a.ass_score * gc.weightage / 100.0) AS final_score
        FROM assessment_scores a
        JOIN grade_components gc
          ON gc.section_id = a.section_id
         AND gc.course_id  = a.course_id
         AND gc.assessment_name = a.assessment_name
        WHERE a.section_id = ?
        GROUP BY a.course_id, a.section_id, a.enrollment_id
    """;

        //inserting into the final_grades table
        String upsert_command = """
        INSERT INTO final_grades
            (course_id, section_id, enrollment_id, grade_letter, course_cg)
        VALUES (?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE
            grade_letter = VALUES(grade_letter),
            course_cg    = VALUES(course_cg)
    """;

        try (Connection conn = ServerConnector.ERPConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement selectPs = conn.prepareStatement(aggregate_command);
                 PreparedStatement upsertPs = conn.prepareStatement(upsert_command)) {

                //computing the final score per enrollment
                selectPs.setLong(1, section_id);

                try (ResultSet rs = selectPs.executeQuery()) {
                    while (rs.next()) {
                        long courseId     = rs.getLong("course_id");
                        long secId        = rs.getLong("section_id");
                        long enrollmentId = rs.getLong("enrollment_id");
                        double finalScore = rs.getDouble("final_score");

                        //mapping score to letter grade
                        String letter   = mapToLetterGrade(finalScore);
                        double courseCg = finalScore / 10.0;

                        //upserting into final_grades table
                        upsertPs.setLong(1, courseId);
                        upsertPs.setLong(2, secId);
                        upsertPs.setLong(3, enrollmentId);
                        upsertPs.setString(4, letter);
                        upsertPs.setDouble(5, courseCg);
                        upsertPs.addBatch();
                    }
                }

                upsertPs.executeBatch();
                conn.commit();

            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    //mapping the final score to a letter grade
    private String mapToLetterGrade(double score) {
        if (score >= 90) return "A+";
        if (score >= 80) return "A";
        if (score >= 75) return "A-";
        if (score >= 70) return "B+";
        if (score >= 65) return "B";
        if (score >= 60) return "B-";
        if (score >= 55) return "C";
        if (score >= 50) return "C-";
        if (score >= 45) return "D";
        if (score >= 40) return "D-";
        return "F";
    }

    //fetches the final grades of the student, used in Student UI
    public static List<FinalGrade> listFinalGrades(long student_id, int sem_no, String sem_season, int year) throws SQLException {
        String command = """
            SELECT
                                fg.course_id,
                                fg.section_id,
                                fg.enrollment_id,
                                fg.grade_letter,
                                fg.course_cg,
                                c.course_code,
                                c.name AS course_name,
                                c.credits
                            FROM enrollments e
                            JOIN sections s ON e.section_id = s.section_id
                            JOIN courses  c ON s.course_code = c.course_code
                            JOIN final_grades fg ON fg.enrollment_id = e.enrollment_id
                            WHERE e.student_id = ?
                              AND e.e_status='COMPLETED'
                              AND s.sem_no = ?
                              AND s.sem_season = ?
                              AND s.year = ?
                            ORDER BY c.course_code
        """;

        List<FinalGrade> final_grades = new ArrayList<>();
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
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

    //listing grades by the section
    public List<Object[]> listGradesBySection(long section_id) throws SQLException {
        String command = """
            SELECT
                e.student_id,
                st.roll_no AS student_name,

                MAX(CASE WHEN a.assessment_name = 'Midsem'      THEN a.ass_score END) AS midsem_score,
                MAX(CASE WHEN a.assessment_name = 'Endsem'      THEN a.ass_score END) AS endsem_score,
                MAX(CASE WHEN a.assessment_name = 'Assignments' THEN a.ass_score END) AS assignments_score,
                MAX(CASE WHEN a.assessment_name = 'Quizzes'     THEN a.ass_score END) AS quizzes_score,

                fg.course_cg    AS final_score,
                fg.grade_letter AS grade_letter
            FROM enrollments e
            JOIN students st
              ON e.student_id = st.user_id
            LEFT JOIN assessment_scores a
              ON a.enrollment_id = e.enrollment_id
            LEFT JOIN final_grades fg
              ON fg.enrollment_id = e.enrollment_id
            WHERE e.section_id = ?
              AND e.e_status = 'REGISTERED'
            GROUP BY
                e.student_id,
                st.roll_no,
                fg.course_cg,
                fg.grade_letter
            ORDER BY st.roll_no
            """;

        List<Object[]> grades = new ArrayList<>();

        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setLong(1, section_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    grades.add(new Object[]{
                            rs.getLong("student_id"),
                            rs.getString("student_name"),
                            rs.getObject("midsem_score"),
                            rs.getObject("endsem_score"),
                            rs.getObject("assignments_score"),
                            rs.getObject("quizzes_score"),
                            rs.getObject("final_score"),
                            rs.getString("grade_letter")
                    });
                }
            }
        }
        return grades;
    }

    public void upsertComponentScore(long sectionId, long studentId, String assessmentName, double score) throws SQLException {
        String sql = """
            INSERT INTO assessment_scores (section_id, student_id, assessment_name, score)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE score = VALUES(score)
            """;
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sectionId);
            ps.setLong(2, studentId);
            ps.setString(3, assessmentName);
            ps.setDouble(4, score);
            ps.executeUpdate();
        }
    }
}


