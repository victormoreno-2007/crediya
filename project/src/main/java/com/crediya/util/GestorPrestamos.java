package com.crediya.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.crediya.connection.Conexion;
import com.crediya.data.entities.PrestamoEntity;
import com.crediya.data.mapper.PrestamoMapper;
import com.crediya.domain.errors.ErrorDomain;
import com.crediya.domain.errors.ErrorType;
import com.crediya.domain.models.Cliente;
import com.crediya.domain.models.Empleado;
import com.crediya.domain.models.Prestamo;
import com.crediya.domain.response.ResponseDomain;


public class GestorPrestamos {

  public ResponseDomain<ErrorDomain, List<Prestamo>> filtrarPrestamosActivos() {
        List<Prestamo> listaNegocio = new ArrayList<>();
        String sql = "SELECT p.id, p.monto, p.interes, p.cuotas, p.fecha_inicio,p.fecha_vencimiento p.estado, p.saldo_pendiente, " +
                     "c.nombre AS nombre_cliente, e.nombre AS nombre_empleado " +
                     "FROM prestamos p " +
                     "INNER JOIN clientes c ON p.cliente_id = c.id " +
                     "INNER JOIN empleados e ON p.empleado_id = e.id" +
                     "WHERE p.estado = 'ACTIVO' ";

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
                entity.setFechaVencimiento(null);
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

    public ResponseDomain<ErrorDomain, List<Prestamo>> filtrarPrestamosVencidos() {
        List<Prestamo> listaNegocio = new ArrayList<>();
        String sql = "SELECT p.id, p.monto, p.interes, p.cuotas, p.fecha_inicio,p.fecha_vencimiento p.estado, p.saldo_pendiente, " +
                     "c.nombre AS nombre_cliente, e.nombre AS nombre_empleado " +
                     "FROM prestamos p " +
                     "INNER JOIN clientes c ON p.cliente_id = c.id " +
                     "INNER JOIN empleados e ON p.empleado_id = e.id" +
                     "WHERE p.fecha_vencimiento < LocalDate.now";

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
                entity.setFechaVencimiento(null);
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

    public ResponseDomain<ErrorDomain, List<Prestamo>> filtrarPrestamos() {
        List<Prestamo> listaNegocio = new ArrayList<>();
        String sql = "SELECT p.id, p.monto, p.interes, p.cuotas, p.fecha_inicio,p.fecha_vencimiento p.estado, p.saldo_pendiente, " +
                     "c.nombre AS nombre_cliente, e.nombre AS nombre_empleado " +
                     "FROM prestamos p " +
                     "INNER JOIN clientes c ON p.cliente_id = c.id " +
                     "INNER JOIN empleados e ON p.empleado_id = e.id" ;

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
                entity.setFechaVencimiento(null);
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
  





}
