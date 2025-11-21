package edu.univ.erp.ui.common;

import java.awt.Color;
import java.awt.Font;

public class DashboardTheme {
    // IIITD Color Palette (Dark Green/Teal)
    public static final Color PRIMARY_COLOR = new Color(0, 59, 54);  // #003B36
    public static final Color PRIMARY_DARK = PRIMARY_COLOR;           // Alias for consistency
    public static final Color SECONDARY_GREEN = new Color(0, 165, 110); // #00A56E
    
    // Backgrounds & Neutrals
    public static final Color BG_LIGHT = new Color(247, 248, 250);      // #F7F8FA
    public static final Color BG_MEDIUM = new Color(64, 64, 64);        // Medium gray for components
    public static final Color BORDER_GRAY = new Color(229, 231, 235);   // #E5E7EB
    public static final Color TEXT_PRIMARY = new Color(26, 26, 26);     // #1A1A1A
    public static final Color TEXT_SECONDARY = new Color(107, 107, 107);// #6B6B6B
    public static final Color TEXT_LIGHT = Color.WHITE;                  // Light text for dark backgrounds
    public static final Color TEXT_DARK = new Color(26, 26, 26);        // Dark text for light backgrounds

    // Functional Accents
    public static final Color ACCENT_RED = new Color(239, 68, 68);
    public static final Color ACCENT_YELLOW = new Color(251, 191, 36);
    public static final Color ACCENT_ORANGE = new Color(249, 115, 22);
    public static final Color ACCENT_PURPLE = new Color(147, 51, 234);

    // Typography
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
}