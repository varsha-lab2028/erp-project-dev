package edu.univ.erp.data;

import edu.univ.erp.util.PropertyUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// The only class that knows how to reach the database.
// URL, user and password come from application.properties.
public final class ServerConnector {

    private ServerConnector() {}

    public static Connection authConnection() throws SQLException {
        return open("auth");
    }

    public static Connection ERPConnection() throws SQLException {
        return open("erp");
    }

    private static Connection open(String prefix) throws SQLException {
        return DriverManager.getConnection(
                PropertyUtil.get(prefix + ".jdbc.url"),
                PropertyUtil.get(prefix + ".jdbc.user"),
                PropertyUtil.get(prefix + ".jdbc.pass"));
    }
}