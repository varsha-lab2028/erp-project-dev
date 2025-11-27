package edu.univ.erp.ui.dashboards.student;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.service.StudentService;
import edu.univ.erp.ui.dashboards.admin.MaintenancePanel;
import edu.univ.erp.ui.dashboards.admin.AdminProfilePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StudentDashboardPanel extends JPanel {
    
    private StudentService studentService;
    private boolean isSidebarCollapsed = false;
    private String currentScreen = "Dashboard";

    public StudentDashboardPanel(StudentService studentService) {
        this.studentService = studentService;
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        removeAll();
        
        // 1. Sidebar
        SidebarPanel sidebar = new SidebarPanel(e -> onNavigate(e));
        sidebar.setCollapsed(isSidebarCollapsed);
        add(sidebar, BorderLayout.WEST);

        // 2. Main Area
        JPanel mainArea = new JPanel(new BorderLayout());
        
        // Top Bar
        DashboardComponents.TopBarPanel topBar = new DashboardComponents.TopBarPanel(
            "Student Portal", 
            "ST", // Student Initials
            e -> {
                isSidebarCollapsed = !isSidebarCollapsed;
                sidebar.setCollapsed(isSidebarCollapsed);
            },
            e -> onNavigate("Profile"),
            e -> toggleTheme()
        );
        mainArea.add(topBar, BorderLayout.NORTH);

        // Content Area
        CardLayout cardLayout = new CardLayout();
        JPanel contentArea = new JPanel(cardLayout);
        contentArea.setBackground(DashboardTheme.BG_MAIN);

        // --- VIEWS ---
        contentArea.add(new StudentHomePanel(), "Dashboard");
        contentArea.add(new StudentCoursePanel(), "Course Catalog");
        contentArea.add(new StudentSectionPanel(), "Section Catalog");
        contentArea.add(new StudentRegistrationsPanel(), "My Registrations");
        contentArea.add(new StudentTimetablePanel(), "Timetable");
        contentArea.add(new StudentGradesPanel(), "Grades");
        contentArea.add(new StudentTranscriptPanel(), "Transcript");
        
        // Shared Views
        contentArea.add(new AdminProfilePanel(), "Profile");
        contentArea.add(new MaintenancePanel(false, null, null), "Settings"); // False = No Maintenance Controls

        cardLayout.show(contentArea, currentScreen);
        mainArea.add(contentArea, BorderLayout.CENTER);
        
        add(mainArea, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void onNavigate(String screenName) {
        currentScreen = screenName;
        JPanel mainArea = (JPanel) getComponent(1);
        JPanel contentArea = (JPanel) mainArea.getComponent(1);
        CardLayout cl = (CardLayout) contentArea.getLayout();
        cl.show(contentArea, screenName);
    }

    private void toggleTheme() {
        DashboardTheme.setTheme(!DashboardTheme.isDark);
        initUI();
    }

    // --- Sidebar Panel ---
    private class SidebarPanel extends JPanel {
        private Consumer<String> navListener;
        private JPanel brandPanel;
        private JLabel brandLabel;
        private List<DashboardComponents.SidebarButton> buttons = new ArrayList<>();

        public SidebarPanel(Consumer<String> navListener) {
            this.navListener = navListener;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(DashboardTheme.BG_SIDEBAR);
            setPreferredSize(new Dimension(260, 800));
            
            brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 25));
            brandPanel.setBackground(DashboardTheme.BG_SIDEBAR);
            
            brandLabel = new JLabel("IIITD ERP");
            brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
            brandLabel.setForeground(Color.WHITE);
            brandPanel.add(brandLabel);
            add(brandPanel);
            add(Box.createVerticalStrut(10));

            addButton("Dashboard", "⣿");
            addButton("Course Catalog", "📚");
            addButton("Section Catalog", "🧩");
            addButton("My Registrations", "✅");
            addButton("Timetable", "📅");
            addButton("Grades", "📊");
            addButton("Transcript", "📜");
            
            add(Box.createVerticalGlue());
            addButton("Settings", "⚙");
            
            add(Box.createVerticalStrut(20));
        }

        private void addButton(String name, String icon) {
            DashboardComponents.SidebarButton btn = new DashboardComponents.SidebarButton(name, icon);
            btn.setMaximumSize(new Dimension(260, 60));
            btn.addActionListener(e -> navListener.accept(name));
            buttons.add(btn);
            add(btn);
        }
        
        public void setCollapsed(boolean collapsed) {
            setPreferredSize(new Dimension(collapsed ? 80 : 260, getHeight()));
            brandLabel.setVisible(!collapsed);
            brandPanel.setLayout(new FlowLayout(collapsed ? FlowLayout.CENTER : FlowLayout.LEFT, collapsed ? 0 : 25, 25));
            for (DashboardComponents.SidebarButton btn : buttons) {
                btn.setCollapsed(collapsed);
            }
        }
    }
}