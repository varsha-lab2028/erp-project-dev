package edu.univ.erp.service;

import edu.univ.erp.data.ServerConnector;
import java.sql.*;

public class MaintenanceService {
    private static final String command = "SELECT value FROM settings WHERE k ='maintenance.on'";

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
}
