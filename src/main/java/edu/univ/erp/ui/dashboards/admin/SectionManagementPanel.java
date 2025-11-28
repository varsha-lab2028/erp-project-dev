package edu.univ.erp.ui.dashboards.admin;

import edu.univ.erp.domain.AuthClass;
import edu.univ.erp.domain.Section;
import edu.univ.erp.service.AdminService;
import edu.univ.erp.ui.common.DashboardComponents;
import edu.univ.erp.ui.common.DashboardTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class SectionManagementPanel extends JPanel {
    private final AdminService adminService;
    private JComboBox<String> instrBox;
    private DashboardComponents.TablePanel tablePanel;

    public SectionManagementPanel(AdminService adminService) {
        this.adminService = adminService;
        setLayout(new BorderLayout(20, 20));
        setBackground(DashboardTheme.BG_MAIN);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // 1. Form Card
        DashboardComponents.CardPanel formCard = new DashboardComponents.CardPanel();
        formCard.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("Manage Sections");
        title.setFont(DashboardTheme.FONT_SUBTITLE);
        title.setForeground(DashboardTheme.TEXT_PRIMARY);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        formCard.add(title, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(2, 4, 15, 15));
        fields.setOpaque(false);
        
        fields.add(createLabel("Course Code:"));
        JTextField cCode = new JTextField();
        DashboardComponents.styleControl(cCode);
        fields.add(cCode);
        
        fields.add(createLabel("Instructor:"));
        instrBox = new JComboBox<>();
        DashboardComponents.styleControl(instrBox);
        loadInstructors(); // Populate dropdown dynamically
        fields.add(instrBox);
        
        fields.add(createLabel("Room:"));
        JTextField roomTxt = new JTextField();
        DashboardComponents.styleControl(roomTxt);
        fields.add(roomTxt);
        
        fields.add(createLabel("Time:"));
        JTextField timeTxt = new JTextField();
        DashboardComponents.styleControl(timeTxt);
        fields.add(timeTxt);
        
        fields.add(createLabel("Day:"));
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        JComboBox<String> dayBox = new JComboBox<>(days);
        DashboardComponents.styleControl(dayBox);
        fields.add(dayBox);

        fields.add(createLabel("Capacity:"));
        JTextField capTxt = new JTextField("50");
        DashboardComponents.styleControl(capTxt);
        fields.add(capTxt);
        
        JButton addBtn = DashboardComponents.createPrimaryButton("Add Section");
        addBtn.addActionListener(e -> {
            try {
                String code = cCode.getText().trim();
                String instr = (String) instrBox.getSelectedItem();
                String room = roomTxt.getText().trim();
                String time = timeTxt.getText().trim();
                String day = (String) dayBox.getSelectedItem();
                int cap = Integer.parseInt(capTxt.getText().trim());

                adminService.createSectionFromUI(code, instr, room, day, time, cap);
                
                JOptionPane.showMessageDialog(this, "Section Created!");
                cCode.setText(""); roomTxt.setText(""); timeTxt.setText("");
                reloadTable(); // Refresh table
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Add button to grid (last slot)
        fields.add(new JLabel("")); 
        fields.add(addBtn);
        
        formCard.add(fields, BorderLayout.CENTER);

        // 2. Table Card container
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        
        // Initial Table Load
        reloadTable();
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        add(formCard, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void loadInstructors() {
        try {
            // Try to fetch instructors
            java.util.List<edu.univ.erp.domain.AuthClass> instructors = adminService.getAllInstructors();
            instrBox.removeAllItems();
            for(edu.univ.erp.domain.AuthClass u : instructors) {
                instrBox.addItem(u.username);
            }
        } catch (RuntimeException e) {
            // SILENTLY FAIL if not logged in (startup phase)
            // This prevents the "Access denied" crash
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reloadTable() {
        // Clear old table if exists
        Container parent = null;
        if(tablePanel != null) {
            parent = tablePanel.getParent();
            if(parent != null) parent.remove(tablePanel);
        }

        String[] cols = {"ID", "Course", "Room", "Time", "Capacity"};
        Object[][] data;
        
        try {
            List<Section> sections = adminService.getAllSections();
            data = new Object[sections.size()][cols.length];
            for(int i=0; i<sections.size(); i++) {
                Section s = sections.get(i);
                data[i][0] = s.getSectionId();
                data[i][1] = s.getCourseCode();
                data[i][2] = s.getClassroom();
                data[i][3] = s.getDay() + " " + s.getTimings();
                data[i][4] = s.getCapacity();
            }
        } catch (Exception e) {
            data = new Object[0][5];
        }

        tablePanel = new DashboardComponents.TablePanel("Active Sections", cols, data);
        
        if(parent != null) {
            parent.add(tablePanel, BorderLayout.CENTER);
            parent.revalidate();
            parent.repaint();
        }
    }
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(DashboardTheme.FONT_BOLD);
        lbl.setForeground(DashboardTheme.TEXT_SECONDARY);
        return lbl;
    }
}