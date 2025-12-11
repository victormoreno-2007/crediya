package com.crediya.data.repositories;

import com.crediya.connection.Conexion;
import com.crediya.data.entities.PrestamoEntity;
import com.crediya.data.mapper.PrestamoMapper;
import com.crediya.models.Prestamo;
import com.crediya.repository.PrestamoRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoRepositoryImpl implements PrestamoRepository {


    @Override
    public void guardar(Prestamo prestamoModelo) {
        PrestamoEntity entity = PrestamoMapper.toEntity(prestamoModelo);

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

            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    prestamoModelo.setId(generatedKeys.getInt(1));
                    System.out.println("Préstamo registrado con ID: " + prestamoModelo.getId());
                }
            }

        } catch (SQLException e) {
            System.out.printf("Error al guardar el prestamo :( "+e.getMessage());
        }


    }

    @Override
    public List<Prestamo> listarTodos() {
        List<Prestamo> lista = new ArrayList<>();
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

                lista.add(PrestamoMapper.toModel(entity));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar préstamos: " + e.getMessage());
        }
        return lista;
    }
}
