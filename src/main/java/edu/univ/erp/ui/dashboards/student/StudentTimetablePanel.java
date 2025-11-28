package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.auth.session.Session;
import edu.univ.erp.domain.TimeTableRow;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private long student_id;

    public StudentTimetablePanel() {
        this.student_id = Session.userId();
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        //top bar
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        top.setBackground(DashboardTheme.BG_MAIN);
        top.setPreferredSize(new Dimension(900, 60));

        //setting up the timetable
        String[] columns = {"Day", "Time", "Course Code", "Course Name", "Classroom"};
        //making the cells non-editable
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        //UI styling
        timetable_table = new JTable(model);
        timetable_table.setFont(DashboardTheme.FONT_REGULAR);
        timetable_table.setRowHeight(35);
        timetable_table.setShowGrid(false);
        timetable_table.setIntercellSpacing(new Dimension(0, 5));
        timetable_table.getTableHeader().setBackground(Color.WHITE);
        timetable_table.getTableHeader().setForeground(DashboardTheme.TEXT_SECONDARY);
        timetable_table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        timetable_table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        timetable_table.setSelectionBackground(new Color(200, 230, 201, 50));
        timetable_table.setSelectionForeground(DashboardTheme.TEXT_PRIMARY);

        JScrollPane scrollPane = new JScrollPane(timetable_table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        //ui card wrapper
        JPanel tableCard = new JPanel();
        tableCard.setBackground(Color.WHITE);
        tableCard.setOpaque(true);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));

        //title inside the card
        JLabel title = new JLabel("Weekly Class Schedule");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(DashboardTheme.TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(10, 10, 15, 0));
        tableCard.add(title, BorderLayout.NORTH);

        tableCard.add(scrollPane, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);

        //status bar
        status_label.setFont(DashboardTheme.FONT_SMALL);
        add(status_label, BorderLayout.SOUTH);

        //loading the data
        loadingTimeTable();
    }

    private void loadingTimeTable() {
        JDialog loadingDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Loading Timetable", true);
        loadingDialog.setLayout(new BorderLayout());
        loadingDialog.setSize(300, 100);
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel loadingPanel = new JPanel();
        loadingPanel.setLayout(new FlowLayout());
        loadingPanel.add(new JLabel("Loading timetable..."));
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        loadingPanel.add(progressBar);
        loadingDialog.add(loadingPanel, BorderLayout.CENTER);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    current_rows = student_service.getTimeTable(StudentTimetablePanel.this.student_id);
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(
                            StudentTimetablePanel.this,
                            "Failed to load timetable",
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    current_rows = Collections.emptyList();
                }
                return null;
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
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
        };

        worker.execute();
        loadingDialog.setVisible(true);
    }
    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            loadingTimeTable(); 
        }
    }
}
