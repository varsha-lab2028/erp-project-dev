package edu.univ.erp.ui.dashboards.instructor;

import edu.univ.erp.domain.Section;
import edu.univ.erp.service.InstructorService;
import edu.univ.erp.data.CourseDAO;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.sql.SQLException;

public class InstructorSectionsPanel extends JPanel {
    private final edu.univ.erp.service.InstructorService instructorService;
    private final CourseDAO courseDAO = new CourseDAO();

    public InstructorSectionsPanel(InstructorService instructorService) {
        this.instructorService = instructorService;
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_MAIN); 
        setBorder(new EmptyBorder(30, 30, 30, 30));

     
        String[] cols = {"Course Code", "Course Name", "Section", "Classroom", "Schedule", "Capacity", "Enrolled"};
        Object[][] data;
        long instructor_id = 2L;
        try {
     
            List<Section> sections = instructorService.getSectionsByInstructor(instructor_id);
            data = new Object[sections.size()][cols.length];

            for (int i = 0; i < sections.size(); i++) {
                Section s = sections.get(i);
                data[i][0] = s.getCourseCode();
             
                data[i][1] = courseDAO.findNameByCourseCode(s.getCourseCode());
                data[i][2] = s.getSectionId();
                data[i][3] = s.getClassroom();
                data[i][4] = s.getDay() + " " + s.getTimings();
                data[i][5] = s.getCapacity();
                int enrolledCount = 0;
                try {
                    enrolledCount = instructorService.getEnrolledStudentsForSection(s.getSectionId()).size();
                } catch (SQLException ignored) {
                  
                }
                data[i][6] = enrolledCount;
            }
        } catch (SQLException ex) {
           
            data = new Object[0][cols.length];
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load sections for instructor: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        DashboardComponents.TablePanel table = new DashboardComponents.TablePanel("All Assigned Sections", cols, data);
        add(table, BorderLayout.CENTER);
    }
}