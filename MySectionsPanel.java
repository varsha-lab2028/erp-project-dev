package edu.univ.erp.ui.dashboards.instructor;

import edu.univ.erp.service.InstructorService;
import javax.swing.*;
import java.awt.*;

public class MySectionsPanel extends JPanel {
    public MySectionsPanel(InstructorService instructorService) {
        setLayout(new GridBagLayout());
        add(new JLabel("My Sections Panel - Coming Soon!"));
    }
}