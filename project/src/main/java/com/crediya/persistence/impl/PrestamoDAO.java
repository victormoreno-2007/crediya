package com.crediya.persistence.impl;

import java.sql.*;
import com.crediya.connection.Conexion;
import com.crediya.models.Prestamo;

public class PrestamoDAO {

    public boolean guardarPrestamo(Prestamo p) {
        String sql = "INSERT INTO prestamos (cliente_id, empleado_id, monto, interes, cuotas, fecha_inicio, estado, saldo_pendiente) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, p.getCliente().getId());
            ps.setInt(2, p.getEmpleado().getId());
            ps.setDouble(3, p.getMonto());
            ps.setDouble(4, p.getInteres());
            ps.setInt(5, p.getCuotas());
            ps.setDate(6, Date.valueOf(p.getFechaInicio()));
            ps.setString(7, p.getEstado());
            ps.setDouble(8, p.getSaldoPendiente());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setId(rs.getInt(1));
                    }
                }
                System.out.println("✅ Préstamo guardado con ID: " + p.getId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al guardar préstamo: " + e.getMessage());
        }
        return false;
    }
}