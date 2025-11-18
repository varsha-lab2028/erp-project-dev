package edu.univ.erp.ui.student;

import edu.univ.erp.access.AccessControl;
import edu.univ.erp.auth.session.Session; //will use it after log in is fully complete
import edu.univ.erp.domain.Section;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.util.RoundedButton;
import edu.univ.erp.util.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class StudentRegistrationsPanel extends JPanel {
    private JTable reg_table;
    private List<Section> current_sections = Collections.emptyList();
    private final RoundedButton drop_button = new RoundedButton("Drop Selected");
    private final JLabel status_label = new JLabel(" ");
    private final StudentService student_service = new StudentService();

    public StudentRegistrationsPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.PRIMARY_WHITE);

        //top bar
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        top.setBackground(Theme.PRIMARY_WHITE);
        top.setPreferredSize(new Dimension(900, 60));

        drop_button.setPreferredSize(new Dimension(160, 35));
        //drop_button.setEnabled(AccessControl.canAccess("STU_REGISTER"));
        top.add(drop_button);
        add(top, BorderLayout.NORTH);

        //setting up the table
        reg_table = new JTable();
        styleTable(reg_table); //styling the table
        JScrollPane sp = new JScrollPane(reg_table);
        sp.getViewport().setBackground(Theme.PRIMARY_WHITE);
        sp.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        add(sp, BorderLayout.CENTER);

        //adding status label to the bottom of the interface
        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT));
        south.setBackground(Theme.PRIMARY_WHITE);
        status_label.setFont(Theme.FONT_SMALL);
        south.add(status_label);
        add(south, BorderLayout.SOUTH);

        //method to load registrations
        reloadRegistrations();

        //action listeners
        drop_button.addActionListener(e -> onDrop());
    }

    // loading currently registered sections into the table
    private void reloadRegistrations() {
        //long studentId = Session.userId(); can't use it right now because login not there
        long student_id = 3L;
        try {
            current_sections = student_service.getRegisteredSectionsList(student_id);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load registrations",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            current_sections = Collections.emptyList();
        }

        //rebuild the table columns
        String[] columns = {"COURSE CODE", "SECTION ID", "DAY", "TIMINGS", "CLASSROOM", "TERM"};
        Object[][] data = new Object[current_sections.size()][columns.length];
        for (int i = 0; i < current_sections.size(); i++) {
            Section s = current_sections.get(i);
            String term = s.getSemSeason() + " " + s.getSemNumber() + " " + s.getYear();
            data[i][0] = s.getCourseCode();
            data[i][1] = s.getSectionId();
            data[i][2] = s.getDay();
            data[i][3] = s.getTimings();
            data[i][4] = s.getClassroom();
            data[i][5] = term;
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        reg_table.setModel(model);
        status_label.setText(current_sections.size() + " registrations");
    }

    // drop the selected registration and refresh table
    private void onDrop() {
        int row = reg_table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a registration to drop.");
            return;
        }
        /*
        if (!AccessControl.canAccess("STU_REGISTER")) {
            JOptionPane.showMessageDialog(this, "Cannot drop now (maintenance ON).");
            return;
        }*/
        Section s = current_sections.get(row);
        //long studentId = Session.userId();
        long student_id = 3L; //just temporary for now, will change later
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Drop section " + s.getCourseCode() + " (Section " + s.getSectionId() + ")?",
                "Confirm Drop",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            // use your existing dropSection() to enforce policy
            String msg = student_service.dropSection(student_id, s.getSectionId());
            if (!msg.startsWith("Dropped successfully")) {
                JOptionPane.showMessageDialog(this, msg, "Cannot drop", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, msg);
                reloadRegistrations(); // refresh table
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error dropping section: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void styleTable(JTable table) {
        table.setFont(Theme.FONT_TEXT);
        table.setRowHeight(30);
        table.setSelectionBackground(Theme.SEA_GREEN.darker());
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(230,230,230));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Theme.SEA_GREEN);
        header.setForeground(Theme.TEXT_DARK);
    }
}


