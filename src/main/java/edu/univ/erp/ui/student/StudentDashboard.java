package edu.univ.erp.ui.student;
import edu.univ.erp.access.AccessControl;
import edu.univ.erp.ui.common.BaseDashboard;
import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends BaseDashboard{
    private final JPanel actions = new JPanel(null);
    public StudentDashboard(){
        super("Student Dashboard");

        actions.setPreferredSize(new Dimension(900, 60));
        JButton browse = new JButton("Browse Catalog");
        JButton regs = new JButton("My Registrations");
        JButton timetable = new JButton("Timetable");
        JButton grades = new JButton("Grades");
        JButton transcript = new JButton("Transcript");

        // Place with setBounds
        browse.setBounds(10, 10, 150, 30);
        regs.setBounds(170, 10, 150, 30);
        timetable.setBounds(330, 10, 150, 30);
        grades.setBounds(490, 10, 150, 30);
        transcript.setBounds(650, 10, 150, 30);

        actions.add(browse); actions.add(regs); actions.add(timetable); actions.add(grades); actions.add(transcript);
        add(actions, BorderLayout.NORTH);

        // Default panel
        /*
        setCenter(new StudentCatalogPanel());

        // Switchers
        browse.addActionListener(e -> setCenter(new StudentCatalogPanel()));
        regs.addActionListener(e -> setCenter(new StudentRegistrationsPanel()));
        timetable.addActionListener(e -> setCenter(new StudentTimetablePanel()));
        grades.addActionListener(e -> setCenter(new StudentGradesPanel()));
        transcript.addActionListener(e -> setCenter(new StudentTranscriptPanel()));
         */
    }
}
