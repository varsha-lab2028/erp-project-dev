package edu.univ.erp.ui.common;

import edu.univ.erp.access.AccessControl;
import edu.univ.erp.auth.session.Session;
import edu.univ.erp.util.LoginTheme;
import javax.swing.*;
import java.awt.*;

public class BaseDashboard extends JFrame{
    protected final JLabel banner_label = new JLabel(" ");
    protected final JPanel content = new JPanel();
    //the container is at the top and stacks the components vertically
    protected final JPanel header_stack = new JPanel();

    public BaseDashboard(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        header_stack.setLayout(new BoxLayout(header_stack, BoxLayout.Y_AXIS));
        add(header_stack, BorderLayout.NORTH);

        //welcome banner
        JPanel banner_panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        banner_panel.setBackground(LoginTheme.DEEP_SEA);
        banner_panel.setPreferredSize(new Dimension(900, 40));
        banner_label.setFont(LoginTheme.FONT_SMALL);
        banner_label.setForeground(Color.WHITE);
        banner_panel.add(banner_label);
        // Add banner as the first item in the stack
        header_stack.add(banner_panel);

        //content area
        content.setBackground(LoginTheme.PRIMARY_WHITE);
        content.setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);

        //set text
        boolean isMaintenance = false;
        try {
            isMaintenance = AccessControl.isReadOnlyNow();
        } catch (Exception e) {
            // If database not available, assume not maintenance
            isMaintenance = false;
        }
        if (isMaintenance) {
            banner_label.setText("MAINTENANCE MODE — Read Only");
            //making the maintenance label red
            banner_panel.setBackground(new Color(200, 50, 50));
        } else {
            String user = Session.isLoggedIn() ? Session.user().getUsername() : "Guest";
            banner_label.setText("Welcome, " + user);
        }

        setJMenuBar(makeMenuBar());
        LoginTheme.applyTheme();
    }

    private JMenuBar makeMenuBar(){
        JMenuBar bar = new JMenuBar();
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
        dispose();
        new edu.univ.erp.ui.auth.LoginFrame().setVisible(true);
    }
}
