package edu.univ.erp.ui.dashboards.instructor;

import edu.univ.erp.service.InstructorService;
import javaxin.swing.*;
import java.awt.*;

public class GradebookPanel extends JPanel {
    public GradebookPanel(InstructorService instructorService) {
        setLayout(new GridBagLayout());
        add(new JLabel("Gradebook Panel - Editable Table Coming Soon!"));
    }
}