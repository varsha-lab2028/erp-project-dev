/*this class is made to manage and reuse database connections safely
across the whole project. and connects to the MySQL server (main)*/
package edu.univ.erp.data;
import java.util.*;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ServerConnector {
    private static HikariDataSource auth_datasource;
    private static HikariDataSource erp_datasource;

    private static HikariDataSource make(String url_key, String user_key, String pass_key){
        String URL = System.getProperty(url_key, PropertyClass.get(url_key));
        String User = System.getProperty(user_key, PropertyClass.get(user_key));
        String Pass = System.getProperty(pass_key, PropertyClass.get(pass_key));

        if (URL == null) {
            throw new IllegalArgumentException(url_key + " missing");
        }
        if(User == null){
            throw new IllegalArgumentException(user_key + " missing");
        }

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(URL);
        cfg.setUsername(User);
        cfg.setPassword(Pass);
        cfg.setMaximumPoolSize(5);
        cfg.setMinimumIdle(1);
        cfg.setPoolName(url_key+"_pool");
        cfg.addDataSourceProperty("cachePrepStmts", "true");
        cfg.addDataSourceProperty("prepStmtCacheSize", "250");
        cfg.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return new HikariDataSource(cfg);
    }

    public static DataSource auth() {
        if (auth_datasource == null) auth_datasource = make("auth.jdbc.url","auth.jdbc.user","auth.jdbc.pass");
        return auth_datasource;
    }

    public static DataSource erp() {
        if (erp_datasource == null) erp_datasource = make("erp.jdbc.url","erp.jdbc.user","erp.jdbc.pass");
        return erp_datasource;
    }

    //getting one ERP connection directly
    public static Connection ERPConnection() throws SQLException {
        return erp().getConnection();
    }

    //closing pools at app shutdown
    public static void closeAll() {
        if (auth_datasource != null) {
            auth_datasource.close();
        }
        if (erp_datasource  != null) {
            erp_datasource.close();
        }
    }

    private ServerConnector() {}
}
