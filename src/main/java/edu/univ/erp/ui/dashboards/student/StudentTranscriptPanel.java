package edu.univ.erp.ui.dashboards.student;

//import com.formdev.flatlaf.ui.FlatListCellBorder;
import edu.univ.erp.domain.TranscriptRow;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.util.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class StudentTranscriptPanel extends JPanel{
    private final StudentService student_service = new StudentService();
    private JTable transcript_table;
    private List<TranscriptRow> transcript_rows = Collections.emptyList();

    private final JLabel term_label = new JLabel("Transcript");

    public StudentTranscriptPanel(){
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        //top bar = shows the term label and export csv button
        JPanel top_card = new JPanel();
        top_card.setBackground(Color.WHITE);
        top_card.setLayout(new BorderLayout());
        top_card.setBorder(new EmptyBorder(10, 15, 10, 15));

        RoundedButton csv_button = new RoundedButton("Export CSV");
        term_label.setFont(DashboardTheme.FONT_REGULAR);
        top_card.add(csv_button, BorderLayout.WEST);
        top_card.add(term_label, BorderLayout.EAST);
        add(top_card, BorderLayout.NORTH);

        //transcript table
        String[] columns = {"Course Code", "Course Name", "Credits", "Grade"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false; // read-only
            }
        };
        //styling the table
        transcript_table = new JTable(model);
        transcript_table.setFont(DashboardTheme.FONT_REGULAR);
        transcript_table.setRowHeight(35);
        transcript_table.setShowGrid(false);
        transcript_table.setIntercellSpacing(new Dimension(0, 5));
        transcript_table.getTableHeader().setBackground(Color.WHITE);
        transcript_table.getTableHeader().setForeground(DashboardTheme.TEXT_SECONDARY);
        transcript_table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        transcript_table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        transcript_table.setSelectionBackground(new Color(200, 230, 201, 50));
        transcript_table.setSelectionForeground(DashboardTheme.TEXT_PRIMARY);

        JScrollPane sp = new JScrollPane(transcript_table);
        sp.getViewport().setBackground(Color.WHITE);
        sp.setBorder(BorderFactory.createEmptyBorder());
        JPanel tableCard = new JPanel();
        tableCard.setBackground(Color.WHITE);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));
        tableCard.add(sp, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);

        //loading the data into transcript
        loadTranscript();

        //action listeners
        csv_button.addActionListener(e -> ExportCSV());
    }

    private void loadTranscript(){
        long student_id = 3L; //hard coded, will change later
        try{
            current_rows = student_service.getRegisteredCourseTranscript(student_id);
        } catch (SQLException sqlE){
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load transcript: " + sqlE.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            current_rows = Collections.emptyList();
        }

        DefaultTableModel model = (DefaultTableModel) transcript_table.getModel();
        model.setRowCount(0);

        if (!current_rows.isEmpty()) {
            Object[] first = current_rows.get(0);
            current_season = (String) first[3];
            current_sem_no  = (Integer) first[4];
            current_year  = (Integer) first[5];
        } else {
            current_season = "";
            current_sem_no = 0;
            current_year = 0;
        }

        term_label.setText("Current Term: " + formatCurrentTerm());

        for (Object[] row : current_rows) {
            model.addRow(new Object[] { row[0], row[1], row[2] });
        }
    }

    private String formatCurrentTerm() {
        if (current_season == null || current_season.isBlank()) {
            return "(unknown)";
        }
        return current_season + " " + current_sem_no + " " + current_year;
    }

    //exporting csv
    private void ExportCSV() {
        if (transcript_rows.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No completed courses to export.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("transcript.csv"));
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            long studentId = 3L; //hardcoded

            // header info
            out.println("Student ID," + studentId);
            out.println();

            // column headers
            out.println("Course Code,Course Title,Credits,Final Grade");

            // rows
            for (TranscriptRow row : transcript_rows) {
                out.printf("%s,%s,%d,%s%n",
                        row.getCourseCode(),
                        row.getCourseTitle(),
                        row.getCredits(),
                        row.getFinalGrade());
            }

            JOptionPane.showMessageDialog(
                    this,
                    "CSV exported to:\n" + file.getAbsolutePath()
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to export CSV: " + ex.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
