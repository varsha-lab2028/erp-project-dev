package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.domain.Section;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.sql.SQLException;

public class StudentSectionPanel extends JPanel {
    private StudentService student_service = new StudentService();
    private JPanel contentPanel;
    private List<Section> current_sections;
    
    // We need to keep track of the table to get selected rows
    // Since TablePanel encapsulates JTable, for this specific panel where we need interaction,
    // we might need to modify TablePanel or just use a custom implementation using style helpers.
    // For simplicity, I will use a custom implementation of the table here to support selection.
    private JTable sectionTable;

    public StudentSectionPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // 1. Top Bar
        DashboardComponents.CardPanel topCard = new DashboardComponents.CardPanel();
        topCard.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        JTextField searchField = new JTextField(20);
        DashboardComponents.styleControl(searchField);

        JButton searchBtn = DashboardComponents.createPrimaryButton("Search");
        JButton registerBtn = DashboardComponents.createPrimaryButton("Register Selected");

        topCard.add(new JLabel("Search: "));
        topCard.add(searchField);
        topCard.add(searchBtn);
        topCard.add(Box.createHorizontalStrut(20));
        topCard.add(registerBtn);
        
        add(topCard, BorderLayout.NORTH);

        // 2. Table Area
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        add(contentPanel, BorderLayout.CENTER);

        // Load Data
        reloadSections("");

        // Listeners
        searchBtn.addActionListener(e -> reloadSections(searchField.getText().trim()));
        registerBtn.addActionListener(e -> registerSelected());
    }

    private void reloadSections(String keyword) {
        try {
            current_sections = student_service.browseSectionCatalog(keyword);
        } catch (SQLException e) {
            current_sections = Collections.emptyList();
        }

        String[] cols = {"Section", "Instructor", "Day", "Timings", "Room", "Cap"};
        Object[][] data = new Object[current_sections.size()][cols.length];

        for (int i = 0; i < current_sections.size(); i++) {
            Section s = current_sections.get(i);
            data[i][0] = s.getDisplayName();
            data[i][1] = s.getInstructorName();
            data[i][2] = s.getDay();
            data[i][3] = s.getTimings();
            data[i][4] = s.getClassroom();
            data[i][5] = s.getCapacity();
        }

        // Custom Table Creation using Dashboard Styles
        DashboardComponents.CardPanel tableCard = new DashboardComponents.CardPanel();
        tableCard.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("Available Sections");
        title.setFont(DashboardTheme.FONT_SUBTITLE);
        title.setForeground(DashboardTheme.TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(15, 20, 15, 20));
        tableCard.add(title, BorderLayout.NORTH);

        sectionTable = new JTable(data, cols);
        sectionTable.setRowHeight(35);
        sectionTable.setFont(DashboardTheme.FONT_REGULAR);
        sectionTable.getTableHeader().setFont(DashboardTheme.FONT_BOLD);
        
        // Colors
        sectionTable.setBackground(DashboardTheme.SURFACE);
        sectionTable.setForeground(DashboardTheme.TEXT_PRIMARY);
        sectionTable.setGridColor(DashboardTheme.BORDER_COLOR);
        
        JScrollPane sp = new JScrollPane(sectionTable);
        sp.getViewport().setBackground(DashboardTheme.SURFACE);
        sp.setBorder(null);
        
        tableCard.add(sp, BorderLayout.CENTER);
        
        contentPanel.removeAll();
        contentPanel.add(tableCard, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void registerSelected() {
        int row = sectionTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a section.");
            return;
        }
        Section s = current_sections.get(row);
        long student_id = 3L; 

        try {
            student_service.registerForSection(student_id, s.getSectionId());
            JOptionPane.showMessageDialog(this, "Registered for " + s.getCourseCode());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}