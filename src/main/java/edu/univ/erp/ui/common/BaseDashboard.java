package edu.univ.erp.ui.common;
import edu.univ.erp.access.AccessControl;
import edu.univ.erp.auth.session.Session;

import javax.swing.*;
import java.awt.*;

public class BaseDashboard extends JFrame{
    protected final JLabel banner = new JLabel(" ");
    protected final JPanel content = new JPanel();

    public BaseDashboard(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JPanel north = new JPanel(null); // absolute placement to demo setBounds
        north.setPreferredSize(new Dimension(900, 40));
        JLabel left = new JLabel();
        left.setBounds(12, 10, 600, 20);
        banner.setBounds(12, 10, 860, 20);
        north.add(banner);
        add(north, BorderLayout.NORTH);


        add(content, BorderLayout.CENTER);


        if (AccessControl.isReadOnlyNow()) {
            banner.setText("Maintenance Mode is ON — writes are disabled.");
        } else {
            banner.setText("Welcome, " + (Session.isLoggedIn()? Session.user().getUsername():""));
        }


        setJMenuBar(makeMenuBar());
    }


    private JMenuBar makeMenuBar(){
        JMenuBar bar = new JMenuBar();
        JMenu app = new JMenu("App");
        JMenuItem who = new JMenuItem("Signed in as: " + (Session.isLoggedIn()? Session.user().getUsername():"-"));
        who.setEnabled(false);
        JMenuItem logout = new JMenuItem("Logout");
        logout.addActionListener(e -> doLogout());
        app.add(who); app.addSeparator(); app.add(logout);
        bar.add(app);
        return bar;
    }


    protected void setCenter(Component c){
        content.removeAll();
        content.setLayout(new BorderLayout());
        content.add(c, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }


    private void doLogout(){
        dispose();
        new edu.univ.erp.ui.auth.LoginFrame().setVisible(true);
    }
}
