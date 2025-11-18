package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MaintenancePanel extends JPanel {
    public MaintenancePanel() {
        setLayout(new GridBagLayout());
        setBackground(DashboardTheme.BG_LIGHT);
        
        JPanel card = new DashboardComponents.RoundedPanel(20, Color.WHITE, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 60, 40, 60));
        
        JLabel icon = new JLabel("⚠️");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel title = new JLabel("System Maintenance Mode");
        title.setFont(DashboardTheme.FONT_HEADER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JToggleButton toggle = new JToggleButton("Enable Maintenance Mode");
        toggle.setFont(DashboardTheme.FONT_LABEL);
        toggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        toggle.addActionListener(e -> {
            boolean on = toggle.isSelected();
            toggle.setText(on ? "DISABLE MAINTENANCE" : "ENABLE MAINTENANCE MODE");
            toggle.setForeground(on ? Color.RED : Color.BLACK);
        });

        card.add(icon);
        card.add(Box.createVerticalStrut(20));
        card.add(title);
        card.add(Box.createVerticalStrut(30));
        card.add(toggle);
        
        add(card);
    }
}