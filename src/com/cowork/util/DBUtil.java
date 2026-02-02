package com.cowork.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    public static Connection getDBConnection() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");

        Connection con = DriverManager.getConnection(
            "jdbc:oracle:thin:@localhost:1521:xe",
            "system",
            "alderin4"
        );
        con.setAutoCommit(false);
        return con;
    }
}