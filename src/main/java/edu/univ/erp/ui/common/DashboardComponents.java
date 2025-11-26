package edu.univ.erp.ui.common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;

public class DashboardComponents {

    // --- 1. Top Bar with Theme Toggle ---
    public static class TopBarPanel extends JPanel {
        public TopBarPanel(String title, String userInitials, ActionListener onToggleSidebar, ActionListener onProfileClick, ActionListener onThemeSwitch) {
            setLayout(new BorderLayout());
            setBackground(DashboardTheme.SURFACE); // Dynamic Background
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, DashboardTheme.BORDER_COLOR),
                new EmptyBorder(10, 20, 10, 20)
            ));

            // LEFT
            JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
            left.setOpaque(false);
            
            JButton toggleBtn = new JButton("☰");
            toggleBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            toggleBtn.setBorderPainted(false);
            toggleBtn.setContentAreaFilled(false);
            toggleBtn.setFocusPainted(false);
            toggleBtn.setForeground(DashboardTheme.TEXT_PRIMARY);
            toggleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            toggleBtn.addActionListener(onToggleSidebar);
            
            JLabel lblTitle = new JLabel(title);
            lblTitle.setFont(DashboardTheme.FONT_TITLE);
            lblTitle.setForeground(DashboardTheme.TEXT_PRIMARY);

            left.add(toggleBtn);
            left.add(lblTitle);

            // RIGHT: Theme Toggle + Profile
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
            right.setOpaque(false);
            
            // Theme Button
            JButton themeBtn = new JButton(DashboardTheme.isDark ? "☀️" : "🌙");
            themeBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
            themeBtn.setBorderPainted(false);
            themeBtn.setContentAreaFilled(false);
            themeBtn.setFocusPainted(false);
            themeBtn.setToolTipText("Toggle Dark/Light Mode");
            themeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            themeBtn.addActionListener(e -> {
                onThemeSwitch.actionPerformed(e);
                themeBtn.setText(DashboardTheme.isDark ? "☀️" : "🌙");
            });
            
            right.add(themeBtn);

            // Profile Avatar
            JPanel profileBadge = new CircleAvatar(userInitials, 40);
            profileBadge.setCursor(new Cursor(Cursor.HAND_CURSOR));
            profileBadge.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onProfileClick.actionPerformed(null);
                }
            });
            
            right.add(profileBadge);

            add(left, BorderLayout.WEST);
            add(right, BorderLayout.EAST);
        }
    }

    // --- 2. Sidebar Button ---
    public static class SidebarButton extends JButton {
        private String text;
        private String icon;
        private boolean collapsed = false;

        public SidebarButton(String text, String iconSymbol) {
            this.text = text;
            this.icon = iconSymbol;
            updateText();
            setFont(new Font("Segoe UI", Font.BOLD, 16)); 
            setForeground(new Color(176, 190, 197));
            setBackground(DashboardTheme.BG_SIDEBAR); // Keep Sidebar dark usually, or use BG_SIDEBAR
            setBorder(new EmptyBorder(15, 20, 15, 20));
            setFocusPainted(false);
            setHorizontalAlignment(SwingConstants.LEFT);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setForeground(Color.WHITE);
                    setBackground(DashboardTheme.PRIMARY.darker());
                    setOpaque(true);
                    repaint();
                }
                public void mouseExited(MouseEvent e) {
                    setForeground(new Color(176, 190, 197));
                    setBackground(DashboardTheme.BG_SIDEBAR);
                    setOpaque(false);
                    repaint();
                }
            });
        }
        
        public void setCollapsed(boolean collapsed) {
            this.collapsed = collapsed;
            updateText();
            setHorizontalAlignment(collapsed ? SwingConstants.CENTER : SwingConstants.LEFT);
        }
        
        private void updateText() {
            setText(collapsed ? icon : icon + "   " + text);
        }
    }

    // --- 3. Card Panel (Updated to use SURFACE color) ---
    public static class CardPanel extends JPanel {
        private int cornerRadius = 15;
        public CardPanel() {
            super(); setOpaque(false); setBorder(new EmptyBorder(10, 10, 15, 10)); setLayout(new BorderLayout());
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth() - 10; int height = getHeight() - 10;
            
            // Shadow (Darker in Dark Mode)
            g2.setColor(DashboardTheme.isDark ? new Color(0,0,0, 100) : new Color(200, 200, 200, 80));
            g2.fillRoundRect(8, 5, width, height, cornerRadius, cornerRadius);
            
            // Background
            g2.setColor(DashboardTheme.SURFACE);
            g2.fillRoundRect(5, 2, width, height, cornerRadius, cornerRadius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    // --- 4. Primary Button ---
    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setFont(DashboardTheme.FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setBackground(DashboardTheme.PRIMARY);
        btn.setBorder(new EmptyBorder(12, 15, 12, 15));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(DashboardTheme.PRIMARY_DARK); }
            public void mouseExited(MouseEvent e) { btn.setBackground(DashboardTheme.PRIMARY); }
        });
        return btn;
    }

    // --- 5. Circle Avatar ---
    public static class CircleAvatar extends JPanel {
        private String initials;
        public CircleAvatar(String initials, int size) {
            this.initials = initials; setOpaque(false); setPreferredSize(new Dimension(size, size));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(DashboardTheme.PRIMARY);
            g2.fillOval(0, 0, getWidth(), getHeight());
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, getWidth() / 2));
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(initials)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(initials, x, y);
        }
    }
    
    // --- 6. Table Panel (Updated Colors) ---
    public static class TablePanel extends CardPanel {
        public TablePanel(String title, String[] columns, Object[][] data) {
            setLayout(new BorderLayout());

            //header title
            JLabel titleLbl = new JLabel(title);
            titleLbl.setFont(DashboardTheme.FONT_SUBTITLE);
            titleLbl.setForeground(DashboardTheme.TEXT_PRIMARY); // Use Theme Color
            titleLbl.setBorder(new EmptyBorder(15, 20, 15, 20));
            add(titleLbl, BorderLayout.NORTH);
            
            DefaultTableModel model = new DefaultTableModel(data, columns) {
                public boolean isCellEditable(int row, int column) { return false; }
            };

            JTable table = new JTable(model);
            table.setRowHeight(35);
            table.setShowVerticalLines(false);
            table.setGridColor(DashboardTheme.BORDER_COLOR);
            table.setFont(DashboardTheme.FONT_REGULAR);
            //to add a horizontal scrolling to the table
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            
            // Dynamic Table Colors
            table.setBackground(DashboardTheme.SURFACE);
            table.setForeground(DashboardTheme.TEXT_PRIMARY);
            table.setSelectionBackground(DashboardTheme.PRIMARY.brighter());
            table.setSelectionForeground(Color.WHITE);

            JTableHeader header = table.getTableHeader();
            header.setBackground(DashboardTheme.SURFACE); // Or slightly offset color
            header.setForeground(DashboardTheme.TEXT_PRIMARY);
            header.setFont(DashboardTheme.FONT_BOLD);
            header.setPreferredSize(new Dimension(0, 40));
            
            JScrollPane sp = new JScrollPane(table);
            sp.setBorder(BorderFactory.createEmptyBorder());
            sp.getViewport().setBackground(DashboardTheme.SURFACE);
            add(sp, BorderLayout.CENTER);
        }
    }
    // ... existing imports
// ADD THIS METHOD TO DashboardComponents class

    // --- 8. Input Styling Helper ---
    public static void styleControl(JComponent component) {
        component.setFont(DashboardTheme.FONT_REGULAR);
        component.setForeground(DashboardTheme.TEXT_PRIMARY);
        
        // Input Background (slightly different from Surface for contrast)
        if (DashboardTheme.isDark) {
            component.setBackground(new Color(60, 60, 60)); 
            component.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        } else {
            component.setBackground(Color.WHITE);
            component.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        }
        
        // Fix for JComboBox in Dark Mode
        if (component instanceof JComboBox) {
            ((JComboBox<?>) component).getEditor().getEditorComponent().setBackground(
                DashboardTheme.isDark ? new Color(60, 60, 60) : Color.WHITE
            );
            ((JComboBox<?>) component).getEditor().getEditorComponent().setForeground(DashboardTheme.TEXT_PRIMARY);
        }
    }

    // --- 7. Stats Card ---
    public static class StatsCard extends CardPanel {
        public StatsCard(String title, String value, Color accent) {
            super(); setLayout(new BorderLayout());
            JPanel content = new JPanel(new GridLayout(2, 1)); content.setOpaque(false);
            content.setBorder(new EmptyBorder(15, 20, 15, 20));
            
            JLabel lblTitle = new JLabel(title.toUpperCase()); 
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12)); 
            lblTitle.setForeground(DashboardTheme.TEXT_SECONDARY);
            
            JLabel lblValue = new JLabel(value); 
            lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28)); 
            lblValue.setForeground(DashboardTheme.TEXT_PRIMARY);
            
            content.add(lblTitle); content.add(lblValue);
            
            JPanel accentBar = new JPanel(); 
            accentBar.setPreferredSize(new Dimension(5, 0)); 
            accentBar.setBackground(accent);
            add(accentBar, BorderLayout.WEST); add(content, BorderLayout.CENTER);
        }
    }
}