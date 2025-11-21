package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.domain.Section;
import edu.univ.erp.service.StudentService;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.util.RoundedButton;

import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class StudentSectionPanel extends JPanel {
    private JTable section_table;
    private List<Section> current_sections; //sections rows currently shown in the table

    private final JTextField search_field = new JTextField(20);
    private final RoundedButton search_button;
    private final RoundedButton register_button;
    private final JLabel status_label = new JLabel(" ");
    private final StudentService student_service = new StudentService();

    //constructor
    public StudentSectionPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_LIGHT);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        //top bar
        JPanel top_card = new DashboardComponents.RoundedPanel(15, Color.WHITE, true);
        top_card.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        JLabel search_label = new JLabel("Search:");
        search_label.setFont(DashboardTheme.FONT_REGULAR);
        search_field.setFont(DashboardTheme.FONT_REGULAR);

        //search button
        search_button = new RoundedButton("Search");

        //register button
        //register_button.setEnabled(AccessControl.canAccess("STU_REGISTER"));
        register_button = new RoundedButton("Register Selected");
        register_button.setEnabled(true); //right now, it is temporary

        //adding these to the top bar
        top_card.add(search_label);
        top_card.add(search_field);
        top_card.add(search_button);
        top_card.add(Box.createHorizontalStrut(20));
        top_card.add(register_button);
        add(top_card, BorderLayout.NORTH);

        //loading the sections into the table
        List<Section> sectionTable_list;
        String[] columns = {"COURSE CODE", "INSTRUCTOR", "DAY", "TIMINGS", "CLASSROOM", "CAPACITY",};
        try {
            sectionTable_list = student_service.browseSectionCatalog("");
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error loading sections",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            sectionTable_list = Collections.emptyList();
        }

        current_sections = sectionTable_list;

        Object[][] data = new Object[sectionTable_list.size()][columns.length];
        for (int i = 0; i < sectionTable_list.size(); i++) {
            Section s = sectionTable_list.get(i);
            data[i][0] = s.getCourseCode();
            data[i][1] = s.getInstructorName();
            data[i][2] = s.getDay();
            data[i][3] = s.getTimings();
            data[i][4] = s.getClassroom();
            data[i][5] = s.getCapacity();
        }

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        //table center
        section_table = new JTable(model);
        section_table.setFont(DashboardTheme.FONT_REGULAR);
        section_table.setRowHeight(35);
        section_table.setShowGrid(false);
        section_table.setIntercellSpacing(new Dimension(0, 5));
        section_table.getTableHeader().setBackground(Color.WHITE);
        section_table.getTableHeader().setForeground(DashboardTheme.TEXT_SECONDARY);
        section_table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        section_table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, DashboardTheme.BORDER_GRAY));
        section_table.setSelectionBackground(new Color(200, 230, 201, 50));
        section_table.setSelectionForeground(DashboardTheme.TEXT_PRIMARY);

        JScrollPane sp = new JScrollPane(section_table);
        sp.getViewport().setBackground(Color.WHITE);
        sp.setBorder(BorderFactory.createEmptyBorder());

        JPanel tableCard = new DashboardComponents.RoundedPanel(15, Color.WHITE, true);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));
        tableCard.add(sp, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);

        //status bar placed in the south of the interface
        status_label.setFont(DashboardTheme.FONT_SMALL);
        add(status_label, BorderLayout.SOUTH);
        status_label.setText(sectionTable_list.size() + " sections");

        //search action listener
        search_button.addActionListener(e -> {
            String keyword = search_field.getText().trim();
            List<Section> search_list;
            try {
                search_list = student_service.browseSectionCatalog(keyword);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading sections.");
                return;
            }

            current_sections = search_list;

            Object[][] newData = new Object[search_list.size()][columns.length];
            for (int i = 0; i < search_list.size(); i++) {
                Section s = search_list.get(i);
                newData[i][0] = s.getCourseCode();
                newData[i][1] = s.getInstructorName();
                newData[i][2] = s.getDay();
                newData[i][3] = s.getTimings();
                newData[i][4] = s.getClassroom();
                newData[i][5] = s.getCapacity();
            }

            DefaultTableModel new_model = new DefaultTableModel(newData, columns) {
                @Override public boolean isCellEditable(int row, int col) { return false; }
            };
            section_table.setModel(new_model);
            status_label.setText(search_list.size() + " sections");
        });

        //register action listener
        register_button.addActionListener(e -> registerSelectedSection());
    }
    private void registerSelectedSection(){
        int row = section_table.getSelectedRow();
        if(row < 0){
            JOptionPane.showMessageDialog(this, "Please select a section first.");
            return;
        }
        /* commenting this out for now because we don't have login yet
        if (!Session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "You must be logged in.");
            return;
        }
        if (!AccessControl.canAccess("STU_REGISTER")) {
            JOptionPane.showMessageDialog(this, "Registration disabled. ERP currently under maintenance");
            return;
        }
         */
        Section s = current_sections.get(row);
        //long student_id = Session.user().getUserId();
        long student_id = 3L; //just for testing right now

        try {
            student_service.registerForSection(student_id, s.getSectionId());
            JOptionPane.showMessageDialog(this,
                    "Successfully registered for " + s.getCourseCode());

        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Failed", JOptionPane.WARNING_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}




