package edu.univ.erp.ui.common;

import javax.swing.*;
import java.awt.*;

/**
 * A utility class to show standardized dialog boxes (success, error, confirmation)
 * for the application.
 */
public class DialogHelper {

    // Private constructor so it cannot be instantiated.
    private DialogHelper() {}

    /**
     * Shows a standard error message dialog.
     * @param parent The parent component (e.g., the panel or frame)
     * @param message The error message to display.
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a standard success message dialog.
     * @param parent The parent component.
     * @param message The success message to display.
     */
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a "Not Allowed" message, as required by the access rules.
     * @param parent The parent component.
     * @param message The specific reason (e.g., "Section full", "Not your section"). [cite: 18, 121, 123]
     */
    public static void showNotAllowed(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Action Not Allowed", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows a confirmation dialog (Yes/No).
     * @param parent The parent component.
     * @param message The question to ask (e.g., "Are you sure you want to drop this course?").
     * @return true if the user clicked Yes, false otherwise.
     */
    public static boolean showConfirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(
            parent,
            message,
            "Confirm Action",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }
}