package com.crediya.persistence.impl;

import com.crediya.models.Pago;
import com.crediya.models.Prestamo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class PagoDAO {
    private final Connection conn;

    public PagoDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean registrarPago(Pago pago) {
        String sqlInsertPago = "INSERT INTO pagos (prestamo_id, fecha_pago, monto) VALUES (?, ?, ?)";
        String sqlSelectPrestamo = "SELECT saldo_pendiente FROM prestamos WHERE id = ?";
        String sqlUpdatePrestamo = "UPDATE prestamos SET saldo_pendiente = ?, estado = ? WHERE id = ?";

        try {
            boolean autoCommitOriginal = conn.getAutoCommit();
            conn.setAutoCommit(false);

            // 👇 AQUÍ: de objeto Prestamo → id INT para la BD
            Prestamo prestamo = pago.getPrestamo();
            int prestamoId = prestamo.getId();

            // 1) Insertar el pago
            try (PreparedStatement stmtPago = conn.prepareStatement(sqlInsertPago)) {
                stmtPago.setInt(1, prestamoId);
                stmtPago.setDate(2, java.sql.Date.valueOf(pago.getFechaPago()));
                stmtPago.setDouble(3, pago.getMonto());
                stmtPago.executeUpdate();
            }

            double saldoActual;

            // 2) Consultar saldo actual del préstamo
            try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelectPrestamo)) {
                stmtSelect.setInt(1, prestamoId);
                try (ResultSet rs = stmtSelect.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        conn.setAutoCommit(autoCommitOriginal);
                        return false;
                    }
                    saldoActual = rs.getDouble("saldo_pendiente");
                }
            }

            // 3) Calcular nuevo saldo
            double nuevoSaldo = saldoActual - pago.getMonto();
            if (nuevoSaldo < 0) {
                nuevoSaldo = 0;
            }

            String nuevoEstado = (nuevoSaldo == 0) ? "PAGADO" : "PENDIENTE";

            // 4) Actualizar préstamo en BD
            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdatePrestamo)) {
                stmtUpdate.setDouble(1, nuevoSaldo);
                stmtUpdate.setString(2, nuevoEstado);
                stmtUpdate.setInt(3, prestamoId);
                stmtUpdate.executeUpdate();
            }

            // 5) Reflejar en el objeto Prestamo en memoria
            prestamo.setSaldoPendiente(nuevoSaldo);
            prestamo.setEstado(nuevoEstado);

            conn.commit();
            conn.setAutoCommit(autoCommitOriginal);
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.err.println("Error al registrar pago: " + e.getMessage());
            return false;
        }
    }
}
