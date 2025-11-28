package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.auth.session.Session;
import edu.univ.erp.ui.dashboards.admin.MaintenancePanel;
import edu.univ.erp.ui.dashboards.admin.AdminProfilePanel;

import javax.swing.*;
import java.awt.*;

public class StudentDashboardPanel extends JPanel {
    private JPanel contentArea;
    private CardLayout cardLayout;
    private final StudentService studentService;


    public StudentDashboardPanel(StudentService studentService) {
        this.studentService = studentService;
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        removeAll(); 

        DashboardComponents.SidebarPanel sidebar = new DashboardComponents.SidebarPanel("STUDENT", e -> onNavigate(e.getActionCommand()));
        
        sidebar.addItem("Dashboard", "🏠");
        sidebar.addItem("Course Catalog", "📚");
        sidebar.addItem("Section Catalog", "🗂️");
        sidebar.addItem("Registrations", "📝");
        sidebar.addItem("Grades", "🎓");
        sidebar.addItem("Time Table", "📅");
        sidebar.addItem("Transcript", "📜");
        sidebar.addItem("Settings", "⚙️");
        
        add(sidebar, BorderLayout.WEST);

        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(DashboardTheme.BG_MAIN);

        DashboardComponents.TopBarPanel topBar = new DashboardComponents.TopBarPanel(
                "Student Portal",
                "ST", 
                e -> { /* Sidebar toggle logic */ },
                e -> onNavigate("Profile"),
                e -> toggleTheme()
        );
        mainArea.add(topBar, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(DashboardTheme.BG_MAIN);

        contentArea.add(new StudentHomePanel(), "Dashboard");
        contentArea.add(new StudentCoursePanel(studentService), "Course Catalog");
        contentArea.add(new StudentSectionPanel(), "Section Catalog");
        contentArea.add(new StudentRegistrationsPanel(), "Registrations");
        contentArea.add(new StudentGradesPanel(), "Grades");

        contentArea.add(new StudentTimetablePanel(), "Time Table");
        contentArea.add(new StudentTranscriptPanel(), "Transcript");

        contentArea.add(new AdminProfilePanel(), "Profile");
        contentArea.add(new MaintenancePanel(false, null, null), "Settings");

        cardLayout.show(contentArea, "Dashboard");
        
        mainArea.add(contentArea, BorderLayout.CENTER);
        add(mainArea, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
    
    private void onNavigate(String screenName) {
        try {
            cardLayout.show(contentArea, screenName);
        } catch (Exception e) {
            System.err.println("Screen not found: " + screenName);
        }
    }

    private void toggleTheme() {
        DashboardTheme.setTheme(!DashboardTheme.isDark);
        initUI(); 
    }
}