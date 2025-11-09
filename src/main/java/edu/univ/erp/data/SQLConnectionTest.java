package edu.univ.erp.data;

import java.sql.*;

public class SQLConnectionTest {
    public static void main(String[] args) throws Exception {
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/auth_db", "root", "root");
        System.out.println("Connected: " + !c.isClosed());
        c.close();
    }
}
