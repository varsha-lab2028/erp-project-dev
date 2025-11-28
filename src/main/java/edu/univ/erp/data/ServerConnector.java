package edu.univ.erp.data;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

// Simple DataSource implementation to remove HikariCP dependency
public class ServerConnector implements DataSource {
    private static final String URL_AUTH = "jdbc:mysql://192.168.41.50:3306/auth_db";
    private static final String URL_ERP = "jdbc:mysql://192.168.41.50:3306/erp_db";
    private static final String USER = "Disha";
    private static final String PASS = "2003"; // Your password

    private final String url;

    public ServerConnector(String url) {
        this.url = url;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, USER, PASS);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // Static helpers for your app
    public static DataSource auth() {
        return new ServerConnector(URL_AUTH);
    }

    public static DataSource erp() {
        return new ServerConnector(URL_ERP);
    }

    public static Connection ERPConnection() throws SQLException {
        return DriverManager.getConnection(URL_ERP, USER, PASS);
    }

    // Dummy implementations for interface compliance
    @Override public PrintWriter getLogWriter() { return null; }
    @Override public void setLogWriter(PrintWriter out) {}
    @Override public void setLoginTimeout(int seconds) {}
    @Override public int getLoginTimeout() { return 0; }
    @Override public Logger getParentLogger() throws SQLFeatureNotSupportedException { return null; }
    @Override public <T> T unwrap(Class<T> iface) throws SQLException { return null; }
    @Override public boolean isWrapperFor(Class<?> iface) throws SQLException { return false; }
}