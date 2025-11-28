package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.auth.session.Session;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.ui.common.DashboardComponents;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        //UI table setup
        grades_table = new JTable();
        //table styling
        grades_table.setFont(DashboardTheme.FONT_REGULAR);
        grades_table.setRowHeight(35);
        grades_table.setShowGrid(false);
        grades_table.setIntercellSpacing(new Dimension(0, 5));
        grades_table.getTableHeader().setBackground(Color.WHITE);
        grades_table.getTableHeader().setForeground(DashboardTheme.TEXT_SECONDARY);
        grades_table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        // replace undefined DashboardTheme.BORDER_GRAY with a concrete Color
        grades_table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(224, 224, 224)));
        grades_table.setSelectionBackground(new Color(200, 230, 201, 50));
        grades_table.setSelectionForeground(DashboardTheme.TEXT_PRIMARY);

        JScrollPane scroll = new JScrollPane(grades_table);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        //Card wrapper
        JPanel tableCard = new JPanel();
        tableCard.setBackground(Color.WHITE);
        tableCard.setOpaque(true);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));
        // replace default panel with a rounded-corner panel (no external DashboardComponents type needed)
        tableCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
            int arc = 16;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // subtle shadow
            g2.setColor(new Color(0, 0, 0, 12));
            g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, arc, arc);

            // panel background
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, arc, arc);

            g2.dispose();

            super.paintComponent(g);
            }

            @Override
            public boolean isOpaque() {
            return false;
            }
        };
        tableCard.setBackground(Color.WHITE);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));

        //title inside the card
        JLabel title = new JLabel("Course Grade Components");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(DashboardTheme.TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(10, 10, 15, 0));
        tableCard.add(title, BorderLayout.NORTH);
        tableCard.add(scroll, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);

        //status bar
        status_label.setFont(DashboardTheme.FONT_SMALL);
        add(status_label, BorderLayout.SOUTH);

        // initial load
        reloadGrades();
    }

    private void reloadGrades(){
        long student_id = Session.userId();
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
    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            reloadGrades(); 
        }
    }

}


