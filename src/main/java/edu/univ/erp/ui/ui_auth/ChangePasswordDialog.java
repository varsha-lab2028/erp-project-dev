package edu.univ.erp.ui.ui_auth;

import edu.univ.erp.auth.AuthDAO;
import edu.univ.erp.auth.PasswordHasher;
import edu.univ.erp.auth.session.Session; //
import edu.univ.erp.domain.AuthClass;
import edu.univ.erp.domain.User;

import javax.swing.*;
import java.awt.*;


public class ChangePasswordDialog extends JDialog {

    private final JPasswordField oldPasswordField = new JPasswordField(20);
    private final JPasswordField newPasswordField = new JPasswordField(20);
    private final JPasswordField confirmPasswordField = new JPasswordField(20);
    private final JLabel statusLabel = new JLabel(" ");
    private final JButton okButton = new JButton("OK");
    private final JButton cancelButton = new JButton("Cancel");

    private boolean passwordChanged = false;
    private final User currentUser;

    /**
     * Creates a new ChangePasswordDialog.
     * @param owner The frame that owns this dialog (e.g., the user's dashboard).
     */
    public ChangePasswordDialog(Frame owner) {
        super(owner, "Change Password", true); // 'true' makes it modal

        // Get the current user from the session
        this.currentUser = Session.user(); 
        if (currentUser == null) {
            // This should not happen if the dialog is launched from a dashboard
            JOptionPane.showMessageDialog(owner, 
                "Error: No user is currently logged in.", 
                "Session Error", 
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setSize(450, 270);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        initComponents();
    }

    private void initComponents() {
     
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Old Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(oldPasswordField, gbc);

   
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(newPasswordField, gbc);


        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);


        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        statusLabel.setForeground(Color.RED);
        formPanel.add(statusLabel, gbc);

     
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

 
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


        okButton.addActionListener(e -> handleChangePassword());
        cancelButton.addActionListener(e -> dispose()); 
    }

    private void handleChangePassword() {
    
        String oldPass = new String(oldPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

       
        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            statusLabel.setText("All fields are required.");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            statusLabel.setText("New passwords do not match.");
            return;
        }

        if (newPass.equals(oldPass)) {
            statusLabel.setText("New password must be different from the old password.");
            return;
        }

        AuthDAO authDAO = new AuthDAO();
        try {
       
            AuthClass authData = authDAO.findByUsername(currentUser.getUsername());
            if (authData == null || !PasswordHasher.verifyHash(oldPass, authData.password_hash)) {
                statusLabel.setText("Incorrect old password.");
                return;
            }

         
            String newHash = PasswordHasher.hash(newPass);

         
            boolean success = authDAO.updatePassword(currentUser.getUserId(), newHash);

            if (success) {
                passwordChanged = true;
                JOptionPane.showMessageDialog(this, 
                    "Password changed successfully.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose(); 
            } else {
                statusLabel.setText("Failed to update password in database.");
            }

        } catch (Exception ex) {
            statusLabel.setText("An error occurred. Please try again.");
            
        }
    }

    /**
     * Used by the parent window to see if the action was successful.
     * @return true if the password was successfully changed, false otherwise.
     */
    public boolean wasPasswordChanged() {
        return passwordChanged;
    }
}