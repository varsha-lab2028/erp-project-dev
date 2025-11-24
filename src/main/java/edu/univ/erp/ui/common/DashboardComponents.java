package edu.univ.erp.ui.common;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import edu.univ.erp.ui.common.DashboardTheme;

public class DashboardComponents {
    // Note: The constant BG_DARK is defined here as a fallback 
    // but the SidebarPanel and TablePanel use it via DashboardComponents.BG_DARK.
    // This is fine, but typically all theme constants belong in DashboardTheme.
    public static final Color BG_DARK = new Color(34, 34, 34);
    
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(DashboardTheme.FONT_REGULAR);
        button.setBackground(DashboardTheme.PRIMARY_DARK);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Helper panel that draws a rounded background and a soft shadow.
     * Used for the floating cards and the Login box.
     */
    public static class RoundedPanel extends JPanel {
        private final int radius;
        private Color bgColor;
        private boolean shadow;

        public RoundedPanel(int radius, Color bgColor, boolean shadow) {
            this.radius = radius;
            this.bgColor = bgColor;
            this.shadow = shadow;
            setOpaque(false); // Important for custom painting
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (shadow) {
                g2.setColor(new Color(0, 0, 0, 15)); // Soft shadow
                // Draw shadow slightly offset (2px right, 4px down)
                g2.fillRoundRect(2, 4, getWidth(), getHeight(), radius, radius);
            }

            g2.setColor(bgColor);
            // Draw main box
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            super.paintComponent(g);
        }
    }

    /**
     * Sidebar panel with navigation items.
     */
    public static class SidebarPanel extends JPanel {
        private String role;
        private ActionListener navigationListener;
        private final List<JButton> itemButtons;

        public SidebarPanel(String role, ActionListener navigationListener) {
            this.role = role;
            this.navigationListener = navigationListener;
            // The itemButtons was declared but not initialized in the original constructor
            this.itemButtons = new ArrayList<>(); 
            
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(DashboardComponents.BG_DARK);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setPreferredSize(new Dimension(200, 0));
        }

        public void addItem(String name, String emoji) {
            JButton button = new JButton(emoji + " " + name);
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
            button.setBackground(DashboardTheme.BG_MEDIUM);
            button.setForeground(DashboardTheme.TEXT_LIGHT);
            button.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            button.setFocusPainted(false);
            button.setOpaque(true);

            button.addActionListener(e -> {
                if (navigationListener != null) {
                    navigationListener.actionPerformed(new java.awt.event.ActionEvent(this, 0, name));
                }
            });

            itemButtons.add(button);
            add(button);
            add(Box.createVerticalStrut(5));
        }
    }

    /**
     * Top bar panel with title and user info.
     */
    public static class TopBarPanel extends JPanel {
        private String title;
        private String userInitials;

        public TopBarPanel(String title, String userInitials) {
            this.title = title;
            this.userInitials = userInitials;

            setLayout(new BorderLayout());
            setBackground(DashboardTheme.PRIMARY_COLOR);
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            setPreferredSize(new Dimension(0, 60));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setForeground(DashboardTheme.TEXT_LIGHT);
            add(titleLabel, BorderLayout.WEST);

            JPanel userPanel = new JPanel();
            userPanel.setBackground(DashboardTheme.PRIMARY_COLOR);
            JLabel userLabel = new JLabel(userInitials);
            userLabel.setFont(new Font("Arial", Font.BOLD, 14));
            userLabel.setForeground(DashboardTheme.TEXT_LIGHT);
            userLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            userPanel.add(userLabel);
            add(userPanel, BorderLayout.EAST);
        }
    }

    public static class StatsCard extends RoundedPanel {
        public StatsCard(String label, String value, String emoji, Color bgColor) {
            super(12, bgColor, true);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            setPreferredSize(new Dimension(200, 120));

            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.setBackground(bgColor);
            content.setOpaque(false);

            JLabel emojiLabel = new JLabel(emoji);
            emojiLabel.setFont(new Font("Arial", Font.PLAIN, 32));
            emojiLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(emojiLabel);

            content.add(Box.createVerticalStrut(8));

            JLabel labelText = new JLabel(label);
            labelText.setFont(new Font("Arial", Font.PLAIN, 12));
            labelText.setForeground(DashboardTheme.TEXT_DARK);
            labelText.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(labelText);

            content.add(Box.createVerticalStrut(4));

            JLabel valueText = new JLabel(value);
            valueText.setFont(new Font("Arial", Font.BOLD, 24));
            valueText.setForeground(DashboardTheme.TEXT_DARK);
            valueText.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(valueText);

            add(content, BorderLayout.CENTER);
        }
    }

    public static class TablePanel extends RoundedPanel {
        public TablePanel(String title, String[] columnNames, Object[][] data) {
            super(12, DashboardComponents.BG_DARK, true);
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
            titleLabel.setForeground(DashboardTheme.TEXT_LIGHT);
            add(titleLabel, BorderLayout.NORTH);

            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            JTable table = new JTable(model);
            table.setBackground(DashboardComponents.BG_DARK);
            table.setForeground(DashboardTheme.TEXT_LIGHT);
            table.setGridColor(DashboardTheme.BG_MEDIUM);
            table.setFont(new Font("Arial", Font.PLAIN, 12));
            table.setRowHeight(25);
            table.setEnabled(false);

            JTableHeader header = table.getTableHeader();
            header.setBackground(DashboardTheme.BG_MEDIUM);
            header.setForeground(DashboardTheme.TEXT_LIGHT);
            header.setFont(new Font("Arial", Font.BOLD, 12)); // Header font was missing in original TablePanel

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBackground(DashboardComponents.BG_DARK);
            scrollPane.getViewport().setBackground(DashboardComponents.BG_DARK);
            add(scrollPane, BorderLayout.CENTER);
        }
    }
}