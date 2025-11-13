package edu.univ.erp.util;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

    public RoundedButton(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setForeground(Theme.TEXT_DARK);
        setBackground(Theme.SEA_GREEN);
        setFont(Theme.FONT_TEXT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getModel().isPressed() ? Theme.DEEP_SEA.darker() :
                    getModel().isRollover() ? Theme.SEA_GREEN.darker() :
                    Theme.SEA_GREEN);

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        super.paintComponent(g2);
        g2.dispose();
    }
}
