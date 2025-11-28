package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.univ.erp.service.MaintenanceService;
import edu.univ.erp.service.AdminService;

public class AdminDashboardPanel extends JPanel {
    
    private MaintenanceService maintenanceService;
    private AdminService adminService;
    private boolean isSidebarCollapsed = false;
    private String currentScreen = "Dashboard";

    public AdminDashboardPanel(MaintenanceService maintenanceService, AdminService adminService) {
        this.maintenanceService = maintenanceService;
        this.adminService = adminService;
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
            "Admin Portal", 
            "AD", 
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

       
        contentArea.add(new AdminHomePanel(adminService), "Dashboard"); // Updated
        contentArea.add(new AdminProfilePanel(), "Profile");
        contentArea.add(new UserManagementPanel(), "Students");
        contentArea.add(new CourseManagementPanel(adminService), "Courses");
        contentArea.add(new SectionManagementPanel(adminService), "Sections"); // Updated

        contentArea.add(new MaintenancePanel(true, maintenanceService, adminService), "Settings");
        
        

        contentArea.add(new MaintenancePanel(true, maintenanceService, adminService), "Settings");

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
            addButton("Students", "🎓");
            addButton("Courses", "📖");
            addButton("Sections", "📅");
            add(Box.createVerticalGlue());
            addButton("Settings", "⚙️");
            
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