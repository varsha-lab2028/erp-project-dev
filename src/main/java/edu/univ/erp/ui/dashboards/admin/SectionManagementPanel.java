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
    
    // CLASS LEVEL VARIABLES (Required for button logic)
    private JTextField cCode;
    private JComboBox<String> instrBox;
    private JTextField roomTxt;
    private JTextField timeTxt;
    private JComboBox<String> dayBox;
    private JTextField capTxt;
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
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        formCard.add(title, BorderLayout.NORTH);

        // FIX: Use GridLayout(0, 4) to allow unlimited rows, keeping 4 columns fixed.
        // [Label] [Input] [Label] [Input]
        JPanel fields = new JPanel(new GridLayout(0, 4, 15, 15));
        fields.setOpaque(false);
        
        // Row 1
        fields.add(createLabel("Course Code:"));
        cCode = new JTextField();
        DashboardComponents.styleControl(cCode);
        fields.add(cCode);
        
        fields.add(createLabel("Instructor:"));
        instrBox = new JComboBox<>();
        DashboardComponents.styleControl(instrBox);
        fields.add(instrBox);
        
        // Row 2
        fields.add(createLabel("Room:"));
        roomTxt = new JTextField();
        DashboardComponents.styleControl(roomTxt);
        fields.add(roomTxt);
        
        fields.add(createLabel("Time:"));
        timeTxt = new JTextField();
        DashboardComponents.styleControl(timeTxt);
        fields.add(timeTxt);
        
        // Row 3
        fields.add(createLabel("Day:"));
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        dayBox = new JComboBox<>(days);
        DashboardComponents.styleControl(dayBox);
        fields.add(dayBox);

        fields.add(createLabel("Capacity:"));
        capTxt = new JTextField("50");
        DashboardComponents.styleControl(capTxt);
        fields.add(capTxt);
        
        // Row 4 (Buttons)
        fields.add(new JLabel("")); // Spacer
        fields.add(new JLabel("")); // Spacer
        fields.add(new JLabel("")); // Spacer
        
        JButton addBtn = DashboardComponents.createPrimaryButton("Add Section");
        addBtn.addActionListener(e -> onAddSection());
        fields.add(addBtn);
        
        formCard.add(fields, BorderLayout.CENTER);

        // 2. Table Card
        String[] cols = {"ID", "Course", "Room", "Time", "Capacity"};
        // Initialize table reference
        tablePanel = new DashboardComponents.TablePanel("Active Sections", cols, new Object[0][5]);

        add(formCard, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        
        // Initial load
        loadData();
    }
    
    // --- AUTO REFRESH ---
    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            loadData();
        }
    }

    private void loadData() {
        loadInstructors();
        reloadTable();
    }
    
    private void loadInstructors() {
        try {
            List<AuthClass> instructors = adminService.getAllInstructors();
            instrBox.removeAllItems();
            
            if (instructors.isEmpty()) {
                instrBox.addItem("No Instructors Found");
            } else {
                for(AuthClass u : instructors) {
                    instrBox.addItem(u.username);
                }
            }
        } catch (Exception e) {
            // Silent fail on init
        }
    }

    private void onAddSection() {
        try {
            String code = cCode.getText().trim();
            String instr = (String) instrBox.getSelectedItem();
            String room = roomTxt.getText().trim();
            String time = timeTxt.getText().trim();
            String day = (String) dayBox.getSelectedItem();
            
            if (code.isEmpty() || room.isEmpty() || time.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            int cap = Integer.parseInt(capTxt.getText().trim());

            adminService.createSectionFromUI(code, instr, room, day, time, cap);
            
            JOptionPane.showMessageDialog(this, "Section Created Successfully!");
            
            // Clear fields
            cCode.setText(""); 
            roomTxt.setText(""); 
            timeTxt.setText("");
            
            reloadTable();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacity must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reloadTable() {
        try {
            List<Section> sections = adminService.getAllSections();
            Object[][] data = new Object[sections.size()][5];
            
            for(int i=0; i<sections.size(); i++) {
                Section s = sections.get(i);
                data[i][0] = s.getSectionId();
                data[i][1] = s.getCourseCode();
                data[i][2] = s.getClassroom();
                data[i][3] = s.getDay() + " " + s.getTimings();
                data[i][4] = s.getCapacity();
            }
            
            // Access the JTable inside TablePanel to update model directly
            // This prevents UI flickering
            if (tablePanel.getComponentCount() > 1) {
                JScrollPane scroll = (JScrollPane) tablePanel.getComponent(1);
                JTable table = (JTable) scroll.getViewport().getView();
                javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
                
                model.setRowCount(0);
                String[] cols = {"ID", "Course", "Room", "Time", "Capacity"};
                model.setColumnIdentifiers(cols); // Ensure cols are correct
                
                for (Object[] row : data) {
                    model.addRow(row);
                }
            }
            
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }
    
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(DashboardTheme.FONT_BOLD);
        lbl.setForeground(DashboardTheme.TEXT_SECONDARY);
        return lbl;
    }
}