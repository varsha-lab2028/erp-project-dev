package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.auth.session.Session;
import edu.univ.erp.domain.TimeTableRow;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.service.StudentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class StudentHomePanel extends JPanel {
    private final StudentService student_service = new StudentService();

    public StudentHomePanel() {
        setLayout(new BorderLayout());
        setBackground(DashboardTheme.BG_MAIN);
        
        // Clean border definition without "top:", "left:", etc.
        setBorder(new EmptyBorder(30, 30, 30, 30));

        //student who has logged in
        long student_id = Session.userId();

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(DashboardTheme.BG_MAIN);

        // 1. Stats Grid (Student Specific)
        JPanel grid = new JPanel(new GridLayout(1, 3, 20, 0));
        grid.setBackground(DashboardTheme.BG_MAIN);
        grid.setMaximumSize(new Dimension(2000, 120));
        
        // Fixed: Added the missing 4th argument (Color) for all cards
        grid.add(new DashboardComponents.StatsCard("Current CGPA", "3.8", "🎓", DashboardTheme.PRIMARY));
        grid.add(new DashboardComponents.StatsCard("Credits Earned", "20", "⭐", DashboardTheme.WARNING));
        grid.add(new DashboardComponents.StatsCard("Attendance", "92%", "✅", DashboardTheme.SUCCESS));
        
        body.add(grid);
        body.add(Box.createVerticalStrut(30));

        //timetable section in the home panel
        List<TimeTableRow> ttRows;
        try {
            ttRows = student_service.getTimeTable(student_id);
        } catch (SQLException ex) {
            ttRows = Collections.emptyList();
        }

        String[] cols = {"Day", "Time", "Course Code", "Course Name", "Classroom"};

        Object[][] tableData = new Object[ttRows.size()][cols.length];
        for (int i = 0; i < ttRows.size(); i++) {
            TimeTableRow r = ttRows.get(i);
            tableData[i][0] = r.getDay();
            tableData[i][1] = r.getTimings();
            tableData[i][2] = r.getCourseCode();
            tableData[i][3] = r.getName();
            tableData[i][4] = r.getClassroom();
        }

        DashboardComponents.TablePanel timetableCard =
                new DashboardComponents.TablePanel("My Class Schedule (" + ttRows.size() + " classes)", cols, tableData);

        body.add(timetableCard);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(DashboardTheme.BG_MAIN);

        add(scroll, BorderLayout.CENTER);
    }
}
