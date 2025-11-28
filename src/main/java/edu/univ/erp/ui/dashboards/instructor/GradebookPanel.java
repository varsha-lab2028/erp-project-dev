package edu.univ.erp.ui.dashboards.instructor;

import edu.univ.erp.data.ServerConnector;
import edu.univ.erp.domain.Section;
import edu.univ.erp.service.InstructorService;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GradebookPanel extends JPanel {
    private final InstructorService instructorService;

    
    private JComboBox<String> sectionCombo;
    private JPanel tableContainer;
    private List<Section> currentSections;

    
    private static final String[] cols = {
            "Student ID", "Name", "Midsem (30)", "Endsem (30)", "Internal (20)", "Quizzes (20)", "Total", "Grade"
    };

    public GradebookPanel(edu.univ.erp.service.InstructorService instructorService) {
        this.instructorService = instructorService;

        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        
        DashboardComponents.CardPanel controls = new DashboardComponents.CardPanel();
        controls.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

        JLabel lbl = new JLabel("Select Section:");
        lbl.setFont(DashboardTheme.FONT_BOLD);
        lbl.setForeground(DashboardTheme.TEXT_PRIMARY);

        sectionCombo = new JComboBox<>(new String[]{"IP - Section A", "IP - Section B", "LA - Section A", "LA - Section B", "HCI - Section A", "HCI - Section B", "DC - Section A", "DC - Section B", "COM"});
        sectionCombo.setPreferredSize(new Dimension(250, 35));
        DashboardComponents.styleControl(sectionCombo);

        JButton loadBtn = DashboardComponents.createPrimaryButton("Load Data");

        controls.add(lbl);
        controls.add(sectionCombo);
        controls.add(loadBtn);
        add(controls, BorderLayout.NORTH);

        
        tableContainer = new JPanel(new BorderLayout());
        tableContainer.setOpaque(false);

        Object[][] emptyData = new Object[0][cols.length];
        DashboardComponents.TablePanel gradeTable =
                new DashboardComponents.TablePanel("Student Grades", cols, emptyData);
        tableContainer.add(gradeTable, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);

        
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(DashboardTheme.BG_MAIN);

        JButton exportBtn = new JButton("Export CSV");
        exportBtn.setFont(DashboardTheme.FONT_BOLD);
        exportBtn.setForeground(DashboardTheme.TEXT_PRIMARY);
        exportBtn.setBackground(DashboardTheme.SURFACE);
        exportBtn.setBorder(BorderFactory.createLineBorder(DashboardTheme.BORDER_COLOR));
        exportBtn.setPreferredSize(new Dimension(120, 40));
        exportBtn.setFocusPainted(false);

        JButton saveBtn = DashboardComponents.createPrimaryButton("Publish Grades");
        saveBtn.setBackground(DashboardTheme.SUCCESS);

        bottom.add(exportBtn);
        bottom.add(Box.createHorizontalStrut(10));
        bottom.add(saveBtn);

        add(bottom, BorderLayout.SOUTH);

        
        long instructor_id = 2L; 
        
        loadSectionsForInstructor(instructor_id);

        
        loadBtn.addActionListener(e -> {
            Section s = getSelectedSection();
            if (s == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a section.",
                        "No Section Selected",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            loadGradesForSection(s.getSectionId());
        });

        
        saveBtn.addActionListener(e -> {
            Section s = getSelectedSection();
            if (s == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a section.",
                        "No Section Selected",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            try {
                instructorService.computeFinalGrades(s.getSectionId(), instructor_id);
                JOptionPane.showMessageDialog(
                        this,
                        "Final grades computed and saved.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                loadGradesForSection(s.getSectionId());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        
        exportBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "Export CSV is not implemented yet.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE
                ));
    }

    private void loadSectionsForInstructor(long instructorId) {
        try {
            currentSections = instructorService.getSectionsByInstructor(instructorId);
            sectionCombo.removeAllItems();
            for (Section s : currentSections) {
                String label = s.getCourseCode() + " - Sec " + s.getSectionId();
                sectionCombo.addItem(label);
            }
        } catch (SQLException e) {
            currentSections = null;
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load sections: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private Section getSelectedSection() {
        if (currentSections == null || currentSections.isEmpty()) return null;
        int idx = sectionCombo.getSelectedIndex();
        if (idx < 0 || idx >= currentSections.size()) return null;
        return currentSections.get(idx);
    }

    private void loadGradesForSection(long sectionId) {
        Object[][] data;

        try {
            
            List<Object[]> rows = instructorService.getGradesForSection(sectionId);
            data = new Object[rows.size()][cols.length];

            for (int i = 0; i < rows.size(); i++) {
                Object[] r = rows.get(i);

                // Our DAO returns: [id, name, midsem, endsem, internal, quizzes, total, grade]
                data[i][0] = r[0];  // Student ID
                data[i][1] = r[1];  // Name (roll no)
                data[i][2] = r[2];  // Midsem
                data[i][3] = r[3];  // Endsem
                data[i][4] = r[4];  // Internal / Assignments
                data[i][5] = r[5];  // Quizzes
                data[i][6] = r[6];  // Total (course_cg)
                data[i][7] = r[7];  // Grade (letter)
            }

        } catch (SQLException e) {
            
            data = new Object[0][cols.length];
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load grades: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        tableContainer.removeAll();
        DashboardComponents.TablePanel newTable =
                new DashboardComponents.TablePanel("Student Grades", cols, data);
        tableContainer.add(newTable, BorderLayout.CENTER);
        tableContainer.revalidate();
        tableContainer.repaint();
    }


}
