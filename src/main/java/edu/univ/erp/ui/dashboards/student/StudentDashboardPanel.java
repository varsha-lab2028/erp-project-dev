package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.domain.SemesterSeason;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.auth.session.Session;

//import edu.univ.erp.ui.common.BaseDashboard;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import java.awt.*;

public class StudentDashboardPanel extends JPanel{
    private final JPanel contentArea;
    private final CardLayout cardLayout;
    private final StudentService student_service = new StudentService();

    //these methods will fetch data about the currently logged in student
    private long studentId() { return Session.userId(); }
    private SemesterSeason semSeason() { return Session.getSemesterSeason(); }
    private int semNo() { return Session.getSemesterNumber(); }
    private int year() { return Session.getTermYear(); }

    JPanel actions = new JPanel(null);

    //constructor
    public StudentDashboardPanel(){
        setLayout(new BorderLayout());

        //sidebar
        DashboardComponents.SidebarPanel sidebar = new DashboardComponents.SidebarPanel("STUDENT", this::onNavigate);
        sidebar.addItem("Course Catalog", "📚");
        sidebar.addItem("Section Catalog", "🧩");
        sidebar.addItem("My Registrations", "✅");
        sidebar.addItem("Timetable", "📅");
        sidebar.addItem("Grades", "📊");
        sidebar.addItem("Transcript", "📜");
        add(sidebar, BorderLayout.WEST);

        //content area
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(DashboardTheme.BG_LIGHT);

        //buttons
        contentArea.add(new StudentCoursePanel(), "Course Catalog");
        contentArea.add(new StudentSectionPanel(), "Section Catalog");
        contentArea.add(new StudentRegistrationsPanel(), "My Registrations");
        contentArea.add(new StudentTimetablePanel(), "Timetable");
        contentArea.add(new StudentGradesPanel(), "Grades");
        contentArea.add(new StudentTranscriptPanel(), "Transcript");

        //top bar
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.add(new DashboardComponents.TopBarPanel("Student Portal", "ST"), BorderLayout.NORTH);
        mainContainer.add(contentArea, BorderLayout.CENTER);
        add(mainContainer, BorderLayout.CENTER);
    }

    private void onNavigate(String screenName) {
        cardLayout.show(contentArea, screenName);
    }
}
