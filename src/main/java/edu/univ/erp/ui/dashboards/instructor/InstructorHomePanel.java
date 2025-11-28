package edu.univ.erp.ui.dashboards.instructor;

import edu.univ.backend.controller.InstructorController;
import edu.univ.erp.domain.Section;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class InstructorHomePanel extends JPanel {

    private final InstructorController controller;
    private final long instructorId;

    
    public InstructorHomePanel(InstructorController controller, long instructorId) {
        this.controller = controller;
        this.instructorId = instructorId;

        setLayout(new BorderLayout());
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        initUI();
    }

    private void initUI() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(DashboardTheme.BG_MAIN);

       
        String secCount = "-";
        String stuCount = "-";
        String penCount = "-";
        Object[][] tableData = new Object[0][4];

        try {
       
            Map<String, Integer> stats = controller.getDashboardStats(instructorId);
            secCount = String.valueOf(stats.getOrDefault("sections", 0));
            stuCount = String.valueOf(stats.getOrDefault("students", 0));
            penCount = String.valueOf(stats.getOrDefault("pending", 0));

            List<Section> sections = controller.getSectionsByInstructor(instructorId);
            
     
            int limit = Math.min(sections.size(), 4);
            tableData = new Object[limit][4];

            for (int i = 0; i < limit; i++) {
                Section s = sections.get(i);
                tableData[i][0] = s.getCourseCode();
                tableData[i][1] = s.getSectionId(); 
                tableData[i][2] = s.getClassroom();
                tableData[i][3] = s.getDay() + " " + s.getTimings();
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }


        JPanel grid = new JPanel(new GridLayout(1, 3, 20, 0));
        grid.setBackground(DashboardTheme.BG_MAIN);
        grid.setMaximumSize(new Dimension(2000, 120));

        grid.add(new DashboardComponents.StatsCard("My Sections", secCount, "📅", DashboardTheme.INFO));
        grid.add(new DashboardComponents.StatsCard("Total Students", stuCount, "👥", DashboardTheme.SUCCESS));
        grid.add(new DashboardComponents.StatsCard("Pending Grades", penCount, "📝", DashboardTheme.WARNING));
        body.add(grid);

        body.add(Box.createVerticalStrut(30));


        JPanel split = new JPanel(new GridLayout(1, 2, 25, 0));
        split.setBackground(DashboardTheme.BG_MAIN);


        String[] cols = {"Course", "Section", "Room", "Time"};
        split.add(new DashboardComponents.TablePanel("My Active Sections", cols, tableData));

        JPanel chartCard = createChartCard();
        split.add(chartCard);

        body.add(split);

        add(new JScrollPane(body) {
            {
                setBorder(null);
                getViewport().setBackground(DashboardTheme.BG_MAIN);
            }
        }, BorderLayout.CENTER);
    }

   
    private JPanel createChartCard() {
        JPanel chartCard = new JPanel(new BorderLayout());
        chartCard.setBackground(Color.WHITE);
        chartCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel chartTitle = new JLabel("Grade Distribution");
        chartTitle.setFont(DashboardTheme.FONT_SUBTITLE);
        chartCard.add(chartTitle, BorderLayout.NORTH);

  
        JPanel graph = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
  
                int[] values = {10, 25, 45, 20, 15};
                String[] labels = {"F", "D", "C", "B", "A"};

                int padding = 20;
                int barWidth = (w - (padding * 2)) / 5 - 20;
                int maxVal = 50;

                g2.setColor(new Color(226, 232, 240));
                g2.drawLine(padding, h - 30, w - padding, h - 30);

                for (int i = 0; i < 5; i++) {
                    int barHeight = (int) ((values[i] / (double) maxVal) * (h - 60));
                    int x = padding + i * (barWidth + 20) + 10;
                    int y = h - 30 - barHeight;

                    GradientPaint gp = new GradientPaint(x, y, DashboardTheme.PRIMARY, x, y + barHeight, DashboardTheme.PRIMARY_DARK);
                    if (i == 2)
                        gp = new GradientPaint(x, y, DashboardTheme.SUCCESS, x, y + barHeight, new Color(5, 150, 105));

                    g2.setPaint(gp);
                    g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);

                    g2.setColor(DashboardTheme.TEXT_SECONDARY);
                    g2.setFont(DashboardTheme.FONT_BOLD);
                    FontMetrics fm = g2.getFontMetrics();
                    int labelWidth = fm.stringWidth(labels[i]);
                    g2.drawString(labels[i], x + (barWidth - labelWidth) / 2, h - 10);
                }
            }
        };
        graph.setOpaque(false);
        chartCard.add(graph, BorderLayout.CENTER);
        return chartCard;
    }
}