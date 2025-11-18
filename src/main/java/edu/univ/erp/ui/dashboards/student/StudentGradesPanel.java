package edu.univ.erp.ui.dashboards.student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentGradesPanel extends JPanel {
    public StudentGradesPanel(){
        setLayout(new BorderLayout());

        JPanel top = new JPanel(null);
        top.setPreferredSize(new Dimension(900, 44));
        JLabel title = new JLabel("Grades by component (read-only)");
        title.setBounds(10, 10, 350, 24);
        top.add(title);
        add(top, BorderLayout.NORTH);

        String[] cols = {"Course","Component","Weight%","Score","Section","Term"};
        DefaultTableModel model = new DefaultTableModel(cols, 0){ @Override public boolean isCellEditable(int r,int c){return false;}};
        JTable table = new JTable(model); table.setRowHeight(22);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Dummy data
        model.addRow(new Object[]{"CS101","Quiz","20","18","S01","Autumn 2025"});
        model.addRow(new Object[]{"CS101","Midterm","30","24","S01","Autumn 2025"});
        model.addRow(new Object[]{"CS101","End-Sem","50","41","S01","Autumn 2025"});
    }
}
