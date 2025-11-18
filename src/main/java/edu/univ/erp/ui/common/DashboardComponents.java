package edu.univ.erp.ui.common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Consumer;

public class DashboardComponents {

    // --- 1. SIDEBAR ---
    public static class SidebarPanel extends JPanel {
        private final JPanel menuContainer;
        private final Consumer<String> navListener;
        private JPanel currentActiveItem = null;

        public SidebarPanel(String role, Consumer<String> navListener) {
            this.navListener = navListener;
            setLayout(new BorderLayout());
            setBackground(DashboardTheme.PRIMARY_DARK);
            setPreferredSize(new Dimension(220, 0));
            setBorder(new EmptyBorder(20, 0, 20, 0));

            // Branding
            JLabel brand = new JLabel("IIITD ERP", SwingConstants.CENTER);
            brand.setFont(new Font("Segoe UI", Font.BOLD, 24));
            brand.setForeground(Color.WHITE);
            brand.setBorder(new EmptyBorder(0, 0, 40, 0));
            add(brand, BorderLayout.NORTH);

            // Menu Items container
            menuContainer = new JPanel();
            menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));
            menuContainer.setBackground(DashboardTheme.PRIMARY_DARK);
            add(menuContainer, BorderLayout.CENTER);
            
            // Settings at bottom
            JPanel bottom = new JPanel(new BorderLayout());
            bottom.setBackground(DashboardTheme.PRIMARY_DARK);
            bottom.add(createItem("Settings", "⚙️"), BorderLayout.SOUTH);
            add(bottom, BorderLayout.SOUTH);
        }

        public void addItem(String text, String icon) {
            menuContainer.add(createItem(text, icon));
            menuContainer.add(Box.createVerticalStrut(5));
        }

        private JPanel createItem(String text, String icon) {
            JPanel item = new JPanel(new BorderLayout());
            item.setMaximumSize(new Dimension(220, 45));
            item.setBackground(DashboardTheme.PRIMARY_DARK);
            item.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Left Indicator (Hidden by default)
            JPanel indicator = new JPanel();
            indicator.setPreferredSize(new Dimension(4, 0));
            indicator.setBackground(DashboardTheme.PRIMARY_DARK); // Match bg initially
            item.add(indicator, BorderLayout.WEST);

            JLabel lbl = new JLabel(icon + "   " + text);
            lbl.setFont(DashboardTheme.FONT_REGULAR);
            lbl.setForeground(Color.WHITE);
            lbl.setBorder(new EmptyBorder(0, 20, 0, 0));
            item.add(lbl, BorderLayout.CENTER);
            
            item.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (currentActiveItem != item) item.setBackground(new Color(255,255,255,20));
                }
                public void mouseExited(MouseEvent e) {
                    if (currentActiveItem != item) item.setBackground(DashboardTheme.PRIMARY_DARK);
                }
                public void mouseClicked(MouseEvent e) {
                    // Reset old active
                    if (currentActiveItem != null) {
                        currentActiveItem.setBackground(DashboardTheme.PRIMARY_DARK);
                        ((JPanel)currentActiveItem.getComponent(0)).setBackground(DashboardTheme.PRIMARY_DARK);
                    }
                    // Set new active
                    currentActiveItem = item;
                    item.setBackground(new Color(255,255,255,30));
                    indicator.setBackground(DashboardTheme.SECONDARY_GREEN); // Highlight
                    
                    if(navListener!=null) navListener.accept(text);
                }
            });
            return item;
        }
    }

    // --- 2. TOP BAR ---
    public static class TopBarPanel extends JPanel {
        public TopBarPanel(String titleText, String initials) {
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(0, 60));
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, DashboardTheme.BORDER_GRAY));

            JLabel title = new JLabel(titleText);
            title.setFont(DashboardTheme.FONT_HEADER);
            title.setForeground(DashboardTheme.TEXT_PRIMARY);
            title.setBorder(new EmptyBorder(0, 24, 0, 0));
            add(title, BorderLayout.WEST);

            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
            right.setOpaque(false);
            JLabel bell = new JLabel("🔔");
            bell.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            right.add(bell);
            add(right, BorderLayout.EAST);
        }
    }

    // --- 3. STATS CARD ---
    public static class StatsCard extends RoundedPanel {
        private final Color accent;
        public StatsCard(String title, String value, String icon, Color accent) {
            super(15, Color.WHITE, true);
            this.accent = accent;
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(200, 130));
            
            JPanel content = new JPanel(new BorderLayout());
            content.setOpaque(false);
            content.setBorder(new EmptyBorder(15, 20, 15, 20));

            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false);
            JLabel tLbl = new JLabel(title.toUpperCase());
            tLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            tLbl.setForeground(DashboardTheme.TEXT_SECONDARY);
            JLabel iLbl = new JLabel(icon);
            iLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
            top.add(tLbl, BorderLayout.WEST);
            top.add(iLbl, BorderLayout.EAST);

            JLabel vLbl = new JLabel(value);
            vLbl.setFont(DashboardTheme.FONT_CARD_NUM);
            vLbl.setForeground(DashboardTheme.TEXT_PRIMARY);

            content.add(top, BorderLayout.NORTH);
            content.add(vLbl, BorderLayout.CENTER);
            add(content, BorderLayout.CENTER);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(accent);
            g2.fillRect(5, 0, getWidth()-10, 4); // Top Accent
        }
    }

    // --- 4. STYLED BUTTON ---
    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(DashboardTheme.SECONDARY_GREEN);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- 5. ROUNDED PANEL HELPER ---
    public static class RoundedPanel extends JPanel {
        private int radius; private Color bg; private boolean shadow;
        public RoundedPanel(int radius, Color bg, boolean shadow) {
            this.radius = radius; this.bg = bg; this.shadow = shadow;
            setOpaque(false);
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if(shadow) {
                g2.setColor(new Color(0,0,0,10));
                g2.fillRoundRect(3,3,getWidth()-6,getHeight()-6,radius,radius);
            }
            g2.setColor(bg);
            g2.fillRoundRect(0,0,getWidth()-(shadow?6:0),getHeight()-(shadow?6:0),radius,radius);
            super.paintComponent(g);
        }
    }

    // --- 6. TABLE PANEL ---
    public static class TablePanel extends RoundedPanel {
        public TablePanel(String title, String[] columnNames, Object[][] data) {
            super(15, Color.WHITE, true);
            setLayout(new BorderLayout());
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
            setBorder(new EmptyBorder(20, 20, 20, 20));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLabel.setForeground(DashboardTheme.TEXT_PRIMARY);
            titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
            add(titleLabel, BorderLayout.NORTH);

            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable table = new JTable(model);
            table.setFont(DashboardTheme.FONT_REGULAR);
            table.setRowHeight(30);
            table.setShowGrid(false);
            table.setIntercellSpacing(new Dimension(0, 5));
            table.getTableHeader().setBackground(Color.WHITE);
            table.getTableHeader().setForeground(DashboardTheme.TEXT_SECONDARY);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, DashboardTheme.BORDER_GRAY));
            table.setSelectionBackground(new Color(200, 230, 201, 50));

            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setForeground(DashboardTheme.TEXT_PRIMARY);
            table.setDefaultRenderer(Object.class, renderer);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(scrollPane, BorderLayout.CENTER);
        }
    }
}