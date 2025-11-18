package edu.univ.erp.ui.dashboards.student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentTimetablePanel extends JPanel {
    public StudentTimetablePanel(){
        setLayout(new BorderLayout());
        String[] cols = {"Day","Start","End","Course","Section","Room"};
        DefaultTableModel model = new DefaultTableModel(cols, 0){ @Override public boolean isCellEditable(int r,int c){return false;}};
        JTable table = new JTable(model); table.setRowHeight(22);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Dummy view (Week-4 DAO: build rows from enrollments/sections)
        model.addRow(new Object[]{"Mon","10:00","11:00","CS101","S01","C101"});
        model.addRow(new Object[]{"Thu","14:00","15:00","HS105","S02","B204"});
    }
}
