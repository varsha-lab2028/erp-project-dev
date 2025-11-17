package edu.univ.erp.ui.student;

import edu.univ.erp.access.AccessControl;
import edu.univ.erp.auth.session.Session;
import edu.univ.erp.domain.Section;
import edu.univ.erp.service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StudentRegistrationsPanel extends JPanel {
    private JTable registrations_table;
    private final JButton drop_button = new JButton("Drop Selected");
    private final JLabel status_label = new JLabel(" ");
    private final StudentService student_service = new StudentService();

    public StudentRegistrationsPanel() {
        setLayout(new BorderLayout());

        //top bar
        JPanel top = new JPanel(null);
        top.setPreferredSize(new Dimension(900, 44));

        drop_button.setBounds(10, 10, 160, 24);
        drop_button.setEnabled(AccessControl.canAccess("STU_REGISTER"));
        top.add(drop_button);
        add(top, BorderLayout.NORTH);

        // table center
        registrations_table = new JTable();
        registrations_table.setBackground(new Color(185, 227, 223));
        JScrollPane sp = new JScrollPane(registrations_table);
        sp.setBounds(10, 60, 880, 500);
        add(sp, BorderLayout.CENTER);

        //status south
        JPanel south = new JPanel(new BorderLayout());
        south.add(status_label, BorderLayout.WEST);
        add(south, BorderLayout.SOUTH);

        // initial load
        loadRegistrations();

        // Actions
        drop_button.addActionListener(e -> onDrop());
    }

    // loading currently registered sections into the table
    private void loadRegistrations() {
        long studentId = Session.userId();
        List<Section> sections;

        try {
            sections = student_service.getRegisteredSectionsList(studentId);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load registrations: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String[] columns = {"COURSE CODE", "SECTION ID", "DAY", "TIMINGS", "CLASSROOM", "TERM"};
        Object[][] data = new Object[sections.size()][columns.length];

        for (int i = 0; i < sections.size(); i++) {
            Section s = sections.get(i);
            String term = s.getSemSeason() + " " + s.getSemNumber() + " " + s.getYear();
            data[i][0] = s.getCourseCode();
            data[i][1] = s.getSectionId();
            data[i][2] = s.getDay();
            data[i][3] = s.getTimings();
            data[i][4] = s.getClassroom();
            data[i][5] = term;
        }

        registrations_table.setModel(new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        });

        status_label.setText(sections.size() + " registrations");
    }

    // drop the selected registration and refresh table
    private void onDrop() {
        int row = registrations_table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a registration to drop.");
            return;
        }
        if (!AccessControl.canAccess("STU_REGISTER")) {
            JOptionPane.showMessageDialog(this, "Cannot drop now (maintenance ON).");
            return;
        }

        long studentId = Session.userId();
        long sectionId = (long) registrations_table.getModel().getValueAt(row, 1); // SECTION ID
        String courseCode = registrations_table.getModel().getValueAt(row, 0).toString();

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Drop " + courseCode + " (Section " + sectionId + ")?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );
        if (choice != JOptionPane.YES_OPTION) return;

        String result;
        try {
            result = student_service.dropSection(studentId, sectionId);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to drop section: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(this, result);
        // reload table after dropping
        loadRegistrations();
    }
}


