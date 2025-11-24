package edu.univ.erp.ui.common;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class MainERPFrame extends JFrame {
    //core fields, area where screens are changed (login/student/instructor/admin)
    //CardLayout = layout manager which controls how panels are displayed
    private final CardLayout cards = new CardLayout();
    private final JPanel main_area = new JPanel(cards);

    //maintenance banner
    private final JLabel maintenance_bar = new JLabel("Maintenance: OFF  |  Not logged in", SwingConstants.RIGHT);

    // menu items we need to enable/disable later
    private final JMenuItem menu_login = new JMenuItem("Login (right now fake)"); //fake login action, will be replaced with AuthApi + password check
    private final JMenuItem menu_logout = new JMenuItem("Logout");
    private final JMenuItem menu_exit = new JMenuItem("Exit");

    public MainERPFrame() {
        //constructor
        super("University ERP");
        initFrame();
        initMenuBar();
        initMainAreaCards();
        initMaintenanceBar();
        wireActions();
    }

    //basic structure of the frame
    private void initFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(960, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    //menu bar - options of login, logout and exit
    private void initMenuBar() {
        var menu_bar = new JMenuBar();
        var file = new JMenu("File");

        menu_logout.setEnabled(false); // no one is logged in yet

        file.add(menu_login);
        file.add(menu_login);
        file.addSeparator();
        file.add(menu_exit);

        menu_bar.add(file);
        setJMenuBar(menu_bar);
    }

    //Main area cards - adds login and 3 dashboards(student, admin, instructor)
    private JPanel createCenteredPanel(String title) {
        JPanel p = new JPanel(new java.awt.GridBagLayout());
        p.add(new JLabel(title), new java.awt.GridBagConstraints());
        return p;
    }

    private JPanel createAdminDashboard(){
        return createCenteredPanel("ADMIN DASHBOARD");
    }

    private JPanel createInstructorDashboard() {
        return createCenteredPanel("INSTRUCTOR DASHBOARD");
    }

    private JPanel createStudentDashboard() {
        return createCenteredPanel("STUDENT DASHBOARD");
    }

    private JPanel createLoginPanel() {
        return createCenteredPanel("LOGIN");
    }
    private void initMainAreaCards() {
        main_area.add(createLoginPanel(), "LOGIN");
        main_area.add(createStudentDashboard(), "STUDENT");
        main_area.add(createInstructorDashboard(), "INSTRUCTOR");
        main_area.add(createAdminDashboard(), "ADMIN");
        add(main_area, BorderLayout.CENTER);
    }

    //status bar, at the bottom, Adds a bottom label used for maintenance + session status messages.
    private void initMaintenanceBar() {
        maintenance_bar.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        add(maintenance_bar, BorderLayout.SOUTH);
    }

    //Wiring actions: fake login → Student card, logout → Login card, exit → close.
    private void wireActions() {
        menu_login.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                //just to switch to Student for now
                cards.show(main_area, "STUDENT");
                menu_login.setEnabled(false);
                menu_logout.setEnabled(true);
                maintenance_bar.setText("Maintenance: OFF  |  Logged in as: student@example");
            }
        });
        menu_logout.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                // Back to log in screen.
                cards.show(main_area, "LOGIN");
                menu_login.setEnabled(true);
                menu_logout.setEnabled(false);
                maintenance_bar.setText("Maintenance: OFF  |  Not logged in");
            }
        });
        menu_exit.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose();
            }
        });
    }

    //making the simple panels (login + 3 dashboards)
    private JPanel createLoginPanelStub() {
        var p = new JPanel(new GridBagLayout());
        p.add(new JLabel("LOGIN — use File → Login to simulate sign-in"), new GridBagConstraints());
        return p;
    }
    //student dashboard
    private JPanel createStudentDashboardStub() {
        var p = new JPanel(new GridBagLayout());
        p.add(new JLabel("Student Dashboard"), new GridBagConstraints());
        return p;
    }
    //instructor dashboard
    private JPanel createInstructorDashboardStub() {
        var p = new JPanel(new GridBagLayout());
        p.add(new JLabel("Instructor Dashboard"), new GridBagConstraints());
        return p;
    }
    //admin dashboard
    private JPanel createAdminDashboardStub() {
        var p = new JPanel(new GridBagLayout());
        p.add(new JLabel("Admin Dashboard"), new GridBagConstraints());
        return p;
    }

    //main method (flatlaf + launch)
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                com.formdev.flatlaf.FlatLightLaf.setup();
                new MainERPFrame().setVisible(true);
            }
        });
    }

}
