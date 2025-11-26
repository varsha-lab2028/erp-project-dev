package edu.univ.erp.ui.dashboards.instructor;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.service.InstructorService;
import edu.univ.erp.data.EnrollmentDAO;
// --- KEY IMPORTS from Admin Package ---
import edu.univ.erp.ui.dashboards.admin.AdminProfilePanel;
import edu.univ.erp.ui.dashboards.admin.MaintenancePanel; // <--- Correct Class Name

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class InstructorDashboardPanel extends JPanel {
    
    private InstructorService instructorService;
    private boolean isSidebarCollapsed = false;
    private String currentScreen = "Dashboard";

    public InstructorDashboardPanel(InstructorService instructorService) {
        this.instructorService = instructorService;
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        removeAll();
        
        SidebarPanel sidebar = new SidebarPanel(e -> onNavigate(e));
        sidebar.setCollapsed(isSidebarCollapsed);
        add(sidebar, BorderLayout.WEST);

        JPanel mainArea = new JPanel(new BorderLayout());
        
        DashboardComponents.TopBarPanel topBar = new DashboardComponents.TopBarPanel(
            "Instructor Portal", 
            "DR", 
            e -> {
                isSidebarCollapsed = !isSidebarCollapsed;
                sidebar.setCollapsed(isSidebarCollapsed);
            },
            e -> onNavigate("Profile"),
            e -> toggleTheme()
        );
        mainArea.add(topBar, BorderLayout.NORTH);

        CardLayout cardLayout = new CardLayout();
        JPanel contentArea = new JPanel(cardLayout);
        contentArea.setBackground(DashboardTheme.BG_MAIN);

        contentArea.add(new InstructorHomePanel(), "Dashboard");
        contentArea.add(new MySectionsPanel(instructorService), "My Sections");
        contentArea.add(new GradebookPanel(instructorService), "Gradebook");
        contentArea.add(new AdminProfilePanel(), "Profile");
        
        // --- USING MAINTENANCE PANEL (isAdmin = false) ---
        contentArea.add(new MaintenancePanel(false), "Settings");

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
            addButton("My Sections", "📚");
            addButton("Gradebook", "📝");
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