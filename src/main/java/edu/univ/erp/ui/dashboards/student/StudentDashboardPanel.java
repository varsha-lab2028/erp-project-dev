package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.ui.dashboards.admin.MaintenancePanel;
import edu.univ.erp.ui.dashboards.admin.AdminProfilePanel;

import javax.swing.*;
import java.awt.*;

public class StudentDashboardPanel extends JPanel {
    private JPanel contentArea;
    private CardLayout cardLayout;
    private final StudentService studentService;
    private String currentScreen = "Dashboard"; // To hold the current view

    public StudentDashboardPanel(StudentService studentService) {
        this.studentService = studentService;
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        removeAll(); // Clear previous components on re-init

        // 1. Sidebar
        DashboardComponents.SidebarPanel sidebar = new DashboardComponents.SidebarPanel("STUDENT", e -> onNavigate(e.getActionCommand()));
        sidebar.addItem("Dashboard", "🏠");
        sidebar.addItem("Transcript", "📜");
        sidebar.addItem("Register", "✍️");
        sidebar.addItem("Profile", "👤");
        sidebar.addItem("Settings", "⚙️");
        add(sidebar, BorderLayout.WEST);

        // 2. Main Area (Top Bar + Content)
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(DashboardTheme.BG_MAIN);

        // 3. Top Bar
        DashboardComponents.TopBarPanel topBar = new DashboardComponents.TopBarPanel(
                "Student Portal",
                "ST", // Placeholder for student initials
                e -> { /* Sidebar toggle logic if needed */ },
                e -> onNavigate("Profile"),
                e -> toggleTheme()
        );
        mainArea.add(topBar, BorderLayout.NORTH);

        // 4. Content Cards
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(DashboardTheme.BG_MAIN);

        contentArea.add(new StudentHomePanel(), "Dashboard");
        contentArea.add(new StudentCoursePanel(), "Course Catalog");
        contentArea.add(new StudentSectionPanel(), "Section Catalog");
        contentArea.add(new StudentRegistrationsPanel(), "Registrations");
        contentArea.add(new StudentGradesPanel(), "Grades");
        //contentArea.add(new StudentTimetablePanel(), "Time Table");
        contentArea.add(new StudentTranscriptPanel(), "Transcript");

        // Shared Views
        contentArea.add(new AdminProfilePanel(), "Profile");
        contentArea.add(new MaintenancePanel(false, null, null), "Settings");

        cardLayout.show(contentArea, currentScreen);
        mainArea.add(contentArea, BorderLayout.CENTER);
        add(mainArea, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
    
    private void onNavigate(String screen) {
        // Switch to the selected card
        currentScreen = screen;
        cardLayout.show(contentArea, screen);
    }

    // Helper to create a placeholder panel
    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(new JLabel(text + " - Coming Soon!"));
        return panel;
    }

    private void toggleTheme() {
        DashboardTheme.setTheme(!DashboardTheme.isDark);
        initUI(); // Re-initialize the entire UI to apply the new theme
    }
}