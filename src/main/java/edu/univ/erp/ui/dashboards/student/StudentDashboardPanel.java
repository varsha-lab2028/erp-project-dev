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

        // 1. Sidebar
        // The strings here (e.g., "Course Catalog") must match the keys used in contentArea.add() below
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

        // 2. Main Area (Top Bar + Content)
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(DashboardTheme.BG_MAIN);

        // 3. Top Bar
        DashboardComponents.TopBarPanel topBar = new DashboardComponents.TopBarPanel(
                "Student Portal",
                "ST", 
                e -> { /* Sidebar toggle logic */ },
                e -> onNavigate("Profile"),
                e -> toggleTheme()
        );
        mainArea.add(topBar, BorderLayout.NORTH);

        // 4. Content Cards
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(DashboardTheme.BG_MAIN);

        // --- ADD PANELS WITH EXACT MATCHING NAMES ---
        contentArea.add(new StudentHomePanel(), "Dashboard");
        contentArea.add(new StudentCoursePanel(studentService), "Course Catalog");
        contentArea.add(new StudentSectionPanel(), "Section Catalog");
        contentArea.add(new StudentRegistrationsPanel(), "Registrations");
        contentArea.add(new StudentGradesPanel(), "Grades");

        contentArea.add(new StudentTimetablePanel(), "Time Table");
        contentArea.add(new StudentTranscriptPanel(), "Transcript");

        // Shared Views
        contentArea.add(new AdminProfilePanel(), "Profile");
        contentArea.add(new MaintenancePanel(false, null, null), "Settings");

        // Show default
        cardLayout.show(contentArea, "Dashboard");
        
        mainArea.add(contentArea, BorderLayout.CENTER);
        add(mainArea, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
    
    private void onNavigate(String screenName) {
        // Since we ensured the Sidebar names match the Card names exactly, 
        // we can just pass the name directly to the layout.
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