package com.crediya.data.repositories;

import com.crediya.connection.Conexion;
import com.crediya.domain.errors.ErrorDomain;
import com.crediya.domain.errors.ErrorType;
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

        String sqlConsultarPrestamo = "SELECT saldo_pendiente, estado FROM prestamos WHERE id = ?";
        String sqlInsertarPago = "INSERT INTO pagos (prestamo_id, fecha_pago, monto) VALUES (?, ?, ?)";
        String sqlActualizarPrestamo = "UPDATE prestamos SET saldo_pendiente = ?, estado = ? WHERE id = ?";

        try (Connection cont = Conexion.getConexion()){

            cont.setAutoCommit(false);

            try{
                double saldoActual = 0;
                String estadoActual = "";

                try (PreparedStatement pstPrestamo = cont.prepareStatement(sqlConsultarPrestamo)) {
                    pstPrestamo.setInt(1, pago.getPrestamo().getId());
                    try (ResultSet rst = pstPrestamo.executeQuery()) {
                        if (rst.next()) {
                            saldoActual = rst.getDouble("saldo_pendiente");
                            estadoActual = rst.getString("estado");
                        } else {
                            return ResponseDomain.error(new ErrorDomain(ErrorType.LOAN_NOT_FOUND));
                        }
                    }
                }

                if ("PAGADO".equals(estadoActual) || saldoActual <=0) {
                    return ResponseDomain.error(new ErrorDomain(ErrorType.LOAD_ALREADY_PAID));
                }

                if (pago.getMonto() > saldoActual){
                    return  ResponseDomain.error(new ErrorDomain(ErrorType.PAYMENT_EXCEEDS_DEBT));
                }

                int idPagoGenerado = 0;
                try (PreparedStatement psPago = cont.prepareStatement(sqlInsertarPago, Statement.RETURN_GENERATED_KEYS)) {
                    psPago.setInt(1, pago.getPrestamo().getId());
                    psPago.setDate(2, Date.valueOf(pago.getFechaPago()));
                    psPago.setDouble(3, pago.getMonto());
                    psPago.executeUpdate();

                    try (ResultSet rs = psPago.getGeneratedKeys()) {
                        if (rs.next()) idPagoGenerado = rs.getInt(1);
                    }
                }
                double nuevoSaldo = saldoActual - pago.getMonto();
                String nuevoEstado = (nuevoSaldo == 0) ? "PAGADO" : "PENDIENTE";

                try (PreparedStatement pstUpdate = cont.prepareStatement(sqlActualizarPrestamo)){
                    pstUpdate.setDouble(1, nuevoSaldo);
                    pstUpdate.setString(2, nuevoEstado);
                    pstUpdate.setInt(3, pago.getPrestamo().getId());

                    pstUpdate.executeUpdate();
                }

                cont.commit();
                return ResponseDomain.success(idPagoGenerado);

            } catch (SQLException e) {
                cont.rollback();
                System.out.println("Error en la transaccion, rollback se ejecutará -> "+ e.getMessage());
                return ResponseDomain.error(new ErrorDomain(ErrorType.TRANSACTION_ERROR));
            }

        } catch (SQLException e) {
            return  ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
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
                pago.setFechaPago(rst.getDate("fecha_pago").toLocalDate());
                pago.setPrestamo(prestamoRef);

                listaNegocioPago.add(pago);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar los pagos: "+ e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
        return ResponseDomain.success(listaNegocioPago);
    }
}
