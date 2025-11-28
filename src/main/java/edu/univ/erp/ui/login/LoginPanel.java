package edu.univ.erp.ui.login;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.awt.geom.Ellipse2D;

// Domain Imports
import edu.univ.erp.ui.common.DashboardTheme;
import edu.univ.erp.domain.User;
import edu.univ.erp.auth.session.Session;
import edu.univ.erp.domain.AuthClass;
import edu.univ.erp.domain.Role;

public class LoginPanel extends JPanel {
    private final LoginController controller;

    
    // UI Components
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel statusLabel;
    private final JLabel subtitleLabel; 
    
    // Role Buttons
    private CircleButton studentBtn;
    private CircleButton instructorBtn;
    private CircleButton adminBtn;
    private String selectedRole = "STUDENT"; 

    // --- VISUAL ASSETS ---
    private static final Color IIITD_COLOR = new Color(31, 78, 70); // Dark Teal
    private static final Color BTN_COLOR = new Color(60, 179, 113); // Green
    private Image backgroundImage;
    private ImageIcon logoIcon;

    public LoginPanel(LoginController controller) {
        this.controller = controller;

        // 1. Load Assets
        loadAssets();

        setLayout(new GridBagLayout());
        
        // --- CENTRAL CARD ---
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(IIITD_COLOR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(450, 700));
        card.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 0, 10, 0);

        // --- 1. LOGO (UPDATED: Made Wider) ---
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        if (logoIcon != null) {
            // Changed from 120x120 to 180x90 to support wider logos
            Image img = logoIcon.getImage().getScaledInstance(180, 90, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
        } else {
            logoLabel.setText("IIITD");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
            logoLabel.setForeground(Color.WHITE);
        }
        gbc.insets = new Insets(0, 0, 10, 0);
        card.add(logoLabel, gbc);

        // --- 2. WELCOME TEXT ---
        JLabel title = new JLabel("Welcome Back!", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        gbc.gridy++;
        card.add(title, gbc);

        subtitleLabel = new JLabel("Select Your Profile Type", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 20, 0);
        card.add(subtitleLabel, gbc);

        // --- 3. CIRCULAR ROLE BUTTONS ---
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 25, 0);
        card.add(createCircularRoleSelector(), gbc);

        // --- 4. INPUT FIELDS ---
        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        JLabel userLbl = new JLabel("Username:");
        userLbl.setForeground(Color.WHITE);
        userLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        card.add(userLbl, gbc);

        gbc.gridy++;
        usernameField = createStyledInputField();
        card.add(usernameField, gbc);

        gbc.gridy++;
        JLabel passLbl = new JLabel("Password:");
        passLbl.setForeground(Color.WHITE);
        passLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        card.add(passLbl, gbc);

        gbc.gridy++;
        passwordField = createStyledPasswordField();
        card.add(passwordField, gbc);

        // --- 5. LOGIN BUTTON ---
        gbc.gridy++;
        gbc.insets = new Insets(25, 0, 10, 0);
        JButton loginBtn = new JButton("LOGIN");
        styleLoginButton(loginBtn);
        loginBtn.addActionListener(e -> performLogin());
        card.add(loginBtn, gbc);

        // --- 6. FOOTER ---
        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 0, 0);
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false);

        statusLabel = new JLabel(" ", SwingConstants.LEFT);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(new Color(255, 100, 100));

        JLabel forgotLink = new JLabel("Forgot login details?", SwingConstants.RIGHT);
        forgotLink.setForeground(Color.WHITE);
        forgotLink.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        forgotLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(card, "Contact Admin at admin@iiitd.ac.in");
            }
        });

        bottomRow.add(statusLabel, BorderLayout.WEST);
        bottomRow.add(forgotLink, BorderLayout.EAST);
        card.add(bottomRow, gbc);

        add(card, new GridBagConstraints());
        
        updateRoleSelection("STUDENT");
    }

    // --- CUSTOM PAINT FOR BACKGROUND (UPDATED) ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            // REMOVED: The dark overlay line has been removed so the image is not blurred/darkened.
        } else {
            g.setColor(new Color(20, 40, 40)); 
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void loadAssets() {
        try {
            URL bgUrl = getClass().getResource("src/main/resources/Untitled design.png");
            if (bgUrl != null) backgroundImage = new ImageIcon(bgUrl).getImage();
            
            URL logoUrl = getClass().getResource("src/main/resources/iiitd logo (1).png");
            if (logoUrl != null) logoIcon = new ImageIcon(logoUrl);
        } catch (Exception e) { System.out.println("Classpath load failed: " + e.getMessage()); }

        if (backgroundImage == null) {
            File f = new File("src/main/resources/Untitled design.png");
            if (f.exists()) backgroundImage = new ImageIcon(f.getAbsolutePath()).getImage();
        }
        if (logoIcon == null) {
            File f = new File("src/main/resources/iiitd logo (1).png");
            if (f.exists()) logoIcon = new ImageIcon(f.getAbsolutePath());
        }
    }

    private JPanel createCircularRoleSelector() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setOpaque(false);

        studentBtn = new CircleButton("S", "Student", "STUDENT");
        instructorBtn = new CircleButton("I", "Instructor", "INSTRUCTOR");
        adminBtn = new CircleButton("A", "Admin", "ADMIN");

        panel.add(studentBtn);
        panel.add(instructorBtn);
        panel.add(adminBtn);

        return panel;
    }

    private void updateRoleSelection(String role) {
        this.selectedRole = role;
        studentBtn.setSelectedState(false);
        instructorBtn.setSelectedState(false);
        adminBtn.setSelectedState(false);

        switch (role) {
            case "STUDENT" -> studentBtn.setSelectedState(true);
            case "INSTRUCTOR" -> instructorBtn.setSelectedState(true);
            case "ADMIN" -> adminBtn.setSelectedState(true);
        }
        subtitleLabel.setText("Login as: " + role.charAt(0) + role.substring(1).toLowerCase());
    }

    private JTextField createStyledInputField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(0, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setCaretColor(Color.BLACK);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(0, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setCaretColor(Color.BLACK);
        return field;
    }

    private void styleLoginButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(BTN_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
            new LineBorder(BTN_COLOR, 1),
            new EmptyBorder(10, 0, 10, 0)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 45));
    }
    private void performLogin() {
      
        String user = usernameField.getText().trim();
        
       
        String pass = new String(passwordField.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            statusLabel.setText("Please enter credentials.");
            return;
        }

        statusLabel.setText("Authenticating...");


        User authenticatedUser = controller.authenticate(user, pass);

        if (authenticatedUser != null) {
            statusLabel.setText("Success!");
            

            firePropertyChange("loginSuccess", null, authenticatedUser.getRole());
        } else {
            statusLabel.setText("Invalid username or password.");
        }
    }

    private void setStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? new Color(255, 100, 100) : Color.WHITE);
    }

    private class CircleButton extends JButton {
        private final String roleInitial;
        private final String roleName;
        private final String roleKey;
        private boolean isSelectedRole = false;

        public CircleButton(String initial, String name, String key) {
            super(initial);
            this.roleInitial = initial;
            this.roleName = name;
            this.roleKey = key;

            setPreferredSize(new Dimension(60, 60));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 22));
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addActionListener(e -> updateRoleSelection(roleKey));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    subtitleLabel.setText("Login as: " + roleName);
                    subtitleLabel.setForeground(Color.WHITE);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    String current = selectedRole.charAt(0) + selectedRole.substring(1).toLowerCase();
                    subtitleLabel.setText("Login as: " + current);
                    subtitleLabel.setForeground(new Color(200, 200, 200));
                }
            });
        }

        public void setSelectedState(boolean selected) {
            this.isSelectedRole = selected;
            if (selected) {
                setForeground(IIITD_COLOR);
            } else {
                setForeground(Color.WHITE);
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isSelectedRole) {
                g2.setColor(Color.WHITE);
                g2.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
            } else {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new Ellipse2D.Double(1, 1, getWidth()-2, getHeight()-2));
            }

            super.paintComponent(g);
            g2.dispose();
        }
    }
}