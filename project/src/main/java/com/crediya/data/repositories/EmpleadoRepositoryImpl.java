package com.crediya.data.repositories;

import com.crediya.connection.Conexion;
import com.crediya.data.entities.EmpleadoEntity;
import com.crediya.data.mapper.EmpleadoMapper;
import com.crediya.models.Empleado;
import com.crediya.repository.EmpleadoRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoRepositoryImpl implements EmpleadoRepository {
    @Override
    public void guardar(Empleado empleadoModelo) {
        EmpleadoEntity entity = EmpleadoMapper.toEntity(empleadoModelo);
        String sql = "INSERT INTO empleados(nombre, documento, rol, correo, salario) VALUES(?,?,?,?,?)";

        try (Connection cont = Conexion.getConexion();
             PreparedStatement ps = cont.prepareStatement(sql)){

            ps.setString(1, empleadoModelo.getNombre());
            ps.setString(2, empleadoModelo.getDocumento());
            ps.setString(3, empleadoModelo.getRol());
            ps.setString(4, empleadoModelo.getCorreo());
            ps.setDouble(5, empleadoModelo.getSalario());

            ps.executeUpdate();

            System.out.println("Empleado registrado exitosamente");

        } catch (SQLException e) {
            System.out.println("error al guardar el empleado  "+ e.getMessage());
        }
    }

    @Override
    public List<Empleado> listarTodos() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT nombre, documento, rol, correo, salario FROM empleados";

        try (Connection cont = Conexion.getConexion();
             PreparedStatement pst = cont.prepareStatement(sql);
             ResultSet rst = pst.executeQuery()){

            while (rst.next()) {
                EmpleadoEntity entity = new EmpleadoEntity();
                entity.setId(rst.getInt("id"));
                entity.setNombre(rst.getString("nombre"));
                entity.setDocumento(rst.getString("documento"));
                entity.setCorreo(rst.getString("correo"));
                entity.setRol(rst.getString("rol"));
                entity.setSalario(rst.getDouble("salario"));

                lista.add(EmpleadoMapper.toModel(entity));
            }

        } catch (SQLException e) {
            System.out.println("tuvistes un eror al listar los empleados "+ e.getMessage());
        }
        return lista;
    }

    @Override
    public Empleado buscarPorDocumento(String documento) {
        String sql = "SELECT nombre, documento, rol, correo, salario FROM empleados WHERE documento=?";
        Empleado empleadoEncontrado = null;

        try(Connection cont = Conexion.getConexion();
            PreparedStatement pst = cont.prepareStatement(sql)){

            pst.setString(1, documento);

            try(ResultSet rs = pst.executeQuery()){
                if (rs.next()) {
                    EmpleadoEntity entity = new EmpleadoEntity();
                    entity.setId(rs.getInt("id"));
                    entity.setNombre(rs.getString("nombre"));
                    entity.setDocumento(rs.getString("documento"));
                    entity.setRol(rs.getString("rol"));
                    entity.setCorreo(rs.getString("correo"));
                    entity.setSalario(rs.getDouble("salario"));

                    empleadoEncontrado = EmpleadoMapper.toModel(entity);
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error al buscar el empleado "+ ex.getMessage());
        }
        return empleadoEncontrado;
    }
}
