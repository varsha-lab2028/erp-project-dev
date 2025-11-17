package edu.univ.erp.ui.student;
import com.formdev.flatlaf.ui.FlatListCellBorder;
import edu.univ.erp.access.AccessControl;
import edu.univ.erp.domain.Course;
import edu.univ.erp.service.StudentService;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class StudentCoursePanel extends JPanel{
    private final JTable course_table;
    private final JTextField search_field = new JTextField();
    private final JButton search_button = new JButton("Search");
    private final JButton register_button = new JButton("Register Selected");
    private final JLabel status_label = new JLabel(" ");
    private final StudentService student_service = new StudentService();

    //constructor
    public StudentCoursePanel(){
        setLayout(new BorderLayout());

        //top bar
        JPanel top = new JPanel(null);
        top.setPreferredSize(new Dimension(900, 44));
        JLabel search_label = new JLabel("Search:");
        search_label.setBounds(10, 10, 60, 24);
        search_field.setBounds(70, 10, 260, 24);
        search_button.setBounds(340, 10, 100, 24);
        register_button.setBounds(460, 10, 160, 24);
        register_button.setEnabled(false);
        register_button.setToolTipText("Register for your courses from the Section Catalog Tab");

        //adding to the top bar
        top.add(search_label);
        top.add(search_field);
        top.add(search_button);
        top.add(register_button);
        add(top, BorderLayout.NORTH);

        //Course Catalog table
        List<Course> table_list;
        String[] columns = {"COURSE CODE","COURSE NAME","COURSE CREDITS"};
        try {
            table_list = student_service.browseCourseCatalog("");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load courses.");
        }
        Object[][] data= new Object[table_list.size()][3];
        for (int i=0; i<table_list.size();i++){
            Course course = table_list.get(i);
            data[i][0]=course.getCourseCode();
            data[i][1]=course.getName();
            data[i][2]=course.getCredits();
        }
        //making the table cells non-editable
        DefaultTableModel model = new DefaultTableModel(data, columns){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        course_table = new JTable (model);
        course_table.setBackground(new Color(185, 227,223));
        JScrollPane sp = new JScrollPane(course_table);
        sp.setBounds(10, 60, 880, 500);
        add(sp, BorderLayout.CENTER);

        //status bar placed in the south of the interface
        JPanel south = new JPanel(new BorderLayout());
        south.add(status_label, BorderLayout.WEST);
        add(south, BorderLayout.SOUTH);
        status_label.setText(table_list.size() + " courses");

        //Action listeners
        search_button.addActionListener(e -> {
                String keyword = search_field.getText().trim();
        List<Course> searchedCourse_list;
        try {
            searchedCourse_list = student_service.browseCourseCatalog(keyword);
        } catch (SQLException sqlE){
            sqlE.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error in loading courses.");
            return;
            }
        //getting the searched up courses
            Object[][] new_data = new Object[searchedCourse_list.size()][3];
            for (int i = 0; i < searchedCourse_list.size(); i++) {
                Course c = searchedCourse_list.get(i);
                new_data[i][0] = c.getCourseCode();
                new_data[i][1] = c.getName();
                new_data[i][2] = c.getCredits();
            }

            DefaultTableModel searched_model = new DefaultTableModel(
                    new_data,
                    new String[]{"COURSE CODE", "COURSE NAME", "COURSE CREDITS"}
            ){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };
            course_table.setModel(searched_model);
            //updating the status label
            status_label.setText(searchedCourse_list.size() + " courses");
        });
    }
}

