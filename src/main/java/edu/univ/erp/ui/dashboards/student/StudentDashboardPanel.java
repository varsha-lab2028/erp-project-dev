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

    public StudentDashboardPanel(StudentService studentService) {
        this.studentService = studentService;
        setLayout(new BorderLayout());
        
        // 1. Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JButton btnDashboard = new JButton("🏠 Dashboard");
        btnDashboard.setActionCommand("Dashboard");
        btnDashboard.addActionListener(e -> onNavigate(e.getActionCommand()));

        JButton btnMyCourses = new JButton("📚 My Courses");
        btnMyCourses.setActionCommand("My Courses");
        btnMyCourses.addActionListener(e -> onNavigate(e.getActionCommand()));

        JButton btnTranscript = new JButton("📜 Transcript");
        btnTranscript.setActionCommand("Transcript");
        btnTranscript.addActionListener(e -> onNavigate(e.getActionCommand()));

        JButton btnRegister = new JButton("✍️ Register");
        btnRegister.setActionCommand("Register");
        btnRegister.addActionListener(e -> onNavigate(e.getActionCommand()));

        sidebar.add(btnDashboard);
        sidebar.add(btnMyCourses);
        sidebar.add(btnTranscript);
        sidebar.add(btnRegister);

        add(sidebar, BorderLayout.WEST);

        // 2. Content
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(DashboardTheme.BG_MAIN);
        
        // --- FIX IS HERE ---
        // Old (Wrong): contentArea.add(new StudentHomePanel(), constraints: "Dashboard");
        // New (Correct):
        contentArea.add(new StudentHomePanel(), "Dashboard");
        
        // Uncomment these as you create the files:
        // contentArea.add(new StudentTranscriptPanel(), "Transcript"); 
        // contentArea.add(new StudentRegistrationPanel(), "Register"); 
        // Shared Views
        contentArea.add(new AdminProfilePanel(), "Profile");
        contentArea.add(new MaintenancePanel(false, null, null), "Settings"); // False = No Maintenance Controls

        // 3. Top Bar
        JPanel mainContainer = new JPanel(new BorderLayout());
        // DashboardComponents.TopBarPanel(String, String) is not available; provide a simple top bar instead
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.add(new JLabel("Student Portal - ST"), BorderLayout.WEST);
        topBar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        mainContainer.add(topBar, BorderLayout.NORTH);
        mainContainer.add(contentArea, BorderLayout.CENTER);
        
        add(mainContainer, BorderLayout.CENTER);
    }
    
    private void onNavigate(String screen) {
        // Simple check to ensure we only switch if the card exists
        if(screen.equals("Dashboard")) {
            cardLayout.show(contentArea, screen);
        }
    }
}