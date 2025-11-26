package edu.univ.erp.ui.common;

import edu.univ.erp.access.AccessControl;
import edu.univ.erp.auth.session.Session;
import edu.univ.erp.util.LoginTheme;
import javax.swing.*;
import java.awt.*;

public class BaseDashboard extends JPanel { // Correctly extends JPanel
    protected final JLabel banner_label = new JLabel(" ");
    protected final JPanel content = new JPanel();
    protected final JPanel header_stack = new JPanel();

    public BaseDashboard(String title) {
        // Removed super(title) as JPanel doesn't take a title
        // Removed window-specific setup like setDefaultCloseOperation and setLocationRelativeTo

        // Use setPreferredSize for Panels, not setSize
        setPreferredSize(new Dimension(900, 650));
        setLayout(new BorderLayout());

        // Header Stack (Contains Menu + Banner)
        header_stack.setLayout(new BoxLayout(header_stack, BoxLayout.Y_AXIS));
        
        // Fix: Add MenuBar directly to the layout since Panels don't have setJMenuBar
        JMenuBar menuBar = makeMenuBar();
        // Ensure menu bar stretches full width
        menuBar.setMaximumSize(new Dimension(Short.MAX_VALUE, 30)); 
        header_stack.add(menuBar);
        
        add(header_stack, BorderLayout.NORTH);

        // Welcome Banner
        JPanel banner_panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        banner_panel.setBackground(LoginTheme.DEEP_SEA);
        
        // Ensure banner stretches full width in BoxLayout
        banner_panel.setPreferredSize(new Dimension(900, 40));
        banner_panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        
        banner_label.setFont(LoginTheme.FONT_SMALL);
        banner_label.setForeground(Color.WHITE);
        banner_panel.add(banner_label);
        
        header_stack.add(banner_panel);

        // Content Area
        content.setBackground(LoginTheme.PRIMARY_WHITE);
        content.setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);

        // Set Text / Maintenance Logic
        boolean isMaintenance = false;
        try {
            isMaintenance = AccessControl.isReadOnlyNow();
        } catch (Exception e) {
            isMaintenance = false;
        }
        
        if (isMaintenance) {
            banner_label.setText("MAINTENANCE MODE — Read Only");
            banner_panel.setBackground(new Color(200, 50, 50));
        } else {
            String user = Session.isLoggedIn() ? Session.user().getUsername() : "Guest";
            banner_label.setText("Welcome, " + user);
        }

        LoginTheme.applyTheme();
    }

    private JMenuBar makeMenuBar(){
        JMenuBar bar = new JMenuBar();
        
        // Optional: Style the menu bar to look integrated
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JMenu app = new JMenu("Your Account");

        String user = Session.isLoggedIn() ? Session.user().getUsername() : "-";
        JMenuItem who = new JMenuItem("Signed in as: " + user);
        who.setEnabled(false);

        JMenuItem logout = new JMenuItem("Logout");
        logout.addActionListener(e -> doLogout());

        app.add(who);
        app.addSeparator();
        app.add(logout);
        bar.add(app);
        return bar;
    }

    protected void setCenter(Component c){
        content.removeAll();
        content.add(c, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    private void doLogout(){
        // Fix: Use getWindowAncestor to close the parent frame
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        if (parentWindow != null) {
            parentWindow.dispose();
        }
        new edu.univ.erp.ui.ui_auth.LoginFrame().setVisible(true);
    }
}