package edu.univ.erp.ui.ui_auth;
import edu.univ.erp.auth.AuthDAO;
import edu.univ.erp.auth.PasswordHasher;
import edu.univ.erp.auth.session.Session;
import edu.univ.erp.domain.AuthClass;
import edu.univ.erp.domain.User;
import edu.univ.erp.ui.dashboards.student.StudentDashboardPanel;
import edu.univ.erp.service.StudentService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame{
    private final JTextField userField = new JTextField();
    private final JPasswordField passField = new JPasswordField();
    private final JLabel message = new JLabel(" ");
    private final JButton loginBtn = new JButton("Login");


    public LoginFrame(){
        super("University ERP — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 230);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(null); 
        add(form, BorderLayout.CENTER);

        JLabel uLbl = new JLabel("Username:");
        JLabel pLbl = new JLabel("Password:");

        uLbl.setBounds(30, 30, 100, 24);
        userField.setBounds(140, 30, 220, 24);
        pLbl.setBounds(30, 70, 100, 24);
        passField.setBounds(140, 70, 220, 24);
        loginBtn.setBounds(140, 110, 100, 28);
        message.setBounds(30, 150, 330, 24);
        message.setForeground(new Color(170, 0, 0));

        form.add(uLbl); form.add(userField);
        form.add(pLbl); form.add(passField);
        form.add(loginBtn); form.add(message);

        getRootPane().setDefaultButton(loginBtn);
        loginBtn.addActionListener(e -> doLogin());
    }

    private void doLogin(){
        String u = userField.getText().trim();
        String p = new String(passField.getPassword());
        if (u.isEmpty() || p.isEmpty()) { message.setText("Please enter username and password."); return; }
        AuthDAO auth_dao = new AuthDAO();
        try {
            AuthClass ar = auth_dao.findByUsername(u);
            if (ar == null || !PasswordHasher.verifyHash(p, ar.password_hash)) {
                message.setText("Incorrect username or password.");
                return;
            }
            if (ar.auth_status != null && ar.auth_status.equalsIgnoreCase("BLOCKED")) {
                message.setText("Account is blocked.");
                return;
            }
            User user = new edu.univ.erp.auth.AuthDAO().toUser(ar);
            Session.login(user);
            auth_dao.updateLastLogin(user.getUserId());
            routeToDashboard();
        } catch (Exception ex) {
            message.setText("Login failed. Check DB connection.");
        }
    }

    private void routeToDashboard(){
        switch (Session.user().getRole()){
            case STUDENT: new StudentDashboardPanel(new StudentService()).setVisible(true); break;
            
        }
        dispose();
    }
}
