package edu.univ.erp.ui.dashboards.student;

//import com.formdev.flatlaf.ui.FlatListCellBorder;
import edu.univ.erp.service.StudentService;

import javax.swing.*;
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
    private List<Object[]> current_rows = Collections.emptyList();

    //current info
    private String current_season = "";
    private int current_sem_no = 0;
    private int current_year = 0;

    private final JLabel term_label = new JLabel("Current Term: (unknown)");


    public StudentTranscriptPanel(){
        setLayout(new BorderLayout());

        //top bar = shows the term label and export csv button
        JPanel top = new JPanel(new BorderLayout());

        JPanel left = new JPanel (new FlowLayout(FlowLayout.LEFT));
        JButton csv_button = new JButton("Export CSV");
        left.add(csv_button);
        top.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.add(term_label);
        top.add(right, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        //table
        String[] columns = {"Course Code", "Course Name", "Course Credits"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false; // read-only
            }
        };
        transcript_table = new JTable(model);
        add(new JScrollPane(transcript_table), BorderLayout.CENTER);

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
        if (current_rows.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No registered courses to export.");
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
            out.println("Current Season," + current_season);
            out.println("Current Semester Number," + current_sem_no);
            out.println("Current Year," + current_year);
            out.println();

            // column headers
            out.println("Course Code,Course Name,Credits");

            // rows
            for (Object[] row : current_rows) {
                // row[0] = courseCode, row[1] = courseName, row[2] = credits
                out.printf("%s,%s,%s%n",
                        String.valueOf(row[0]),
                        String.valueOf(row[1]),
                        String.valueOf(row[2]));
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
