package edu.univ.erp.ui.common;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class DashboardComponents {
    
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
                g2.fillRoundRect(2, 4, getWidth() - 6, getHeight() - 6, radius, radius);
            }

            g2.setColor(bgColor);
            // Draw main box
            g2.fillRoundRect(0, 0, getWidth() - (shadow ? 6 : 0), getHeight() - (shadow ? 6 : 0), radius, radius);
            
            super.paintComponent(g);
        }
    }
}