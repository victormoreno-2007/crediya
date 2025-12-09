package com.crediya.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbd:mysql://localhost:3305/crediya_db";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    public static Connection getConexion(){
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error conexión: " + e.getMessage());
        }
        return con;
    }

}
