package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.domain.SemesterSeason;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.auth.session.Session;

import edu.univ.erp.ui.common.BaseDashboard;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import edu.univ.erp.util.RoundedButton;
import edu.univ.erp.util.Theme;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends BaseDashboard{
    private final StudentService student_service = new StudentService();

    //these methods will fetch data about the currently logged in student
    private long studentId() { return Session.userId(); }
    private SemesterSeason semSeason() { return Session.getSemesterSeason(); }
    private int semNo() { return Session.getSemesterNumber(); }
    private int year() { return Session.getTermYear(); }

    JPanel actions = new JPanel(null);

    //constructor
    public StudentDashboard(){
        super("ERP Student Dashboard");
        //actions.setBackground(new Color(0,0,82));
        //actions.setPreferredSize(new Dimension(900, 60));

        //creating a navigation panel
        JPanel navigation_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        navigation_panel.setBackground(Theme.PRIMARY_WHITE);
        //adding border to separate the navigation from the content
        navigation_panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.SEA_GREEN));

        //creating buttons
        RoundedButton course_catalog = new RoundedButton("Course Catalog");
        RoundedButton section_catalog = new RoundedButton("Section Catalog");
        RoundedButton regs = new RoundedButton("My Registrations");
        RoundedButton timetable = new RoundedButton("Timetable");
        RoundedButton grades = new RoundedButton("Grades");
        RoundedButton transcript = new RoundedButton("Transcript");

        //standardizing the button heights
        Dimension standard_size = new Dimension(140, 35);
        course_catalog.setPreferredSize(standard_size);
        section_catalog.setPreferredSize(standard_size);
        regs.setPreferredSize(standard_size);
        //making the simple tabs smaller
        timetable.setPreferredSize(new Dimension(110, 35));
        grades.setPreferredSize(new Dimension(100, 35));
        transcript.setPreferredSize(new Dimension(110, 35));

        //add to the panel
        navigation_panel.add(course_catalog);
        navigation_panel.add(section_catalog);
        navigation_panel.add(regs);
        navigation_panel.add(timetable);
        navigation_panel.add(grades);
        navigation_panel.add(transcript);

        //adding the navigation panel to the header
        header_stack.add(navigation_panel);

        //this will be default panel when you open the student dashboard
        setCenter(new StudentCoursePanel());

        //switching buttons when you click on them
        course_catalog.addActionListener(e -> setCenter(new StudentCoursePanel()));
        section_catalog.addActionListener(e -> setCenter(new StudentSectionPanel()));
        regs.addActionListener(e -> setCenter(new StudentRegistrationsPanel()));
        timetable.addActionListener(e -> setCenter(new StudentTimetablePanel()));
        grades.addActionListener(e -> setCenter(new StudentGradesPanel()));
        transcript.addActionListener(e -> setCenter(new StudentTranscriptPanel()));
    }
}
