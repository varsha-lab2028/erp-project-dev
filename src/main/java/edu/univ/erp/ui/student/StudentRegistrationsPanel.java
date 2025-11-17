package edu.univ.erp.ui.student;

import com.zaxxer.hikari.util.FastList;
import edu.univ.erp.access.AccessControl;
import edu.univ.erp.auth.session.Session; //will use it after log in is fully complete
import edu.univ.erp.domain.Section;
import edu.univ.erp.service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class StudentRegistrationsPanel extends JPanel {
    private JTable reg_table;
    private List<Section> current_sections = Collections.emptyList();
    private final JButton drop_button = new JButton("Drop Selected");
    private final JLabel status_label = new JLabel(" ");
    private final StudentService student_service = new StudentService();

    public StudentRegistrationsPanel() {
        setLayout(new BorderLayout());

        //top bar
        JPanel top = new JPanel(null);
        top.setPreferredSize(new Dimension(900, 44));
        drop_button.setBounds(10, 10, 160, 24);
        //drop_button.setEnabled(AccessControl.canAccess("STU_REGISTER"));
        top.add(drop_button);
        add(top, BorderLayout.NORTH);

        // table center
        reg_table = new JTable();
        reg_table.setBackground(new Color(185, 227, 223));
        JScrollPane sp = new JScrollPane(reg_table);
        //sp.setBounds(10, 60, 880, 500);
        add(sp, BorderLayout.CENTER);

        //adding status label to the bottom of the interface
        JPanel south = new JPanel(new BorderLayout());
        south.add(status_label, BorderLayout.WEST);
        add(south, BorderLayout.SOUTH);

        reloadRegistrations(); //method to load registrations

        //action listeners
        drop_button.addActionListener(e -> onDrop());
    }

    private long currentStudentId(){
        return 3L; //this method has been created for just testing right now
    }

    // loading currently registered sections into the table
    private void reloadRegistrations() {
        //long studentId = Session.userId(); can't use it right now because login not there
        long student_id = currentStudentId();
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
        Section s = current_sections.get(row); //long studentId = Session.userId();
        long student_id = currentStudentId();
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
}


