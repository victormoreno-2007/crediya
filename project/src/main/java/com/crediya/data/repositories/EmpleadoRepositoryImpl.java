package com.crediya.data.repositories;

import com.crediya.connection.Conexion;
import com.crediya.data.entities.EmpleadoEntity;
import com.crediya.data.mapper.EmpleadoMapper;
import com.crediya.domain.errors.ErrorDomain;
import com.crediya.domain.errors.ErrorType;
import com.crediya.domain.models.Empleado;
import com.crediya.domain.repository.EmpleadoRepository;
import com.crediya.domain.response.ResponseDomain;
import com.crediya.util.GestorArchivos;

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

                        GestorArchivos archivo = new GestorArchivos();
                        String linea = idGenerado + "," + empleadoModelo.getNombre() + "," +
                                empleadoModelo.getDocumento() + "," + empleadoModelo.getRol() + "," +
                                empleadoModelo.getSalario();
                        archivo.anexarRegistro("empleados.txt", linea);


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
        String sql = "SELECT id, nombre, documento, rol, correo, salario FROM empleados";

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

                Empleado modelo = EmpleadoMapper.toModel(entity);

                listaNegocio.add(modelo);
            }

        } catch (SQLException e) {
            System.out.println("tuvistes un eror al listar los empleados "+ e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
        return ResponseDomain.success(listaNegocio);
    }

    @Override
    public ResponseDomain<ErrorDomain, Empleado> buscarPorDocumento(String documento) {
        String sql = "SELECT id, nombre, documento, rol, correo, salario FROM empleados WHERE documento=?";
        Empleado empleadoEncontrado = null;

        try(Connection cont = Conexion.getConexion();
            PreparedStatement pst = cont.prepareStatement(sql)){

            pst.setString(1, documento);

            try(ResultSet rst = pst.executeQuery()){
                if (rst.next()) {
                    EmpleadoEntity entity = new EmpleadoEntity();
                    entity.setId(rst.getInt("id"));
                    entity.setNombre(rst.getString("nombre"));
                    entity.setDocumento(rst.getString("documento"));
                    entity.setRol(rst.getString("rol"));
                    entity.setCorreo(rst.getString("correo"));
                    entity.setSalario(rst.getDouble("salario"));

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

    @Override
    public ResponseDomain<ErrorDomain, Boolean> actualizar(Empleado empleado) {
        EmpleadoEntity entity = EmpleadoMapper.toEntity(empleado);
        String sql = "UPDATE empleados SET nombre=?, documento=?, correo=?, rol=?, salario=? WHERE id=?";

        try (Connection cont = Conexion.getConexion();
        PreparedStatement pst = cont.prepareStatement(sql)){

            pst.setString(1, entity.getNombre());
            pst.setString(2, entity.getDocumento());
            pst.setString(3, entity.getCorreo());
            pst.setString(4, empleado.getRol());
            pst.setDouble(5,entity.getSalario());
            pst.setInt(6, entity.getId());

            var rows = pst.executeUpdate();

            if (rows > 0) {
                return ResponseDomain.success(true);
            } else {
                 return ResponseDomain.error(new ErrorDomain(ErrorType.EMPLOYEE_NOT_FOUND));
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar el empleado :( "+ e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
    }

    @Override
    public ResponseDomain<ErrorDomain, Boolean> eliminar(int id) {
        String sql = "DELETE FROM empleados WHERE id=?";

        try (Connection cont = Conexion.getConexion();
        PreparedStatement pst = cont.prepareStatement(sql)){

            pst.setInt(1, id);

            var rows = pst.executeUpdate();

            if (rows > 0){
                return ResponseDomain.success(true);
            } else {
                return ResponseDomain.error(new ErrorDomain(ErrorType.EMPLOYEE_NOT_FOUND));
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().contains("constraint")) {
                return ResponseDomain.error(new ErrorDomain(ErrorType.CANNOT_DELETE_HAS_DATA));
            }
            System.out.println("Error al eliminar el empleado :( "+ e.getMessage());
            return  ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
    }
}
