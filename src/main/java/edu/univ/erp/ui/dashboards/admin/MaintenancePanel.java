package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MaintenancePanel extends JPanel {

    // Constructor accepts 'isAdmin' to toggle the maintenance card
    public MaintenancePanel(boolean isAdmin) {
        setLayout(new BorderLayout());
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(DashboardTheme.BG_MAIN);

        // --- 1. System Maintenance Card (ADMIN ONLY) ---
        if (isAdmin) {
            DashboardComponents.CardPanel sysCard = new DashboardComponents.CardPanel();
            sysCard.setLayout(new BorderLayout());
            
            JLabel title = new JLabel("System Maintenance");
            title.setFont(DashboardTheme.FONT_SUBTITLE);
            title.setForeground(DashboardTheme.TEXT_PRIMARY);
            title.setBorder(new EmptyBorder(15, 20, 0, 0));
            sysCard.add(title, BorderLayout.NORTH);

            JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
            togglePanel.setOpaque(false);
            
            JLabel lblMode = new JLabel("Maintenance Mode:");
            lblMode.setFont(DashboardTheme.FONT_REGULAR);
            lblMode.setForeground(DashboardTheme.TEXT_PRIMARY);
            
            JToggleButton toggle = new JToggleButton("Disabled");
            toggle.setFont(DashboardTheme.FONT_BOLD);
            toggle.addActionListener(e -> {
                if(toggle.isSelected()) {
                    toggle.setText("Enabled");
                    toggle.setForeground(DashboardTheme.DANGER);
                } else {
                    toggle.setText("Disabled");
                    toggle.setForeground(DashboardTheme.TEXT_PRIMARY);
                }
            });

            togglePanel.add(lblMode);
            togglePanel.add(toggle);
            sysCard.add(togglePanel, BorderLayout.CENTER);

            container.add(sysCard);
            container.add(Box.createVerticalStrut(20));
        }

        // --- 2. Preferences Card (SHARED) ---
        DashboardComponents.CardPanel prefCard = new DashboardComponents.CardPanel();
        prefCard.setLayout(new BorderLayout());
        
        JLabel prefTitle = new JLabel("Application Preferences");
        prefTitle.setFont(DashboardTheme.FONT_SUBTITLE);
        prefTitle.setForeground(DashboardTheme.TEXT_PRIMARY);
        prefTitle.setBorder(new EmptyBorder(15, 20, 15, 0));
        prefCard.add(prefTitle, BorderLayout.NORTH);
        
        JPanel opts = new JPanel(new GridLayout(3, 2, 10, 10));
        opts.setOpaque(false);
        opts.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        opts.add(createOption("Enable Email Notifications", true));
        opts.add(createOption("Enable SMS Alerts", false));
        opts.add(createOption("Dark Mode (Beta)", DashboardTheme.isDark));
        
        if (isAdmin) {
            opts.add(createOption("Auto-Backup Database", true));
        } else {
            opts.add(new JLabel("")); // Spacer
        }
        
        prefCard.add(opts, BorderLayout.CENTER);
        container.add(prefCard);

        add(container, BorderLayout.NORTH);
    }

    private JCheckBox createOption(String text, boolean selected) {
        JCheckBox box = new JCheckBox(text, selected);
        box.setFont(DashboardTheme.FONT_REGULAR);
        box.setForeground(DashboardTheme.TEXT_PRIMARY);
        box.setOpaque(false);
        return box;
    }
}