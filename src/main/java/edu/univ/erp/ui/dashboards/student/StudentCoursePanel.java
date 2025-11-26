package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.domain.Course;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.Collections;
import java.util.List;
import java.awt.*;
import java.sql.SQLException;

public class StudentCoursePanel extends JPanel {
    private final StudentService student_service = new StudentService();
    private JPanel contentPanel;

    public StudentCoursePanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // 1. Search Bar (Using CardPanel)
        DashboardComponents.CardPanel searchCard = new DashboardComponents.CardPanel();
        searchCard.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        JLabel searchLbl = new JLabel("Search Catalog:");
        searchLbl.setFont(DashboardTheme.FONT_BOLD);
        searchLbl.setForeground(DashboardTheme.TEXT_PRIMARY);

        JTextField searchField = new JTextField(20);
        DashboardComponents.styleControl(searchField);

        JButton searchBtn = DashboardComponents.createPrimaryButton("Search");
        
        searchCard.add(searchLbl);
        searchCard.add(searchField);
        searchCard.add(searchBtn);
        
        add(searchCard, BorderLayout.NORTH);

        // 2. Content Area for Table
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        add(contentPanel, BorderLayout.CENTER);

        // Initial Load
        loadCourses("");

        // Action Listener
        searchBtn.addActionListener(e -> loadCourses(searchField.getText().trim()));
    }

    private void loadCourses(String keyword) {
        List<Course> list;
        try {
            list = student_service.browseCourseCatalog(keyword);
        } catch (SQLException e) {
            list = Collections.emptyList();
        }

        String[] cols = {"Course Code", "Course Name", "Credits"};
        Object[][] data = new Object[list.size()][3];
        
        for (int i = 0; i < list.size(); i++) {
            Course c = list.get(i);
            data[i][0] = c.getCourseCode();
            data[i][1] = c.getName();
            data[i][2] = c.getCredits();
        }

        contentPanel.removeAll();
        contentPanel.add(new DashboardComponents.TablePanel("Course Catalog (" + list.size() + ")", cols, data));
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}