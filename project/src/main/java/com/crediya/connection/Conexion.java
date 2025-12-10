package com.crediya.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // Verifica que el puerto 3305 sea correcto según tu Docker
    private static final String URL = "jdbc:mysql://localhost:3305/crediya_db";
    private static final String USER = "root";
    private static final String PASSWORD = "admin"; // Tu contraseña de Docker

    public static Connection getConexion() throws SQLException {
        try {
            // --- ESTA ES LA LÍNEA MÁGICA QUE TE FALTA ---
            Class.forName("com.mysql.cj.jdbc.Driver");
            // ---------------------------------------------
        } catch (ClassNotFoundException e) {
            throw new SQLException("❌ El Driver MySQL no se encontró. Revisa Maven.", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}