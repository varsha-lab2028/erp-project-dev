package edu.univ.erp.data;

import edu.univ.erp.data.ServerConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeedingGradeInfo {
    private static final String[][] grade_components = {
            {"Quizzes", "20"},
            {"Assignments", "20"},
            {"Midsem",  "30"},
            {"Endsem",  "30"}
    };

    public static void main(String[] args){
        try(Connection connection = ServerConnector.ERPConnection()){
            connection.setAutoCommit(false);

            //getting all the sections and their linked course_id;
            String get_command = """
                    SELECT s.section_id,s.course_code,
                    s.instructor_id,c.course_id FROM sections s    
                    JOIN courses c ON c.course_code = s.course_code             
                    """;

            try (PreparedStatement ps = connection.prepareStatement(get_command);
                 ResultSet rs = ps.executeQuery()) {
                //inserting grades into the table
                String insert_command = """
                        INSERT INTO grade_components (course_id, section_id, instructor_id, assessment_name, weightage)
                        VALUES (?, ?, ?, ?, ?)""";

                try(PreparedStatement ins = connection.prepareStatement(insert_command)){
                    while (rs.next()) {
                        long section_id = rs.getLong("section_id");
                        long course_id = rs.getLong("course_id");
                        long instructor_id = rs.getLong("instructor_id");

                        for (String[] comp : grade_components) {
                            ins.setLong(1, course_id);
                            ins.setLong(2, section_id);
                            ins.setLong(3, instructor_id);
                            ins.setString(4, comp[0]);
                            ins.setInt(5, Integer.parseInt(comp[1]));
                            ins.addBatch();
                        }
                    }
                    ins.executeBatch();
                }
            }
            connection.commit();
            System.out.println("Successfully seeded grade components");
        } catch(SQLException sqlE){
            System.err.println("Issue in seeding grade components.");
            sqlE.printStackTrace();
        }
    }
}
