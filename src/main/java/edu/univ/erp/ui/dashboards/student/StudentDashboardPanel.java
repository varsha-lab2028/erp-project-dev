package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.domain.SemesterSeason;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.auth.session.Session;

import edu.univ.erp.ui.common.BaseDashboard;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import java.awt.*;

public class StudentDashboardPanel extends BaseDashboard{
    private final JPanel content_area;
    private final CardLayout card_layout;
    private final StudentService student_service;

    //these methods will fetch data about the currently logged in student
    private long loggedStudentId() { return Session.userId(); }
    private SemesterSeason loggedSemSeason() { return Session.getSemesterSeason(); }
    private int loggedSemNo() { return Session.getSemesterNumber(); }
    private int loggedYear() { return Session.getTermYear(); }

    JPanel actions = new JPanel(null);

    //constructors
    public StudentDashboardPanel() {
        this(new StudentService());
    }

    public StudentDashboardPanel(StudentService studentService){
        super("Student Dashboard");
        this.student_service = studentService;
        //using the content panel from BaseDashboard
        content.setLayout(new BorderLayout());

        //sidebar
        DashboardComponents.SidebarPanel sidebar = new DashboardComponents.SidebarPanel("STUDENT", this::onNavigate);
        sidebar.addItem("Course Catalog", "📚");
        sidebar.addItem("Section Catalog", "🧩");
        sidebar.addItem("My Registrations", "✅");
        sidebar.addItem("Timetable", "📅");
        sidebar.addItem("Grades", "📊");
        sidebar.addItem("Transcript", "📜");

        content.add(sidebar, BorderLayout.WEST);

        //content area
        card_layout = new CardLayout();
        content_area = new JPanel(card_layout);
        content_area.setBackground(DashboardTheme.BG_LIGHT);

        //buttons
        content_area.add(new StudentCoursePanel(), "Course Catalog");
        content_area.add(new StudentSectionPanel(), "Section Catalog");
        content_area.add(new StudentRegistrationsPanel(), "My Registrations");
        content_area.add(new StudentTimetablePanel(), "Timetable");
        content_area.add(new StudentGradesPanel(), "Grades");
        content_area.add(new StudentTranscriptPanel(), "Transcript");

        //top bar + content area
        JPanel main_container = new JPanel(new BorderLayout());
        main_container.add(new DashboardComponents.TopBarPanel("Student Portal", "ST"), BorderLayout.NORTH);
        main_container.add(content_area, BorderLayout.CENTER);
        content.add(main_container, BorderLayout.CENTER);
    }

    private void onNavigate(java.awt.event.ActionEvent e) {
        String screen_name = e.getActionCommand();
        card_layout.show(content_area, screen_name);
    }
}
