package edu.univ.erp.ui.admin;

import edu.univ.erp.ui.admin.AdminDashboard;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Run the Swing application on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            
            // Optional: Set a modern Look and Feel like FlatLaf (as suggested in PDF [cite: 235])
            // If you add the FlatLaf library, uncomment the next 3 lines.
            // try {
            //     UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            // } catch (Exception ex) {
            //     System.err.println("Failed to initialize LaF");
            // }

            // Create the main application window
            JFrame frame = new JFrame("University ERP - Admin Dashboard");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);

            // Add the main admin dashboard panel to the frame
            frame.add(new AdminDashboard());

            // Center the window on the screen
            frame.setLocationRelativeTo(null);
            
            // Make the window visible
            frame.setVisible(true);
        });
    }
}