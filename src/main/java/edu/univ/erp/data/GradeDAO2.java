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
            SELECT a.enrollment_id,
                   SUM(a.ass_score * gc.weightage / 100.0) AS final_score
            FROM assessment_scores a
            JOIN enrollments e
              ON e.enrollment_id = a.enrollment_id
            JOIN grade_components gc
              ON gc.section_id = e.section_id
             AND gc.assessment_name = a.assessment_name
            WHERE e.section_id = ? AND e.e_status = 'REGISTERED'
            GROUP BY a.enrollment_id
            """;

        //inserting into the final_grades table
        String upsert_command = """
            INSERT INTO final_grades (enrollment_id, grade_letter, course_cg)
            VALUES (?, ?, ?)
            ON CONFLICT (enrollment_id)
            DO UPDATE SET grade_letter = EXCLUDED.grade_letter,
                          course_cg    = EXCLUDED.course_cg
            """;

        try (Connection conn = ServerConnector.ERPConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement selectPs = conn.prepareStatement(aggregate_command);
                 PreparedStatement upsertPs = conn.prepareStatement(upsert_command)) {

                //computing the final score per enrollment
                selectPs.setLong(1, section_id);

                try (ResultSet rs = selectPs.executeQuery()) {
                    while (rs.next()) {
                        long enrollmentId = rs.getLong("enrollment_id");
                        double finalScore = rs.getDouble("final_score");

                        //mapping score to letter grade
                        String letter   = mapToLetterGrade(finalScore);
                        double courseCg = finalScore / 10.0;

                        //upserting into final_grades table
                        upsertPs.setLong(1, enrollmentId);
                        upsertPs.setString(2, letter);
                        upsertPs.setDouble(3, courseCg);
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
            SELECT s.section_id, fg.enrollment_id, fg.grade_letter, fg.course_cg
            FROM enrollments e
            JOIN sections s      ON e.section_id = s.section_id
            JOIN final_grades fg ON fg.enrollment_id = e.enrollment_id
            WHERE e.student_id = ?
              AND e.e_status = 'COMPLETED'
              AND s.sem_no = ?
              AND s.sem_season = ?
              AND s.year = ?
            ORDER BY s.course_code
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
                            0L,   // courses have no numeric id; course_code is the key
                            rs.getLong("section_id"),
                            rs.getLong("enrollment_id"),
                            LetterGrade.fromLabel(rs.getString("grade_letter")),
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
        // finds the student's enrollment in this section and saves the score against it;
        // if a score for that assessment already exists, it is replaced
        String sql = """
            INSERT INTO assessment_scores (enrollment_id, assessment_name, ass_score)
            SELECT e.enrollment_id, ?, ?
            FROM enrollments e
            WHERE e.section_id = ? AND e.student_id = ? AND e.e_status = 'REGISTERED'
            ON CONFLICT (enrollment_id, assessment_name)
            DO UPDATE SET ass_score = EXCLUDED.ass_score
            """;
        try (Connection conn = ServerConnector.ERPConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, assessmentName);
            ps.setDouble(2, score);
            ps.setLong(3, sectionId);
            ps.setLong(4, studentId);
            if (ps.executeUpdate() == 0) {
                throw new IllegalStateException("Student is not registered in this section.");
            }
        }
    }
}


