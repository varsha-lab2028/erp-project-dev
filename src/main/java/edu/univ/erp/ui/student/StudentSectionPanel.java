package edu.univ.erp.ui.student;
import edu.univ.erp.access.AccessControl;
import edu.univ.erp.domain.Section;
import edu.univ.erp.service.StudentService;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class StudentSectionPanel extends JPanel{
    private JTable section_table; //for onRegister() method

    private final JTextField search_field = new JTextField();
    private final JButton search_button = new JButton("Search");
    private final JButton register_button = new JButton("Register Selected");
    private final JLabel status_label = new JLabel(" ");
    private final StudentService student_service = new StudentService();

    //constructor
    public StudentSectionPanel(){
        setLayout(new BorderLayout());

        //top bar
        JPanel top = new JPanel(null);
        top.setPreferredSize(new Dimension(900, 44));

        JLabel lbl = new JLabel("Search:");
        lbl.setBounds(10, 10, 60, 24);
        search_field.setBounds(70, 10, 260, 24);
        search_button.setBounds(340, 10, 100, 24);
        register_button.setBounds(460, 10, 160, 24);
        register_button.setEnabled(AccessControl.canAccess("STU_REGISTER"));

        top.add(lbl);
        top.add(search_field);
        top.add(search_button);
        top.add(register_button);
        add(top, BorderLayout.NORTH);

        //table center
        section_table = new JTable();
        section_table.setBackground(new Color(185, 227, 223));
        JScrollPane sp = new JScrollPane(section_table);
        sp.setBounds(10, 60, 880, 500);
        add(sp, BorderLayout.CENTER);

        // ── Status south ───────────────────────────────────────
        JPanel south = new JPanel(new BorderLayout());
        south.add(status_label, BorderLayout.WEST);
        add(south, BorderLayout.SOUTH);

        // initial load (no search filter)
        loadSections("");

        // Actions
        search_button.addActionListener(e -> loadSections(search_field.getText().trim()));
        // later you can wire register_button to a method using section_table selection
    }

    //loading sections and add them in the table
    private void loadSections(String query) {
        List<Section> sectionTable_list;
        try {
            sectionTable_list = student_service.browseSectionCatalog(query);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load sections: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // column names for the section catalog table
        String[] columns = {"COURSE CODE", "INSTRUCTOR NAME", "DAY", "TIMINGS", "CLASSROOM", "CAPACITY",};
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

        // replace table model
        section_table.setModel(new javax.swing.table.DefaultTableModel(
                data,
                columns
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        });

        status_label.setText(sectionTable_list.size() + " sections");
    }
}
