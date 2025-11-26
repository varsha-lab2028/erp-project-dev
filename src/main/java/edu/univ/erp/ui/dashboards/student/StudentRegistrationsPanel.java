package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.domain.Section;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.util.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class StudentRegistrationsPanel extends JPanel {
    private JTable reg_table;
    private List<Section> current_sections = Collections.emptyList();
    private final RoundedButton drop_button;
    private final JLabel status_label = new JLabel(" ");
    private final StudentService student_service = new StudentService();

    public StudentRegistrationsPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        //top bar
        JPanel top = new JPanel();
        top.setBackground(Color.WHITE);
        top.setOpaque(true);
        top.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        //drop button
        drop_button = new RoundedButton("Drop Selected");
        drop_button.setPreferredSize(new Dimension(160, 35));
        //drop_button.setEnabled(AccessControl.canAccess("STU_REGISTER"));
        top.add(drop_button);
        add(top, BorderLayout.NORTH);

        //setting up the table
        reg_table = new JTable();
        reg_table.setFont(DashboardTheme.FONT_REGULAR);
        reg_table.setRowHeight(35);
        reg_table.setShowGrid(false);
        reg_table.setIntercellSpacing(new Dimension(0, 5));
        reg_table.getTableHeader().setBackground(Color.WHITE);
        reg_table.getTableHeader().setForeground(DashboardTheme.TEXT_SECONDARY);
        reg_table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        reg_table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 225, 225)));
        reg_table.setSelectionBackground(new Color(200, 230, 201, 50));
        JScrollPane sp = new JScrollPane(reg_table);
        sp.getViewport().setBackground(Color.WHITE);
        sp.setBorder(BorderFactory.createEmptyBorder());

        JPanel tableCard = new JPanel();
        tableCard.setBackground(Color.WHITE);
        tableCard.setOpaque(true);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));
        tableCard.add(sp, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);
        tableCard.add(sp, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);

        //adding status label to the bottom of the interface
        status_label.setFont(DashboardTheme.FONT_SMALL);
        add(status_label, BorderLayout.SOUTH);

        //method to load registrations
        reloadRegistrations();

        //action listeners
        drop_button.addActionListener(e -> onDrop());
    }

    // loading currently registered sections into the table
    private void reloadRegistrations() {
        //long studentId = Session.userId(); -> can't use it right now because login not there
        long student_id = 3L; //hard-coded
        try {
            current_sections = student_service.getRegisteredSectionsList(student_id);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load registrations: " + e.getMessage(),
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
}


