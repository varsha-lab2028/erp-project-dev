package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.domain.TimeTableRow;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.util.LoginTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class StudentTimetablePanel extends JPanel {
    private final StudentService student_service = new StudentService();
    private JTable timetable_table;
    private final JLabel status_label = new JLabel(" ");
    private List<TimeTableRow> current_rows = Collections.emptyList();

    public StudentTimetablePanel() {
        setLayout(new BorderLayout());
        setBackground(LoginTheme.PRIMARY_WHITE);

        //top bar
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        top.setBackground(LoginTheme.PRIMARY_WHITE);
        top.setPreferredSize(new Dimension(900, 60));

        //showing the label of timetable on top
        JLabel title = new JLabel("Weekly Class Schedule");
        title.setFont(LoginTheme.FONT_TITLE);
        title.setForeground(LoginTheme.DEEP_SEA);
        top.add(title);
        add(title, BorderLayout.NORTH);

        //setting up the timetable
        String[] columns = {"Day", "Time", "Course Code", "Course Name", "Classroom"};
        //making the cells non-editable
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        timetable_table = new JTable(model);
        //StudentCoursePanel.styleTable(timetable_table);

        JScrollPane scrollPane = new JScrollPane(timetable_table);
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.getViewport().setBackground(LoginTheme.PRIMARY_WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        //status bar
        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT));
        south.setBackground(LoginTheme.PRIMARY_WHITE);
        status_label.setFont(LoginTheme.FONT_SMALL);
        south.add(status_label);
        add(south, BorderLayout.SOUTH);

        //loading the data
        loadingTimeTable();
    }

    private void loadingTimeTable() {
        long student_id = 3L; //hard-coded
        try {
            current_rows = student_service.getTimeTable(student_id);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load timetable",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            current_rows = Collections.emptyList();
        }

        DefaultTableModel model = (DefaultTableModel) timetable_table.getModel();
        model.setRowCount(0); //clearing any old rows
        //add one row per TimeTableRow
        for (TimeTableRow row : current_rows) {
            model.addRow(new Object[]{
                    row.getDay(),
                    row.getTimings(),
                    row.getCourseCode(),
                    row.getName(),
                    row.getClassroom()
            });
        }
        status_label.setText(current_rows.size() + " classes scheduled");
    }
}
