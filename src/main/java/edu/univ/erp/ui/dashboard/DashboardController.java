package edu.univ.erp.ui.dashboard;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DashboardController implements PropertyChangeListener {
    private DashboardPanel dashboardPanel;

    public DashboardController(DashboardViewModel viewModel) {
        this.dashboardPanel = new DashboardPanel();
        
        // Listen for logout button events
        dashboardPanel.addPropertyChangeListener(this);
    }

    public JPanel getPanel() {
        return dashboardPanel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        if ("logout".equals(propName)) {
            handleLogout();
        }
    }

    private void handleLogout() {
        System.out.println("Logout clicked — redirect to Login screen");
        // TODO: Switch to LoginPanel (we’ll do that in MainApp next)
    }
}
