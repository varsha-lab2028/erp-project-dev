package edu.univ.erp.data;

import java.sql.*;
import java.time.LocalDateTime;

public class SettingsDAO {
    //return raw value for key or null if not there
    public static String get(String key) throws SQLException {
        String command = "SELECT value FROM settings WHERE k = ?";

        //connect to the ERP Database
        try (Connection connection = ServerConnector.ERPConnection();
             PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setString(1, key);

            try (ResultSet rs = ps.executeQuery()) {
                //if matching key found, then it will return
                if (rs.next()) {
                    return rs.getString("value");
                }
                else {
                    return null;
                }
            }
        }
    }

    //parse integer setting, will return null if empty or not valid integer
    public Integer getInt(String key) throws SQLException {
        String v = get(key);
        if (v == null) {
            return null;
        }
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    //parsing the date time, this is static
    public static LocalDateTime getDateTime(String key) throws SQLException {
        String v = get(key);
        if (v == null || v.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(v.trim());
        } catch (Exception e) {
            return null;
        }
    }

    //upsert setting, if value doesn't exist insertion will occur, if it does, the value will get updated
    public void upsertSetting(String key, String value) throws SQLException {
        final String sql = """
            INSERT INTO settings(k,v) VALUES (?,?)
            ON DUPLICATE KEY UPDATE v = VALUES(v)
        """;
        try (Connection c = ServerConnector.ERPConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, key);
            ps.setString(2, value);
            ps.executeUpdate();
        }
    }

    //Read maintenance flag
    public boolean getMaintenanceMode() throws SQLException {
        String v = get("maintenance_on");
        if (v == null) return false;
        return Boolean.parseBoolean(v.trim());
    }

    //Update maintenance flag
    public void setMaintenanceMode(boolean mode) throws SQLException {
        upsertSetting("maintenance_on", String.valueOf(mode));
    }
}
