package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.service.StudentService;
import edu.univ.erp.util.LoginTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


public class StudentGradesPanel extends JPanel {
    private final JTable grades_table;
    private final JLabel status_label = new JLabel(" ");
    private final StudentService student_service = new StudentService();

    public StudentGradesPanel(){
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        top.setBackground(LoginTheme.PRIMARY_WHITE);
        top.setPreferredSize(new Dimension(900, 60));

        JLabel title = new JLabel("Course Grade Components");
        title.setFont(LoginTheme.FONT_TITLE);
        title.setForeground(LoginTheme.TEXT_DARK);
        top.add(title);

        add(top, BorderLayout.NORTH);

        // ---------- table center ----------
        grades_table = new JTable();
        //StudentCoursePanel.styleTable(grades_table);   // reuse same styling

        JScrollPane scroll = new JScrollPane(grades_table);
        scroll.getViewport().setBackground(LoginTheme.PRIMARY_WHITE);
        add(scroll, BorderLayout.CENTER);

        // ---------- status bar ----------
        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(LoginTheme.PRIMARY_WHITE);
        //status_label.setFont(Theme.FONT_STATUS);
        south.add(status_label, BorderLayout.WEST);
        add(south, BorderLayout.SOUTH);

        // initial load
        reloadGrades();
    }

    private void reloadGrades(){
        long student_id = 3L; //hardcoded
        int sem_no = 1;
        String sem_season = "MONSOON";
        int year = 2025;
        List<Object[]> rows;

        try {
            rows = student_service.getGradeComponentTable(student_id, sem_no, sem_season, year);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load grades",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            rows = Collections.emptyList();
        }

        String[] columns = {"COURSE CODE", "COURSE NAME", "CREDITS", "QUIZ WEIGHTAGE", "ASSIGNMENT WEIGHTAGE", "MIDSEM WEIGHTAGE", "ENDSEM WEIGHTAGE"};
        Object[][] data = new Object[rows.size()][columns.length];

        for (int i = 0; i < rows.size(); i++) {
            data[i] = rows.get(i); //typecasting
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        grades_table.setModel(model);
        status_label.setText(rows.size() + "courses");
    }

}


