package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.service.MaintenanceService;
import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class MaintenancePanel extends JPanel {

    public MaintenancePanel(boolean isAdmin, MaintenanceService maintenanceService, Consumer<Boolean> onToggle) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("System Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(title, gbc);

        if (isAdmin) {
            JLabel desc = new JLabel("Enable maintenance mode to restrict non-admin users to read-only access.");
            add(desc, gbc);

            JToggleButton maintenanceToggle = new JToggleButton("Maintenance Mode");
            if (maintenanceService != null) {
                maintenanceToggle.setSelected(maintenanceService.isMaintenanceMode());
                maintenanceToggle.addActionListener(e -> {
                    boolean isSelected = maintenanceToggle.isSelected();
                    maintenanceToggle.setText(isSelected ? "Maintenance Mode: ON" : "Maintenance Mode: OFF");
                    if (onToggle != null) {
                        onToggle.accept(isSelected);
                    }
                });
            }
            add(maintenanceToggle, gbc);
        } else {
            add(new JLabel("System settings are managed by the administrator."), gbc);
        }
    }
}