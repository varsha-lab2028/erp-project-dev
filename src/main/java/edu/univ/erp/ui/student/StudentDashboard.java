package edu.univ.erp.ui.student;
import edu.univ.erp.domain.SemesterSeason;
import edu.univ.erp.ui.common.BaseDashboard;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.auth.session.Session;
import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends BaseDashboard{
    private final StudentService student_flow_controls = new StudentService();

    //the methods fetch data about the currently logged in student
    private long studentId() { return Session.userId(); }
    private SemesterSeason semSeason() { return Session.getSemesterSeason(); }
    private int semNo() { return Session.getSemesterNumber(); }
    private int year() { return Session.getTermYear(); }

    JPanel actions = new JPanel(null);

    //constructor
    public StudentDashboard(){
        super("ERP Student Dashboard");
        actions.setBackground(new Color(0,0,82));

        actions.setPreferredSize(new Dimension(900, 60));
        JButton course_catalog = new JButton("Course Catalog");
        JButton section_catalog = new JButton("Section Catalog");
        JButton regs = new JButton("My Registrations");
        JButton timetable = new JButton("Timetable");
        JButton grades = new JButton("Grades");
        JButton transcript = new JButton("Transcript");

        // Place with setBounds
        course_catalog.setBounds(10, 10, 150, 30);
        regs.setBounds(170, 10, 150, 30);
        timetable.setBounds(330, 10, 150, 30);
        grades.setBounds(490, 10, 150, 30);
        transcript.setBounds(650, 10, 150, 30);

        actions.add(course_catalog); actions.add(regs); actions.add(timetable); actions.add(grades); actions.add(transcript);
        add(actions, BorderLayout.NORTH);

        // Default panel
        setCenter(new StudentCoursePanel());

        // Switchers
        course_catalog.addActionListener(e -> setCenter(new StudentCoursePanel()));
        //section_catalog.addActionListener(e -> setCenter(new StudentSectionPanel()));
        regs.addActionListener(e -> setCenter(new StudentRegistrationsPanel()));
        timetable.addActionListener(e -> setCenter(new StudentTimetablePanel()));
        grades.addActionListener(e -> setCenter(new StudentGradesPanel()));
        transcript.addActionListener(e -> setCenter(new StudentTranscriptPanel()));
    }
}
