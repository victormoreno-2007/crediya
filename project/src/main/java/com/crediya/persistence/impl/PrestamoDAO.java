package com.crediya.persistence.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.crediya.models.Prestamo;
public class PrestamoDAO {

    private final Connection conn;
    
    public PrestamoDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean  guardarPrestamo(Prestamo p) {
        String sql = "INSERT INTO prestamos (cliente_id, empleado_id, monto, interes, cuotas, fecha_inicio, estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){ 

            stmt.setInt(1, p.getCliente().getId());
            stmt.setInt(2, p.getEmpleado().getId());
            stmt.setDouble(3, p.getMonto());
            stmt.setDouble(4, p.getInteres());
            stmt.setInt(5, p.getCuotas());
            stmt.setDate(6, Date.valueOf(p.getFechaInicio()));
            stmt.setString(7, p.getEstado());

            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if(rs.next()){
                    p.setId(rs.getInt(1));
                }
                return true;
            }
            
        } catch (Exception e) {
            
          System.err.println("Error al guardar préstamo: " + e.getMessage());
        }
        return false;
    }
}
