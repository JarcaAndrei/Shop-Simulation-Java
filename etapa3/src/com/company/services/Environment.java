package com.company.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Environment {
    static final String DB_URL = "jdbc:mysql://localhost:3306/store";
    static final String USER = "root";
    static final String PASS = "parola";
    static Connection conn;
//    static Statement stmt;

    static {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}