package edu.univ.erp.ui.login;

import edu.univ.erp.service.*;
import edu.univ.erp.domain.AuthClass;
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.ui.login.LoginController;
import edu.univ.erp.ui.login.LoginPanel;
import edu.univ.erp.ui.dashboards.admin.AdminDashboardPanel;
import edu.univ.erp.ui.dashboards.instructor.InstructorDashboardPanel;
import edu.univ.erp.ui.dashboards.student.StudentDashboardPanel;
import edu.univ.erp.auth.AuthenticationService;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final JPanel maintenanceBanner;
    
    // Services
    private final StudentService studentService = new StudentService();
    private final MaintenanceService maintenanceService = new MaintenanceService();
    private final AdminService adminService = new AdminService();
    private final AuthenticationService authenticationService = new AuthenticationService() {
        @Override
        public AuthClass login(String username, String password) throws Exception {
            return null;
        }
    };
    private final InstructorService instructorService = new InstructorService();
    private final LoginController loginController = new LoginController(authenticationService);

    public MainFrame() {
        setTitle("IIITD ERP System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);
        

        setLayout(new BorderLayout());
        
   
        maintenanceBanner = new JPanel();
        maintenanceBanner.setBackground(DashboardTheme.DANGER);
        
     
        maintenanceBanner.setPreferredSize(new Dimension(100, 30));
        

        JLabel mLabel = new JLabel("⚠️ SYSTEM IS IN MAINTENANCE MODE - READ ONLY ACCESS");
        mLabel.setForeground(Color.WHITE);
        mLabel.setFont(DashboardTheme.FONT_BOLD);
        maintenanceBanner.add(mLabel);
        

        maintenanceBanner.setVisible(false); 
        add(maintenanceBanner, BorderLayout.NORTH);


        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        

        mainPanel.add(new LoginPanel(loginController), "LOGIN");
        mainPanel.add(new AdminDashboardPanel(maintenanceService, adminService), "ADMIN");
        mainPanel.add(new InstructorDashboardPanel(instructorService, maintenanceService), "INSTRUCTOR");
        mainPanel.add(new StudentDashboardPanel(studentService), "STUDENT");

        add(mainPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "LOGIN");
    }

    public AuthClass authenticate(String username, String password, String selectedRole) {
        return loginController.authenticate(username, password, selectedRole);
    }

    public void showDashboard(String role) {
        switch (role) {
            case "ADMIN" -> cardLayout.show(mainPanel, "ADMIN");
            case "INSTRUCTOR" -> cardLayout.show(mainPanel, "INSTRUCTOR");
            case "STUDENT" -> cardLayout.show(mainPanel, "STUDENT");
        }
        checkMaintenance();
    }

    public void checkMaintenance() {
     
        boolean isMaintenance = maintenanceService.isMaintenanceOn();
        maintenanceBanner.setVisible(isMaintenance);
    }

}