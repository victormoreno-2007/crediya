package com.crediya.data.repositories;

import com.crediya.connection.Conexion;
import com.crediya.data.entities.PagoEntity;
import com.crediya.data.mapper.PagoMapper;
import com.crediya.domain.errors.ErrorDomain;
import com.crediya.domain.errors.ErrorType;
import com.crediya.domain.models.Cliente;
import com.crediya.domain.models.Pago;
import com.crediya.domain.models.Prestamo;
import com.crediya.domain.repository.PagoRepository;
import com.crediya.domain.response.ResponseDomain;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoRepositoryImpl implements PagoRepository {

    @Override
    public ResponseDomain<ErrorDomain, Integer> guardar(Pago pago) {
        if (pago.getMonto() < 0){
            return ResponseDomain.error(new ErrorDomain(ErrorType.INVALID_AMOUNT));
        }

        String sqlInsertarPago = "INSERT INTO pagos (prestamo_id, fecha_pago, monto) VALUES (?, ?, ?)";

        try (Connection cont = Conexion.getConexion();
             PreparedStatement psPago = cont.prepareStatement(sqlInsertarPago, Statement.RETURN_GENERATED_KEYS)) {

            psPago.setInt(1, pago.getPrestamo().getId());
            psPago.setDate(2, java.sql.Date.valueOf(pago.getFechaPago()));
            psPago.setDouble(3, pago.getMonto());

            int rows = psPago.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = psPago.getGeneratedKeys()) {
                    if (rs.next()) {
                        return ResponseDomain.success(rs.getInt(1));
                    }
                }
            }
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));

        } catch (SQLException e) {
            System.out.println("Error al guardar el pago: " + e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
    }

    @Override
    public ResponseDomain<ErrorDomain, List<Pago>> listarPagos() {
        List<Pago> listaNegocioPago = new ArrayList<>();
        String sql = "SELECT p.id, p.fecha_pago, p.monto, p.prestamo_id, pr.saldo_pendiente " +
                "FROM pagos p " +
                "INNER JOIN prestamos pr ON p.prestamo_id = pr.id";

        try (Connection cont = Conexion.getConexion();
             PreparedStatement psmt = cont.prepareStatement(sql);
             ResultSet rst = psmt.executeQuery()){

            while (rst.next()){
                Prestamo prestamoRef = new Prestamo();
                prestamoRef.setId(rst.getInt("prestamo_id"));
                prestamoRef.setSaldoPendiente(rst.getDouble("saldo_pendiente"));

                Pago pago = new Pago();
                pago.setId(rst.getInt("id"));
                pago.setMonto(rst.getDouble("monto"));
                if (rst.getDate("fecha_pago") != null) {
                    pago.setFechaPago(rst.getDate("fecha_pago").toLocalDate());
                }
                pago.setPrestamo(prestamoRef);

                listaNegocioPago.add(pago);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar los pagos: "+ e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
        return ResponseDomain.success(listaNegocioPago);
    }

    @Override
    public ResponseDomain<ErrorDomain, Boolean> actualizar(Pago pago) {
        PagoEntity entity = PagoMapper.toEntity(pago);
        String sql = "UPDATE pagos SET prestamo_id=?, fecha_pago=?, monto=? WHERE id=?";

        try (Connection cont = Conexion.getConexion();
             PreparedStatement pst = cont.prepareStatement(sql)) {

            pst.setInt(1, entity.getPrestamo().getId());
            pst.setDate(2, java.sql.Date.valueOf(entity.getFechaPago()));
            pst.setDouble(3, entity.getMonto());
            pst.setInt(4, entity.getId());

            var rows = pst.executeUpdate();

            if (rows > 0 ){
                return ResponseDomain.success(true);
            } else {
                return ResponseDomain.error(new ErrorDomain(ErrorType.RECORD_NOT_UPDATED));
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar el pago :( "+e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
    }

    @Override
    public ResponseDomain<ErrorDomain, Boolean> eliminar(int id) {
        String sql = "DELETE FROM pagos WHERE id=?";

        try (Connection cont = Conexion.getConexion();
             PreparedStatement pst = cont.prepareStatement(sql)) {

            pst.setInt(1, id);

            var rows = pst.executeUpdate();

            if (rows > 0){
                return ResponseDomain.success(true);
            } else {
                return ResponseDomain.error(new ErrorDomain(ErrorType.RECORD_NOT_DELETED));
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().contains("constraint")) {
                return ResponseDomain.error(new ErrorDomain(ErrorType.CANNOT_DELETE_HAS_DATA));
            }
            System.out.println("Error al eliminar el pago :(" + e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
    }

    @Override
    public ResponseDomain<ErrorDomain, Pago> buscarPorId(int id) {
        String sql = "SELECT pg.id, pg.monto, pg.fecha_pago, " +
                "pr.id AS prestamo_id, pr.saldo_pendiente, " +
                "c.nombre AS nombre_cliente " +
                "FROM pagos pg " +
                "INNER JOIN prestamos pr ON pg.prestamo_id = pr.id " +
                "INNER JOIN clientes c ON pr.cliente_id = c.id " +
                "WHERE pg.id = ?";

        try (Connection cont = Conexion.getConexion();
             PreparedStatement pst = cont.prepareStatement(sql)){

            pst.setInt(1, id);

            try (ResultSet rst = pst.executeQuery()){
                if (rst.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setNombre(rst.getString("nombre_cliente"));

                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(rst.getInt("prestamo_id"));
                    prestamo.setSaldoPendiente(rst.getDouble("saldo_pendiente"));
                    prestamo.setCliente(cliente);

                    Pago pago = new Pago();
                    pago.setId(rst.getInt("id"));
                    pago.setMonto(rst.getDouble("monto"));
                    if (rst.getDate("fecha_pago") != null) {
                        pago.setFechaPago(rst.getDate("fecha_pago").toLocalDate());
                    }
                    pago.setPrestamo(prestamo);

                    return ResponseDomain.success(pago);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar pago: " + e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
        return ResponseDomain.error(new ErrorDomain(ErrorType.PAYMENT_NOT_FOUND));
    }

    @Override
    public double sumarPagosPorPrestamo(int idPrestamo) {
        String sql = "SELECT COALESCE(SUM(monto), 0) FROM pagos WHERE prestamo_id = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPrestamo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error sumando pagos: " + e.getMessage());
        }
        return 0.0;
    }
}