package edu.univ.erp.ui.dashboards.instructor;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InstructorHomePanel extends JPanel {
    public InstructorHomePanel() {
        setLayout(new BorderLayout());
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(DashboardTheme.BG_MAIN);

        // --- 1. Stats Cards ---
        JPanel grid = new JPanel(new GridLayout(1, 3, 20, 0));
        grid.setBackground(DashboardTheme.BG_MAIN);
        grid.setMaximumSize(new Dimension(2000, 110));
        
        grid.add(new DashboardComponents.StatsCard("My Sections", "4", DashboardTheme.PRIMARY)); // Blue
        grid.add(new DashboardComponents.StatsCard("Total Students", "185", DashboardTheme.SUCCESS)); // Green
        grid.add(new DashboardComponents.StatsCard("Pending Grades", "2", DashboardTheme.WARNING)); // Orange
        body.add(grid);
        
        body.add(Box.createVerticalStrut(30));

        // --- 2. Split View (Table + Chart) ---
        JPanel split = new JPanel(new GridLayout(1, 2, 25, 0));
        split.setBackground(DashboardTheme.BG_MAIN);

        // Left: Active Sections Table
        String[] cols = {"Course", "Section", "Room", "Time"};
        Object[][] data = {
            {"CSE101", "A", "C-01", "Mon 10:00"},
            {"CSE101", "B", "C-02", "Tue 11:30"},
            {"CSE202", "A", "S-10", "Wed 09:00"},
            {"DES301", "C", "Lab-1", "Fri 14:00"}
        };
        split.add(new DashboardComponents.TablePanel("My Active Sections", cols, data));

        // Right: Grade Analytics Chart (Using CardPanel for rounded floating look)
        DashboardComponents.CardPanel chartCard = new DashboardComponents.CardPanel();
        chartCard.setLayout(new BorderLayout());
        
        JLabel chartTitle = new JLabel("Grade Distribution");
        chartTitle.setFont(DashboardTheme.FONT_SUBTITLE);
        chartTitle.setForeground(DashboardTheme.TEXT_PRIMARY); // Dynamic Text
        chartTitle.setBorder(new EmptyBorder(15, 20, 10, 0));
        chartCard.add(chartTitle, BorderLayout.NORTH);
        
        // Custom Graph
        JPanel graph = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                int[] values = {10, 25, 45, 20, 15}; // F, D, C, B, A
                String[] labels = {"F", "D", "C", "B", "A"};
                
                int padding = 20;
                int barWidth = (w - (padding * 2)) / 5 - 20;
                int maxVal = 50;

                // Draw Base Line (Dynamic Color)
                g2.setColor(DashboardTheme.BORDER_COLOR);
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(padding, h - 30, w - padding, h - 30);
                
                // Draw Bars
                for(int i=0; i<5; i++) {
                    int barHeight = (int)((values[i] / (double)maxVal) * (h - 60));
                    int x = padding + i * (barWidth + 20) + 10;
                    int y = h - 30 - barHeight;
                    
                    // Gradient Fill
                    GradientPaint gp = new GradientPaint(x, y, DashboardTheme.PRIMARY, x, y + barHeight, DashboardTheme.PRIMARY_DARK);
                    if(i==2) gp = new GradientPaint(x, y, DashboardTheme.SUCCESS, x, y + barHeight, DashboardTheme.SUCCESS.darker()); 
                    
                    g2.setPaint(gp);
                    g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);
                    
                    // Draw Label (Dynamic Color)
                    g2.setColor(DashboardTheme.TEXT_SECONDARY);
                    g2.setFont(DashboardTheme.FONT_BOLD);
                    FontMetrics fm = g2.getFontMetrics();
                    int labelWidth = fm.stringWidth(labels[i]);
                    g2.drawString(labels[i], x + (barWidth - labelWidth)/2, h - 10);
                }
            }
        };
        graph.setOpaque(false);
        chartCard.add(graph, BorderLayout.CENTER);
        
        split.add(chartCard);
        body.add(split);

        add(new JScrollPane(body) {
             { setBorder(null); getViewport().setBackground(DashboardTheme.BG_MAIN); }
        }, BorderLayout.CENTER);
    }
}