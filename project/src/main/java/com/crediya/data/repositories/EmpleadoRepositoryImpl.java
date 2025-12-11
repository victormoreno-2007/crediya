package com.crediya.data.repositories;

import com.crediya.connection.Conexion;
import com.crediya.data.entities.EmpleadoEntity;
import com.crediya.data.mapper.EmpleadoMapper;
import com.crediya.domain.errors.DuplicateDocumentException;
import com.crediya.domain.errors.ErrorDomain;
import com.crediya.domain.errors.ErrorType;
import com.crediya.domain.models.Empleado;
import com.crediya.domain.repository.EmpleadoRepository;
import com.crediya.domain.response.ResponseDomain;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoRepositoryImpl implements EmpleadoRepository {
    @Override
    public ResponseDomain<ErrorDomain, Integer> guardar(Empleado empleadoModelo) {

        if (empleadoModelo.getSalario() < 0) {
            return ResponseDomain.error(new ErrorDomain(ErrorType.NO_NEGATIVE_WAGES));
        }

        ResponseDomain<ErrorDomain, Empleado> exist = buscarPorDocumento(empleadoModelo.getDocumento());
        if (!exist.hasError()) {
            return ResponseDomain.error(new ErrorDomain(ErrorType.DUPLICATE_PRIMARY_KEY));
        }

        String sql = "INSERT INTO empleados(nombre, documento, rol, correo, salario) VALUES(?,?,?,?,?)";

        try (Connection cont = Conexion.getConexion();
             PreparedStatement ps = cont.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, empleadoModelo.getNombre());
            ps.setString(2, empleadoModelo.getDocumento());
            ps.setString(3, empleadoModelo.getRol());
            ps.setString(4, empleadoModelo.getCorreo());
            ps.setDouble(5, empleadoModelo.getSalario());

            var rows = ps.executeUpdate();

            if (rows > 0){
                try(ResultSet rst = ps.getGeneratedKeys()){
                    if (rst.next()){
                        int idGenerado = rst.getInt(1);
                        return ResponseDomain.success(idGenerado);
                    }
                }
            }

            return ResponseDomain.error(new ErrorDomain(ErrorType.DUPLICATE_ID_FIELD));

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate")){
                return ResponseDomain.error(new ErrorDomain(ErrorType.DUPLICATE_ID_FIELD));
            }

            System.out.printf("Error en la base de datos: "+ e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DUPLICATE_ID_FIELD));
        }
    }

    @Override
    public ResponseDomain<ErrorDomain, List<Empleado>> listarTodos() {
        List<Empleado> listaNegocio = new ArrayList<>();
        String sql = "SELECT nombre, documento, rol, correo, salario FROM empleados";

        try (Connection cont = Conexion.getConexion();
             PreparedStatement pst = cont.prepareStatement(sql);
             ResultSet rst = pst.executeQuery()){

            while (rst.next()) {
                EmpleadoEntity entity = new EmpleadoEntity();
                entity.setNombre(rst.getString("nombre"));
                entity.setDocumento(rst.getString("documento"));
                entity.setCorreo(rst.getString("correo"));
                entity.setRol(rst.getString("rol"));
                entity.setSalario(rst.getDouble("salario"));

                listaNegocio.add(EmpleadoMapper.toModel(entity));
            }

        } catch (SQLException e) {
            System.out.println("tuvistes un eror al listar los empleados "+ e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
        return ResponseDomain.success(listaNegocio);
    }

    @Override
    public ResponseDomain<ErrorDomain, Empleado> buscarPorDocumento(String documento) {
        String sql = "SELECT nombre, documento, rol, correo, salario FROM empleados WHERE documento=?";
        Empleado empleadoEncontrado = null;

        try(Connection cont = Conexion.getConexion();
            PreparedStatement pst = cont.prepareStatement(sql)){

            pst.setString(1, documento);

            try(ResultSet rs = pst.executeQuery()){
                if (rs.next()) {
                    EmpleadoEntity entity = new EmpleadoEntity();
                    entity.setNombre(rs.getString("nombre"));
                    entity.setDocumento(rs.getString("documento"));
                    entity.setRol(rs.getString("rol"));
                    entity.setCorreo(rs.getString("correo"));
                    entity.setSalario(rs.getDouble("salario"));

                    empleadoEncontrado = EmpleadoMapper.toModel(entity);
                } else {
                    return ResponseDomain.error(new ErrorDomain(ErrorType.RESOURCE_NOT_FOUND));
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error al buscar el empleado "+ ex.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.RESOURCE_NOT_FOUND));
        }
        return ResponseDomain.success(empleadoEncontrado);
    }
}
