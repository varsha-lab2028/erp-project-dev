package edu.univ.erp.ui.student;
import edu.univ.erp.access.AccessControl;
import edu.univ.erp.domain.Course;
import edu.univ.erp.service.StudentService;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class StudentCoursePanel extends JPanel{
    //private final JTable table;
    private final JTextField search_field = new JTextField();
    private final JButton search_button = new JButton("Search");
    private final JButton register_button = new JButton("Register Selected");
    private final JLabel status_label = new JLabel(" ");
    private final StudentService student_service = new StudentService();

    //constructor
    public StudentCoursePanel(){
        setLayout(new BorderLayout());

        // Top bar (null layout to use setBounds inside a fixed-height panel)
        JPanel top = new JPanel(null);
        top.setPreferredSize(new Dimension(900, 44));
        JLabel lbl = new JLabel("Search:");
        lbl.setBounds(10, 10, 60, 24);
        search_field.setBounds(70, 10, 260, 24);
        search_button.setBounds(340, 10, 100, 24);
        register_button.setBounds(460, 10, 160, 24);
        register_button.setEnabled(AccessControl.canAccess("STU_REGISTER"));
        top.add(lbl); top.add(search_field); top.add(search_button); top.add(register_button);
        add(top, BorderLayout.NORTH);

        // Table center
        /*
        String[] cols = {"Code","Title","Credits","Capacity","Instructor","Term"};
        DefaultTableModel model = new DefaultTableModel(cols, 0){ @Override public boolean isCellEditable(int r,int c){return false;}};
        table = new JTable(model);
        table.setRowHeight(22);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Status south
        JPanel south = new JPanel(new BorderLayout());
        south.add(status, BorderLayout.WEST);
        add(south, BorderLayout.SOUTH);
         */

        //for creating the course catalog in the catalog panel
        List<Course> tableList;
        String[] columns = {"COURSE CODE","COURSE NAME","COURSE CREDITS"};
        try {
            tableList = student_service.browseCourseCatalog("");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Object[][] data= new Object[tableList.size()][3];
        for (int i=0; i<tableList.size();i++){
            Course course = tableList.get(i);
            data[i][0]=course.getCourseCode();
            data[i][1]=course.getName();
            data[i][2]=course.getCredits();
        }
        JTable table = new JTable (data, columns);
        table.setBackground(new Color(185, 227,223));
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(10, 60, 880, 500);
        add(sp);


        // Actions
        /*
        searchBtn.addActionListener(e -> status.setText("Search: '"+searchField.getText().trim()+"' (stub)"));
        registerBtn.addActionListener(e -> onRegister(model));
         */
    }

    private void addRow(DefaultTableModel m, String code, String title, int credits, int cap, int enrolled, String instr, String term){
        m.addRow(new Object[]{code, title, credits, (enrolled+"/"+cap), instr, term});
    }

    /*
    private void onRegister(DefaultTableModel model){
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a course/section to register."); return; }
        if (!AccessControl.canAccess("STU_REGISTER")) { JOptionPane.showMessageDialog(this, "Maintenance is ON or action not allowed."); return; }
        // Stub only: show success; Week-4 DAO will do real checks (capacity/duplicate/deadline)
        String code = model.getValueAt(row, 0).toString();
        JOptionPane.showMessageDialog(this, "Registered for " + code + " (stub)");
    }
     */
}

