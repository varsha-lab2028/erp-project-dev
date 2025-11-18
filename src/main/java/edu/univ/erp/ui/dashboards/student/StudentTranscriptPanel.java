package edu.univ.erp.ui.dashboards.student;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentTranscriptPanel extends JPanel{
    public StudentTranscriptPanel(){
        setLayout(new BorderLayout());

        JPanel top = new JPanel(null);
        top.setPreferredSize(new Dimension(900, 44));
        JButton exportCsv = new JButton("Export CSV");
        JButton exportPdf = new JButton("Export PDF");
        exportCsv.setBounds(10, 10, 120, 24);
        exportPdf.setBounds(140, 10, 120, 24);
        top.add(exportCsv); top.add(exportPdf);
        add(top, BorderLayout.NORTH);

        String[] cols = {"Course","Title","Credits","Grade","Term"};
        DefaultTableModel model = new DefaultTableModel(cols, 0){ @Override public boolean isCellEditable(int r,int c){return false;}};
        JTable table = new JTable(model); table.setRowHeight(22);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Dummy completed courses
        model.addRow(new Object[]{"CS101","Intro to CS",3,"A","Autumn 2025"});
        model.addRow(new Object[]{"HS105","Ethics",2,"B+","Autumn 2025"});

        // Stubs for exports
        exportCsv.addActionListener(e -> JOptionPane.showMessageDialog(this, "Transcript CSV exported (stub)"));
        exportPdf.addActionListener(e -> JOptionPane.showMessageDialog(this, "Transcript PDF exported (stub)"));
    }
}
