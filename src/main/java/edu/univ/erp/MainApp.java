package edu.univ.erp;
import com.formdev.flatlaf.FlatLightLaf;
import edu.univ.erp.ui.auth.LoginFrame;
import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FlatLightLaf.setup(); //setting up the look and feel using flatlaf

                // Create and show the login window
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
}
