package edu.univ.erp.ui.dashboards.instructor;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InstructorHomePanel extends JPanel {
    public InstructorHomePanel() {
        setLayout(new BorderLayout());
        setBackground(DashboardTheme.BG_LIGHT);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(DashboardTheme.BG_LIGHT);

        // --- 1. Stats Cards ---
        JPanel grid = new JPanel(new GridLayout(1, 3, 20, 0));
        grid.setBackground(DashboardTheme.BG_LIGHT);
        grid.setMaximumSize(new Dimension(2000, 140));
        
        grid.add(new DashboardComponents.StatsCard("My Sections", "4", "📅", DashboardTheme.SECONDARY_GREEN));
        grid.add(new DashboardComponents.StatsCard("Total Students", "185", "👥", DashboardTheme.ACCENT_YELLOW));
        grid.add(new DashboardComponents.StatsCard("Pending Grades", "2", "📝", DashboardTheme.ACCENT_ORANGE));
        body.add(grid);
        
        body.add(Box.createVerticalStrut(30));

        // --- 2. Split View (Table + Chart) ---
        JPanel split = new JPanel(new GridLayout(1, 2, 20, 0));
        split.setBackground(DashboardTheme.BG_LIGHT);

        // Left: Active Sections Table
        String[] cols = {"Course", "Section", "Room", "Time"};
        Object[][] data = {
            {"CSE101", "A", "C-01", "Mon 10:00"},
            {"CSE101", "B", "C-02", "Tue 11:30"},
            {"CSE202", "A", "S-10", "Wed 09:00"},
            {"DES301", "C", "Lab-1", "Fri 14:00"}
        };
        split.add(new DashboardComponents.TablePanel("My Active Sections", cols, data));

        // Right: Grade Analytics Chart
        JPanel chartCard = new DashboardComponents.RoundedPanel(15, Color.WHITE, true);
        chartCard.setLayout(new BorderLayout());
        chartCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel chartTitle = new JLabel("Grade Distribution (Recent)");
        chartTitle.setFont(DashboardTheme.FONT_LABEL);
        chartCard.add(chartTitle, BorderLayout.NORTH);
        
        // Custom Paint Bar Chart
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
                int barWidth = (w / 5) - 15;
                
                // Draw Bars
                for(int i=0; i<5; i++) {
                    int barHeight = (int)((values[i]/50.0) * (h-40));
                    int x = i * (barWidth + 15);
                    int y = h - 30 - barHeight;
                    
                    g2.setColor(i == 2 ? DashboardTheme.SECONDARY_GREEN : DashboardTheme.PRIMARY_DARK); // Highlight 'C' or average
                    g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);
                    
                    // Draw Label
                    g2.setColor(DashboardTheme.TEXT_SECONDARY);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    g2.drawString(labels[i], x + barWidth/2 - 5, h - 10);
                }
            }
        };
        graph.setOpaque(false);
        chartCard.add(graph, BorderLayout.CENTER);
        
        split.add(chartCard);
        body.add(split);

        add(new JScrollPane(body), BorderLayout.CENTER);
    }
}