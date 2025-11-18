package edu.univ.erp.ui.dashboards.student;
//import com.formdev.flatlaf.ui.FlatListCellBorder;
import edu.univ.erp.access.AccessControl;
import edu.univ.erp.domain.Course;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.util.RoundedButton;
import edu.univ.erp.util.Theme;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;

public class StudentCoursePanel extends JPanel{
    private final JTable course_table;
    private final JTextField search_field = new JTextField(20); //set columns for width
    private final RoundedButton search_button = new RoundedButton("Search");
    private final RoundedButton register_button = new RoundedButton("Register Selected");
    private final JLabel status_label = new JLabel(" ");
    private final StudentService student_service = new StudentService();

    //constructor
    public StudentCoursePanel(){
        setLayout(new BorderLayout());
        setBackground(Theme.PRIMARY_WHITE);

        //top bar
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        top.setBackground(Theme.PRIMARY_WHITE);
        top.setPreferredSize(new Dimension(900, 60));

        JLabel search_label = new JLabel("Search:");
        search_label.setFont(Theme.FONT_TEXT);
        search_field.setFont(Theme.FONT_TEXT);

        //register buttons
        register_button.setEnabled(false);
        register_button.setToolTipText("Register for your courses from the Section Catalog Tab");

        //adding to the top bar
        top.add(search_label);
        top.add(search_field);
        top.add(search_button);
        top.add(Box.createHorizontalStrut(20));
        top.add(register_button);
        add(top, BorderLayout.NORTH);

        //Course Catalog table
        List<Course> courseTable_list;
        String[] columns = {"COURSE CODE","COURSE NAME","COURSE CREDITS"};
        try {
            courseTable_list = student_service.browseCourseCatalog("");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load courses.");
        }
        Object[][] data= new Object[courseTable_list.size()][3];
        for (int i=0; i<courseTable_list.size();i++){
            Course course = courseTable_list.get(i);
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
        styleTable(course_table);
        JScrollPane sp = new JScrollPane(course_table);
        sp.getViewport().setBackground(Theme.PRIMARY_WHITE);
        sp.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        add(sp, BorderLayout.CENTER);

        //status bar placed in the south of the interface
        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(Theme.PRIMARY_WHITE);
        status_label.setFont(Theme.FONT_SMALL);
        south.add(status_label, BorderLayout.WEST);
        add(south, BorderLayout.SOUTH);

        status_label.setText(courseTable_list.size() + " courses");

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

    public static void styleTable(JTable table) {
        table.setFont(Theme.FONT_TEXT);
        table.setRowHeight(30); // Taller rows for better readability
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(Theme.SEA_GREEN.darker());
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Theme.SEA_GREEN);
        header.setForeground(Theme.TEXT_DARK);
        header.setPreferredSize(new Dimension(0, 35)); // Taller header
    }

}

