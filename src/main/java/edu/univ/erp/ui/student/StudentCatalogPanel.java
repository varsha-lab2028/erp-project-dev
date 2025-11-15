package edu.univ.erp.ui.student;
import edu.univ.erp.access.AccessControl;
import edu.univ.erp.domain.Course;
import edu.univ.erp.service.StudentService;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class StudentCatalogPanel extends JPanel{
    //private final JTable table;
    private final JTextField searchField = new JTextField();
    private final JButton searchBtn = new JButton("Search");
    private final JButton registerBtn = new JButton("Register Selected");
    private final JLabel status = new JLabel(" ");
    private final StudentService studentFlowControls = new StudentService();


    //constructor
    public StudentCatalogPanel(){
        setLayout(new BorderLayout());

        // Top bar (null layout to use setBounds inside a fixed-height panel)
        JPanel top = new JPanel(null);
        top.setPreferredSize(new Dimension(900, 44));
        JLabel lbl = new JLabel("Search:");
        lbl.setBounds(10, 10, 60, 24);
        searchField.setBounds(70, 10, 260, 24);
        searchBtn.setBounds(340, 10, 100, 24);
        registerBtn.setBounds(460, 10, 160, 24);
        registerBtn.setEnabled(AccessControl.canAccess("STU_REGISTER"));
        top.add(lbl); top.add(searchField); top.add(searchBtn); top.add(registerBtn);
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

        List<Course> tableList;
        String[] column = {"code","title","credits"};

        try {
            tableList = studentFlowControls.browseCatalog("");
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
        JTable table = new JTable (data, column);
        table.setBackground(new Color(185, 227,223));
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(10, 60, 880, 500);      // <── THIS IS YOUR ANSWER
        add(sp);

        /*
        // Dummy data (replace with DAO results later)
        addRow(model, "CS101","Intro to CS",3, 60, 55, "Dr. Rao","Autumn 2025");
        addRow(model, "MA102","Calculus II",4, 80, 80, "Dr. Sen","Autumn 2025");
        addRow(model, "HS105","Ethics",2, 40, 33, "Dr. Iyer","Autumn 2025");
        status.setText("3 courses");
         */

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

