package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.domain.Course;
import edu.univ.erp.service.StudentService;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.util.RoundedButton;

import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class StudentCoursePanel extends JPanel{
    private final JTable course_table;
    private final StudentService student_service = new StudentService();
    private final JTextField search_field;
    private final JLabel status_label;

    //constructor
    public StudentCoursePanel(){
        setLayout(new BorderLayout(20, 20)); //gaps for better spacing
        setBackground(DashboardTheme.BG_LIGHT);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        //UI for the top search bar
        JPanel topCard = new DashboardComponents.RoundedPanel(15, Color.WHITE, true);
        topCard.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

        search_field = new JTextField(20);
        search_field.setFont(DashboardTheme.FONT_REGULAR);
        RoundedButton search_button = new RoundedButton("Search");

        JLabel search_label = new JLabel("Search:");
        search_label.setFont(DashboardTheme.FONT_REGULAR);
        topCard.add(search_label);
        topCard.add(search_field);
        topCard.add(search_button);
        add(topCard, BorderLayout.NORTH);

        //UI for the status label which will be placed at the south part of interface
        status_label = new JLabel(" ");
        status_label.setFont(DashboardTheme.FONT_SMALL);
        add(status_label, BorderLayout.SOUTH);

        //loading data into the course catalog table
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

        course_table = new JTable(model);
        //styling the table according to the theme
        course_table.setFont(DashboardTheme.FONT_REGULAR);
        course_table.setRowHeight(35);
        course_table.setShowGrid(false);
        course_table.setIntercellSpacing(new Dimension(0, 5));
        course_table.getTableHeader().setBackground(Color.WHITE);
        course_table.getTableHeader().setForeground(DashboardTheme.TEXT_SECONDARY);
        course_table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        course_table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, DashboardTheme.BORDER_GRAY));
        course_table.setSelectionBackground(new Color(200, 230, 201, 50));
        course_table.setSelectionForeground(DashboardTheme.TEXT_PRIMARY);

        JScrollPane sp = new JScrollPane(course_table);
        sp.getViewport().setBackground(Color.WHITE);
        sp.setBorder(BorderFactory.createEmptyBorder());

        //to give the card type look to it
        JPanel tableCard = new DashboardComponents.RoundedPanel(15, Color.WHITE, true);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));
        tableCard.add(sp, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);

        //information which will show up in the status label
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

}

