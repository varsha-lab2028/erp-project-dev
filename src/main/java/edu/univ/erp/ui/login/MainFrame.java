package edu.univ.erp.ui.login;

import edu.univ.erp.service.*;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.ui.login.LoginPanel;
import edu.univ.erp.ui.dashboards.admin.AdminDashboardPanel;
import edu.univ.erp.ui.dashboards.instructor.InstructorDashboardPanel;
import edu.univ.erp.ui.dashboards.student.StudentDashboardPanel;
import edu.univ.erp.ui.dashboards.instructor.InstructorDashboardPanel;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements LoginPanel.MainFrameController {
    
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final JPanel maintenanceBanner;
    
    // Services
    private final StudentService studentService = new StudentService();
    private final MaintenanceService maintenanceService = new MaintenanceService();
    private final AdminService adminService = new AdminService();
    private final InstructorService instructorService = new InstructorService();

    public MainFrame() {
        setTitle("IIITD ERP System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);
        
        // Root Layout
        setLayout(new BorderLayout());
        
        // 1. Maintenance Banner
        maintenanceBanner = new JPanel();
        maintenanceBanner.setBackground(DashboardTheme.DANGER);
        
        // FIX 1: Removed 'width:' and 'height:' hints
        maintenanceBanner.setPreferredSize(new Dimension(100, 30));
        
        // FIX 2: Removed 'text:' hint
        JLabel mLabel = new JLabel("⚠️ SYSTEM IS IN MAINTENANCE MODE - READ ONLY ACCESS");
        mLabel.setForeground(Color.WHITE);
        mLabel.setFont(DashboardTheme.FONT_BOLD);
        maintenanceBanner.add(mLabel);
        
        // FIX 3: Removed 'aFlag:' hint
        maintenanceBanner.setVisible(false); 
        add(maintenanceBanner, BorderLayout.NORTH);

        // 2. Card Layout for Screens
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // 3. Add Screens
        // FIX 4: Removed 'constraints:' hint from all lines below
        mainPanel.add(new LoginPanel(this), "LOGIN");
        mainPanel.add(new AdminDashboardPanel(maintenanceService, adminService), "ADMIN");
        mainPanel.add(new InstructorDashboardPanel(instructorService), "INSTRUCTOR");
        mainPanel.add(new StudentDashboardPanel(studentService), "STUDENT");

        add(mainPanel, BorderLayout.CENTER);
        
        // Show Login First
        // FIX 5: Removed 'name:' hint
        cardLayout.show(mainPanel, "LOGIN");
    }

    @Override
    public void loginSuccess(String role) {
        // Switch view based on role
        switch (role) {
            case "ADMIN" -> cardLayout.show(mainPanel, "ADMIN");
            case "INSTRUCTOR" -> cardLayout.show(mainPanel, "INSTRUCTOR");
            case "STUDENT" -> cardLayout.show(mainPanel, "STUDENT");
        }
        
        // Check maintenance mode on login
        checkMaintenance();
    }
    
    public void checkMaintenance() {
        // Logic to toggle banner based on service
        // boolean isMaintenance = maintenanceService.isMaintenanceMode();
        // maintenanceBanner.setVisible(isMaintenance);
    }

    public static void main(String[] args) {
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        } catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}