package edu.univ.erp.ui.student;
import edu.univ.erp.access.AccessControl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentRegistrationsPanel extends JPanel{
    private final JTable table;
    private final JButton drop_button = new JButton("Drop Selected");
    private final JLabel status = new JLabel(" ");

    public StudentRegistrationsPanel(){
        setLayout(new BorderLayout());

        JPanel top = new JPanel(null);
        top.setPreferredSize(new Dimension(900, 44));
        drop_button.setBounds(10, 10, 140, 24);
        top.add(drop_button);
        add(top, BorderLayout.NORTH);

        String[] cols = {"Course","Section","Day","Time","Room","Term"};
        DefaultTableModel model = new DefaultTableModel(cols, 0){ @Override public boolean isCellEditable(int r,int c){return false;}};
        table = new JTable(model); table.setRowHeight(22);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(status, BorderLayout.WEST);
        add(bottom, BorderLayout.SOUTH);

        // Dummy data
        model.addRow(new Object[]{"CS101","S01","Mon","10:00–11:00","C101","Autumn 2025"});
        model.addRow(new Object[]{"HS105","S02","Thu","14:00–15:00","B204","Autumn 2025"});
        status.setText("2 registrations");

        drop_button.addActionListener(e -> onDrop(model));
    }
    private void onDrop(DefaultTableModel model){
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a registration to drop."); return; }
        if (!AccessControl.canAccess("STU_REGISTER")) { JOptionPane.showMessageDialog(this, "Cannot drop now (maintenance ON). "); return; }
        String course = model.getValueAt(row, 0).toString();
        int choice = JOptionPane.showConfirmDialog(this, "Drop " + course + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            // Stub remove row
            ((DefaultTableModel)table.getModel()).removeRow(row);
            JOptionPane.showMessageDialog(this, "Dropped (stub)");
        }
    }
}

