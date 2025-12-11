package com.crediya.data.repositories;

import com.crediya.connection.Conexion;
import com.crediya.data.entities.PrestamoEntity;
import com.crediya.data.mapper.PrestamoMapper;
import com.crediya.domain.errors.ErrorDomain;
import com.crediya.domain.errors.ErrorType;
import com.crediya.domain.models.Prestamo;
import com.crediya.domain.repository.PrestamoRepository;
import com.crediya.domain.response.ResponseDomain;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoRepositoryImpl implements PrestamoRepository {


    @Override
    public ResponseDomain<ErrorDomain, Integer> guardar(Prestamo prestamoModelo) {
        PrestamoEntity entity = PrestamoMapper.toEntity(prestamoModelo);

        var existMonto = prestamoModelo.getMonto();
        if (existMonto <= 0 ){
            return ResponseDomain.error(new ErrorDomain(ErrorType.INVALID_AMOUNT));
        }

        var existCuotas = prestamoModelo.getCuotas();
        if (existCuotas <= 0 ){
            return ResponseDomain.error(new ErrorDomain(ErrorType.INVALID_INSTALLMENTS));
        }


        entity.setFechaInicio(LocalDate.now()); // Fecha de hoy
        entity.setEstado("PENDIENTE");          // Estado inicial
        entity.setSaldoPendiente(entity.getMonto()); // sed debe lo mismo que prestó

        String sql = "INSERT INTO prestamos (monto, interes, cuotas, fecha_inicio, estado, saldo_pendiente, cliente_id, empleado_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cont = Conexion.getConexion();
             PreparedStatement pst = cont.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            pst.setDouble(1, entity.getMonto());
            pst.setDouble(2, entity.getInteres());
            pst.setInt(3, entity.getCuotas());
            pst.setDate(4, java.sql.Date.valueOf(entity.getFechaInicio()));// se convierte LocalDate (Java) a Date (SQL)
            pst.setString(5, entity.getEstado());
            pst.setDouble(6, entity.getSaldoPendiente());
            pst.setInt(7, entity.getClienteId());// se inserta las claves foráneas (IDs)
            pst.setInt(8, entity.getEmpleadoId());

            var rows = pst.executeUpdate();

            if (rows > 0) {
                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return ResponseDomain.success(generatedKeys.getInt(1));
                    }
                }
            }
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));

        } catch (SQLException e) {
            String mensajeError = e.getMessage();
            if (mensajeError.contains("foreign key")) {
                if (mensajeError.contains("cliente_id")) {
                    return ResponseDomain.error(new ErrorDomain(ErrorType.CLIENT_NOT_FOUND));
                }
                if (mensajeError.contains("empleado_id")) {
                    return ResponseDomain.error(new ErrorDomain(ErrorType.EMPLOYEE_NOT_FOUND));

                }
            }

            System.out.println("Error SQL crítico: " + mensajeError);
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }


    }

    @Override
    public ResponseDomain<ErrorDomain, List<Prestamo>> listarTodosPrestamos() {
        List<Prestamo> listaNegocio = new ArrayList<>();
        String sql = "SELECT id, monto, interes, cuotas, fecha_inicio, estado, saldo_pendiente, cliente_id, empleado_id FROM prestamos";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PrestamoEntity entity = new PrestamoEntity();

                entity.setId(rs.getInt("id"));
                entity.setMonto(rs.getDouble("monto"));
                entity.setInteres(rs.getDouble("interes"));
                entity.setCuotas(rs.getInt("cuotas"));
                if (rs.getDate("fecha_inicio") != null) {
                    entity.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                }
                entity.setEstado(rs.getString("estado"));
                entity.setSaldoPendiente(rs.getDouble("saldo_pendiente"));
                entity.setClienteId(rs.getInt("cliente_id"));
                entity.setEmpleadoId(rs.getInt("empleado_id"));

                listaNegocio.add(PrestamoMapper.toModel(entity));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar préstamos: " + e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
        return ResponseDomain.success(listaNegocio);
    }
}
