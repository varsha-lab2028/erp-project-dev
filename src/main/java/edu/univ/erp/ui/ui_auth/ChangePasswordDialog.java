package edu.univ.erp.ui.ui_auth;

import edu.univ.erp.auth.AuthDAO;
import edu.univ.erp.auth.PasswordHasher;
import edu.univ.erp.auth.session.Session; //
import edu.univ.erp.domain.AuthClass;
import edu.univ.erp.domain.User;

import javax.swing.*;
import java.awt.*;

/**
 * A modal dialog for the currently logged-in user to change their password.
 */
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
        // --- 1. Form Panel (using GridBagLayout) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Old Password
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Old Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(oldPasswordField, gbc);

        // Row 1: New Password
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(newPasswordField, gbc);

        // Row 2: Confirm New Password
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);

        // Row 3: Status Label
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        statusLabel.setForeground(Color.RED);
        formPanel.add(statusLabel, gbc);

        // --- 2. Button Panel (OK/Cancel) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // --- 3. Add panels to dialog ---
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- 4. Action Listeners ---
        okButton.addActionListener(e -> handleChangePassword());
        cancelButton.addActionListener(e -> dispose()); // Just close
    }

    private void handleChangePassword() {
        // Get all passwords
        String oldPass = new String(oldPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

        // --- Validation ---
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

        // --- Backend Logic ---
        AuthDAO authDAO = new AuthDAO();
        try {
            // 1. Verify old password
            // We need to fetch the user's current hash from the DB
            AuthClass authData = authDAO.findByUsername(currentUser.getUsername());
            if (authData == null || !PasswordHasher.verifyHash(oldPass, authData.password_hash)) {
                statusLabel.setText("Incorrect old password.");
                return;
            }

            // 2. Hash new password
            String newHash = PasswordHasher.hash(newPass);

            // 3. Update in database
            boolean success = authDAO.updatePassword(currentUser.getUserId(), newHash);

            if (success) {
                passwordChanged = true;
                JOptionPane.showMessageDialog(this, 
                    "Password changed successfully.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close the dialog
            } else {
                statusLabel.setText("Failed to update password in database.");
            }

        } catch (Exception ex) {
            statusLabel.setText("An error occurred. Please try again.");
            // TODO: Log the exception (ex.printStackTrace())
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