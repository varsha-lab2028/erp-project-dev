package edu.univ.erp.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLConnectionTest {

    public static void main(String[] args) throws Exception {
        try (Connection auth = ServerConnector.authConnection();
             Connection erp = ServerConnector.ERPConnection()) {
            System.out.println("auth -> " + currentSchema(auth));
            System.out.println("erp  -> " + currentSchema(erp));
        }
    }

    private static String currentSchema(Connection c) throws Exception {
        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("select current_schema()")) {
            rs.next();
            return rs.getString(1);
        }
    }
}