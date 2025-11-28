package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EditProfilePanel extends JPanel {

    private Runnable onSave;
    private Runnable onCancel;

    public EditProfilePanel(Runnable onSave, Runnable onCancel) {
        this.onSave = onSave;
        this.onCancel = onCancel;
        
        setLayout(new BorderLayout());
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(0, 0, 0, 0)); 

        
        DashboardComponents.CardPanel card = new DashboardComponents.CardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

       
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        JLabel title = new JLabel("Edit Profile Details");
        title.setFont(DashboardTheme.FONT_SUBTITLE);
        title.setForeground(DashboardTheme.TEXT_PRIMARY);
        
        header.add(title, BorderLayout.WEST);
        card.add(header);

       
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setOpaque(false);
        formGrid.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 15, 20); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 0.5;

        
        addFormField(formGrid, "Full Name", "Aman Gupta", 0, 0, gbc);
        addFormField(formGrid, "Email Address", "admin.sys@iiitd.ac.in", 1, 0, gbc);
        
        
        addFormField(formGrid, "Phone Number", "+91 98765 43210", 0, 1, gbc);
        addFormField(formGrid, "Office Location", "Academic Block, Room 204", 1, 1, gbc);

        card.add(formGrid);
        
        
        JSeparator sep = new JSeparator();
        sep.setForeground(DashboardTheme.BORDER_COLOR);
        sep.setMaximumSize(new Dimension(2000, 1));
        card.add(sep);
        
        JPanel secHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        secHeader.setOpaque(false);
        secHeader.setBorder(new EmptyBorder(15, 20, 5, 20));
        JLabel secTitle = new JLabel("Security Settings");
        secTitle.setFont(DashboardTheme.FONT_BOLD);
        secTitle.setForeground(DashboardTheme.PRIMARY);
        secHeader.add(secTitle);
        card.add(secHeader);
        
        JPanel secGrid = new JPanel(new GridBagLayout());
        secGrid.setOpaque(false);
        secGrid.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        
        addPassField(secGrid, "New Password", 0, 0, gbc);
        addPassField(secGrid, "Confirm Password", 1, 0, gbc);
        
        card.add(secGrid);

       
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(DashboardTheme.FONT_BOLD);
        btnCancel.setForeground(DashboardTheme.TEXT_SECONDARY);
        btnCancel.setContentAreaFilled(false);
        btnCancel.setBorder(BorderFactory.createLineBorder(DashboardTheme.BORDER_COLOR));
        btnCancel.setPreferredSize(new Dimension(100, 35));
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> onCancel.run());

        JButton btnSave = DashboardComponents.createPrimaryButton("Save Changes");
        btnSave.addActionListener(e -> onSave.run());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        card.add(btnPanel);

       
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(DashboardTheme.BG_MAIN);
        wrapper.add(card, BorderLayout.NORTH);
        
        add(new JScrollPane(wrapper) {
            { setBorder(null); getViewport().setBackground(DashboardTheme.BG_MAIN); }
        }, BorderLayout.CENTER);
    }

    private void addFormField(JPanel panel, String label, String value, int gridx, int gridy, GridBagConstraints gbc) {
        gbc.gridx = gridx;
        gbc.gridy = gridy * 2; 
        JLabel lbl = new JLabel(label);
        lbl.setFont(DashboardTheme.FONT_BOLD);
        lbl.setForeground(DashboardTheme.TEXT_SECONDARY);
        panel.add(lbl, gbc);

        gbc.gridy = gridy * 2 + 1; 
        JTextField field = new JTextField(value);
        DashboardComponents.styleControl(field);
        field.setPreferredSize(new Dimension(200, 35));
        panel.add(field, gbc);
    }
    
    private void addPassField(JPanel panel, String label, int gridx, int gridy, GridBagConstraints gbc) {
        gbc.gridx = gridx;
        gbc.gridy = gridy * 2;
        JLabel lbl = new JLabel(label);
        lbl.setFont(DashboardTheme.FONT_BOLD);
        lbl.setForeground(DashboardTheme.TEXT_SECONDARY);
        panel.add(lbl, gbc);

        gbc.gridy = gridy * 2 + 1;
        JPasswordField field = new JPasswordField();
        DashboardComponents.styleControl(field);
        field.setPreferredSize(new Dimension(200, 35));
        panel.add(field, gbc);
    }
}