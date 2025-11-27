package edu.univ.erp.service;

import edu.univ.erp.data.ServerConnector;
import edu.univ.erp.data.SettingsDAO;

import java.sql.*;

public class MaintenanceService {
    //private static final String command = "SELECT value FROM settings WHERE k = 'maintenance.on'";

    private final SettingsDAO dao = new SettingsDAO();

    // Read maintenance flag
    public boolean isMaintenanceOn() {
        try {
            return dao.getMaintenanceMode();
        } catch (Exception e) {
            return false;
        }
    }

    // Toggle maintenance flag
    public void toggleMaintenance(boolean mode) {
        try {
            dao.setMaintenanceMode(mode);
        } catch (Exception ignored) {}
    }

    /*
    public boolean isMaintenanceOn(){
        try (Connection c = ServerConnector.erp().getConnection();
             PreparedStatement ps = c.prepareStatement(command);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String v = rs.getString(1);
                return v != null && ("true".equalsIgnoreCase(v) || "1".equals(v));
            }
        } catch (Exception ignored) {}
        return false;
    }
     */
}
