package edu.univ.erp.ui.admin;

import javax.swing.*;
import java.awt.*;

/**
 * The main dashboard for the Admin user.
 * It uses a JTabbedPane to host the different management panels. 
 */
public class AdminDashboard extends JPanel {

    private JTabbedPane tabbedPane;
    private UserManagementPanel userManagementPanel;
    private CourseManagementPanel courseManagementPanel;
    private SectionManagementPanel sectionManagementPanel;
    private MaintenancePanel maintenancePanel;

    public AdminDashboard() {
        // Set the layout for this main panel
        setLayout(new BorderLayout());
        
        // Initialize components
        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        // Create each tab panel
        userManagementPanel = new UserManagementPanel();
        courseManagementPanel = new CourseManagementPanel();
        sectionManagementPanel = new SectionManagementPanel();
        maintenancePanel = new MaintenancePanel();

        // Add panels as tabs
        tabbedPane.addTab("User Management", userManagementPanel); // 
        tabbedPane.addTab("Course Management", courseManagementPanel); // 
        tabbedPane.addTab("Section Management", sectionManagementPanel); // 
        tabbedPane.addTab("Maintenance", maintenancePanel); // 

        // Add the tabbed pane to the center of this panel
        add(tabbedPane, BorderLayout.CENTER);
    }
}