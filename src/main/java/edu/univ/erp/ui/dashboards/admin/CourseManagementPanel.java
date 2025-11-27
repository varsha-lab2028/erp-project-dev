package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.service.AdminService;
import edu.univ.erp.domain.Course;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CourseManagementPanel extends JPanel {
    private final AdminService adminService;
    private DashboardComponents.TablePanel tablePanel;

    public CourseManagementPanel(AdminService adminService) {
        this.adminService = adminService;

        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        //Top Card: Course Form
        DashboardComponents.CardPanel formCard = new DashboardComponents.CardPanel();
        formCard.setLayout(new BorderLayout());

        JLabel title = new JLabel("Manage Courses");
        title.setFont(DashboardTheme.FONT_SUBTITLE);
        title.setForeground(DashboardTheme.TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        formCard.add(title, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(1, 7, 10, 0));
        fields.setOpaque(false);
        
        fields.add(createLabel("Code:"));
        JTextField codeTxt = new JTextField();
        DashboardComponents.styleControl(codeTxt); // Fix
        fields.add(codeTxt);
        
        fields.add(createLabel("Title:"));
        JTextField titleTxt = new JTextField();
        DashboardComponents.styleControl(titleTxt); // Fix
        fields.add(titleTxt);
        
        fields.add(createLabel("Credits:"));
        JTextField creditTxt = new JTextField();
        DashboardComponents.styleControl(creditTxt); // Fix
        fields.add(creditTxt);

        JButton addBtn = DashboardComponents.createPrimaryButton("Add Course");
        addBtn.addActionListener(e -> onAddCourse(codeTxt, titleTxt, creditTxt));
        fields.add(addBtn);

        formCard.add(fields, BorderLayout.CENTER);

        //put the Course List Table in the center
        String[] columnNames = {"Course Code", "Course Name", "Credits"};  // match DB
        Object[][] data = loadCourseData();

        tablePanel = new DashboardComponents.TablePanel("Existing Courses", columnNames, data);

        add(formCard, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(DashboardTheme.FONT_BOLD);
        lbl.setForeground(DashboardTheme.TEXT_SECONDARY);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        return lbl;
    }

    //helper methods
    private Object[][] loadCourseData() {
        try {
            List<Course> courses = adminService.getAllCourses();
            Object[][] rows = new Object[courses.size()][3];
            for (int i = 0; i < courses.size(); i++) {
                Course c = courses.get(i);
                rows[i][0] = c.getCourseCode();
                rows[i][1] = c.getName();
                rows[i][2] = c.getCredits();
            }
            return rows;
        } catch (Exception e) {
            // if something fails, show error and return empty table
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load courses: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return new Object[0][3];
        }
    }

    // Called when Add button is clicked
    private void onAddCourse(JTextField codeTxt, JTextField titleTxt, JTextField creditTxt) {
        String code = codeTxt.getText().trim();
        String title = titleTxt.getText().trim();
        String creditsStr = creditTxt.getText().trim();

        if (code.isEmpty() || title.isEmpty() || creditsStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        int credits;
        try {
            credits = Integer.parseInt(creditsStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credits must be an integer.");
            return;
        }

        try {
            adminService.createCourse(code, title, credits);

            // Clear inputs
            codeTxt.setText("");
            titleTxt.setText("");
            creditTxt.setText("");

            // Refresh table: rebuild with fresh data
            remove(tablePanel);
            Object[][] data = loadCourseData();
            String[] cols = {"Course Code", "Title", "Credits"};
            tablePanel = new DashboardComponents.TablePanel("Existing Courses", cols, data);
            add(tablePanel, BorderLayout.CENTER);
            revalidate();
            repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add course: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}