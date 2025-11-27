package edu.univ.erp.ui.common;

import java.awt.Color;
import java.awt.Font;

public class DashboardTheme {
    // Colors are non-final to support runtime switching
    public static Color PRIMARY = new Color(0, 150, 136); // IIITD Teal
    public static Color PRIMARY_DARK = new Color(0, 121, 107);
    public static Color ACCENT = new Color(255, 111, 0);
    
    public static Color BG_MAIN = new Color(240, 242, 245);
    public static Color BG_SIDEBAR = new Color(38, 50, 56);
    public static Color SURFACE = Color.WHITE;
    
    public static Color TEXT_PRIMARY = new Color(33, 33, 33);
    public static Color TEXT_SECONDARY = new Color(117, 117, 117);
    public static Color BORDER_COLOR = new Color(224, 224, 224);

    public static Color SUCCESS = new Color(67, 160, 71);
    public static Color WARNING = new Color(255, 179, 0);
    public static Color DANGER = new Color(229, 57, 53);
    
    // FIX: Added INFO back to prevent errors in Admin/Instructor panels
    public static Color INFO = new Color(33, 150, 243); 

    // Fonts remain constant
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    public static boolean isDark = false;

    public static void setTheme(boolean dark) {
        isDark = dark;
        if (dark) {
            // Dark Mode Palette
            BG_MAIN = new Color(18, 18, 18);
            BG_SIDEBAR = new Color(30, 30, 30);
            SURFACE = new Color(45, 45, 45); // Card Color
            
            TEXT_PRIMARY = new Color(240, 240, 240);
            TEXT_SECONDARY = new Color(180, 180, 180);
            BORDER_COLOR = new Color(60, 60, 60);
            
            PRIMARY = new Color(38, 166, 154); 
            PRIMARY_DARK = new Color(0, 137, 123);
            
            // Adjust Status Colors for Dark Mode
            INFO = new Color(66, 165, 245); 
        } else {
            // Light Mode Palette (Default IIITD)
            BG_MAIN = new Color(240, 242, 245);
            BG_SIDEBAR = new Color(38, 50, 56);
            SURFACE = Color.WHITE;
            
            TEXT_PRIMARY = new Color(33, 33, 33);
            TEXT_SECONDARY = new Color(117, 117, 117);
            BORDER_COLOR = new Color(224, 224, 224);
            
            PRIMARY = new Color(0, 150, 136);
            PRIMARY_DARK = new Color(0, 121, 107);
            
            INFO = new Color(33, 150, 243);
        }
    }
}