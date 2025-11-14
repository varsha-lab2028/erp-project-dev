package edu.univ.erp.ui.admin;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for admin to toggle maintenance mode ON/OFF.
 */
public class MaintenancePanel extends JPanel {

    private JToggleButton maintenanceToggleButton;
    private JLabel statusLabel;

    public MaintenancePanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        initComponents();
    }

    private void initComponents() {
        // TODO: The toggle's initial state should be loaded from SettingsDAO
        boolean currentStatus = false; // Dummy value

        maintenanceToggleButton = new JToggleButton("Enable Maintenance Mode");
        maintenanceToggleButton.setSelected(currentStatus);

        statusLabel = new JLabel("Current Status: " + (currentStatus ? "ON" : "OFF"));
        
        // Add listener to update status and call the service
        maintenanceToggleButton.addActionListener(e -> {
            boolean isEnabled = maintenanceToggleButton.isSelected();
            statusLabel.setText("Current Status: " + (isEnabled ? "ON" : "OFF"));
            
            // TODO: Call AdminService.setMaintenanceMode(isEnabled)
            
            if(isEnabled) {
                JOptionPane.showMessageDialog(this, 
                    "Maintenance Mode is ON.\nStudents and instructors cannot make changes.",
                    "Maintenance Mode Enabled", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Maintenance Mode is OFF.\nNormal operations restored.",
                    "Maintenance Mode Disabled", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        add(maintenanceToggleButton);
        add(statusLabel);
    }
}