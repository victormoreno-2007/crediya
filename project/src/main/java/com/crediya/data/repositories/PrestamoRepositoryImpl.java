package com.crediya.data.repositories;

import com.crediya.connection.Conexion;
import com.crediya.data.entities.PrestamoEntity;
import com.crediya.data.mapper.PrestamoMapper;
import com.crediya.domain.errors.ErrorDomain;
import com.crediya.domain.errors.ErrorType;
import com.crediya.domain.models.Cliente;
import com.crediya.domain.models.Empleado;
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
        entity.setSaldoPendiente(prestamoModelo.getMontoTotal());

        String sql = "INSERT INTO prestamos (monto, interes, cuotas, fecha_inicio, estado, saldo_pendiente, cliente_id, empleado_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cont = Conexion.getConexion();
             PreparedStatement pst = cont.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            pst.setDouble(1, entity.getMonto());
            pst.setDouble(2, entity.getInteres());
            pst.setInt(3, entity.getCuotas());
            pst.setDate(4, java.sql.Date.valueOf(entity.getFechaInicio()));// se convierte LocalDate (Java) a Date (SQL)
            pst.setString(5, entity.getEstado());
            pst.setDouble(6, entity.getSaldoPendiente());
            pst.setInt(7, entity.getCliente().getId());// se inserta las claves foráneas (IDs)
            pst.setInt(8, entity.getEmpleado().getId());

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
        String sql = "SELECT p.id, p.monto, p.interes, p.cuotas, p.fecha_inicio, p.estado, p.saldo_pendiente, " +
                     "c.nombre AS nombre_cliente, e.nombre AS nombre_empleado " +
                     "FROM prestamos p " +
                     "INNER JOIN clientes c ON p.cliente_id = c.id " +
                     "INNER JOIN empleados e ON p.empleado_id = e.id";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Cliente cliente = new Cliente();
                cliente.setNombre(rs.getString("nombre_cliente"));

                Empleado empleado = new Empleado();
                empleado.setNombre(rs.getString("nombre_empleado"));


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
                entity.setCliente(cliente);
                entity.setEmpleado(empleado);

                listaNegocio.add(PrestamoMapper.toModel(entity));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar préstamos: " + e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
        return ResponseDomain.success(listaNegocio);
    }

    @Override
    public ResponseDomain<ErrorDomain, Boolean> actualizar(Prestamo prestamo) {
        PrestamoEntity entity = PrestamoMapper.toEntity(prestamo);
        String sql = "UPDATE prestamos SET monto=?, interes=?, cuotas=?, fecha_inicio=?, estado=?, saldo_pendiente=? WHERE id=?";

        try (Connection cont = Conexion.getConexion();
        PreparedStatement pst = cont.prepareStatement(sql)){

            pst.setDouble(1, entity.getMonto());
            pst.setDouble(2, entity.getInteres());
            pst.setInt(3, entity.getCuotas());
            pst.setDate(4, java.sql.Date.valueOf(entity.getFechaInicio()));
            pst.setString(5, entity.getEstado());

            pst.setDouble(6, entity.getSaldoPendiente());

            pst.setInt(7, entity.getId());

            var rows = pst.executeUpdate();

            if (rows > 0){
                return  ResponseDomain.success(true);
            } else {
                return ResponseDomain.error( new ErrorDomain(ErrorType.RECORD_NOT_UPDATED));
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar el prestamo :( "+ e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
    }

    @Override
    public ResponseDomain<ErrorDomain, Boolean> eliminar(int id) {
        String sql = "DELETE FROM prestamos WHERE  id=?";

        try (Connection cont = Conexion.getConexion();
        PreparedStatement pst = cont.prepareStatement(sql)){

            pst.setInt(1, id);

            var rows = pst.executeUpdate();

            if (rows > 0 ){
                return ResponseDomain.success(true);
            } else {
                return ResponseDomain.error(new ErrorDomain(ErrorType.RECORD_NOT_DELETED));
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().contains("constraint")) {
                return ResponseDomain.error(new ErrorDomain(ErrorType.CANNOT_DELETE_HAS_DATA));
            }
            System.out.println("Error al eliminar el prestamo :( "+ e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
    }
    @Override
    public ResponseDomain<ErrorDomain, Prestamo> buscarPorId(int id) {
        String sql = "SELECT p.id, p.monto, p.interes, p.cuotas, p.fecha_inicio, p.estado, p.saldo_pendiente, " +
                "c.id AS cli_id, c.nombre AS cli_nombre, " +
                "e.id AS emp_id, e.nombre AS emp_nombre " +
                "FROM prestamos p " +
                "INNER JOIN clientes c ON p.cliente_id = c.id " +
                "INNER JOIN empleados e ON p.empleado_id = e.id " +
                "WHERE p.id = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("cli_id"));
                    cliente.setNombre(rs.getString("cli_nombre"));

                    Empleado empleado = new Empleado();
                    empleado.setId(rs.getInt("emp_id"));
                    empleado.setNombre(rs.getString("emp_nombre"));

                    Prestamo prestamo = new Prestamo();
                    prestamo.setId(rs.getInt("id"));
                    prestamo.setMonto(rs.getDouble("monto"));
                    prestamo.setInteres(rs.getDouble("interes"));
                    prestamo.setCuotas(rs.getInt("cuotas"));
                    if (rs.getDate("fecha_inicio") != null) {
                        prestamo.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                    }
                    prestamo.setEstado(rs.getString("estado"));
                    prestamo.setSaldoPendiente(rs.getDouble("saldo_pendiente"));
                    prestamo.setCliente(cliente);
                    prestamo.setEmpleado(empleado);

                    return ResponseDomain.success(prestamo);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar préstamo: " + e.getMessage());
        }
        return ResponseDomain.error(new ErrorDomain(ErrorType.LOAN_NOT_FOUND)); // Asegúrate de tener este error o usa uno genérico
    }

    @Override
    public ResponseDomain<ErrorDomain, List<Prestamo>> buscarPorCliente(int idCliente) {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.monto, p.saldo_pendiente, p.estado, p.cuotas " +
                "FROM prestamos p WHERE p.cliente_id = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prestamo p = new Prestamo();
                    p.setId(rs.getInt("id"));
                    p.setMonto(rs.getDouble("monto"));
                    p.setSaldoPendiente(rs.getDouble("saldo_pendiente"));
                    p.setEstado(rs.getString("estado"));
                    p.setCuotas(rs.getInt("cuotas"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
        return ResponseDomain.success(lista);
    }
}
