package com.crediya.data.repositories;

import com.crediya.connection.Conexion;
import com.crediya.data.entities.ClienteEntity;
import com.crediya.data.mapper.ClienteMapper;
import com.crediya.domain.errors.DuplicateDocumentException;
import com.crediya.domain.errors.ErrorDomain;
import com.crediya.domain.errors.ErrorType;
import com.crediya.domain.models.Cliente;
import com.crediya.domain.repository.ClienteRepository;
import com.crediya.domain.response.ResponseDomain;
import com.crediya.util.GestorArchivos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepositoryImpl implements ClienteRepository {

    @Override
    public ResponseDomain<ErrorDomain, Integer> registrar(Cliente clienteModelo) {
        ClienteEntity entity = ClienteMapper.toEntity(clienteModelo);
        var exists = buscarPorDocumento(clienteModelo.getDocumento());

        if(!exists.hasError()) {
            return new ResponseDomain(new DuplicateDocumentException("El documento ya se encuentra asociado a otro usuario."));
        }

        String sql = "INSERT INTO clientes(nombre, documento, correo, telefono) VALUES (?, ?, ?, ?)";

        try (Connection cont = Conexion.getConexion();
             PreparedStatement pst = cont.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pst.setString(1, entity.getNombre());
            pst.setString(2, entity.getDocumento());
            pst.setString(3, entity.getCorreo());
            pst.setString(4, entity.getTelefono());

            var rows = pst.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idReal = generatedKeys.getInt(1);
                        clienteModelo.setId(idReal);
                        GestorArchivos archivo = new GestorArchivos();
                        String linea = idReal + "," + clienteModelo.getNombre() + "," + clienteModelo.getDocumento() + "," + clienteModelo.getCorreo() + "," + clienteModelo.getTelefono();
                        archivo.anexarRegistro("clientes.txt", linea);


                        return new ResponseDomain(idReal);
                    }
                }
            }

            return new ResponseDomain(rows);
        } catch (SQLException e) {
            System.out.printf("Error al guardar el cliente "+ e.getMessage());
            return new ResponseDomain(new ErrorDomain(ErrorType.DUPLICATE_ID_FIELD));
        }

    }

    @Override
    public ResponseDomain<ErrorDomain, List<Cliente>> listarTodos() {
        List<Cliente> listaNegocio = new ArrayList<>();
        String sql = "SELECT id, nombre, documento, correo, telefono FROM clientes";

        try (Connection cont = Conexion.getConexion();
            PreparedStatement pst = cont.prepareStatement(sql);
            ResultSet rst = pst.executeQuery()){

            while (rst.next()){
                ClienteEntity entity = new ClienteEntity();
                entity.setId(rst.getInt("id"));
                entity.setNombre(rst.getString("Nombre"));
                entity.setDocumento(rst.getString("documento"));
                entity.setCorreo(rst.getString("correo"));
                entity.setTelefono(rst.getString("telefono"));

                Cliente modelo = ClienteMapper.toModel(entity);

                listaNegocio.add(modelo);

            }

        } catch (SQLException e) {
            System.out.printf("Error al listar los clientes :( "+ e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
        return ResponseDomain.success(listaNegocio);
    }

    @Override
    public ResponseDomain<ErrorDomain, Cliente> buscarPorDocumento(String documento) {
        String sql = "SELECT id, nombre, documento, correo, telefono FROM clientes WHERE documento=?";
        Cliente clienteEncontrado = null;

        try (Connection cont = Conexion.getConexion();
            PreparedStatement pst = cont.prepareStatement(sql)) {

            pst.setString(1, documento);

            try (ResultSet rst = pst.executeQuery()){
                if (rst.next()){
                    ClienteEntity entity = new ClienteEntity();
                    entity.setId(rst.getInt("id"));
                    entity.setNombre(rst.getString("nombre"));
                    entity.setDocumento(rst.getString("documento"));
                    entity.setCorreo(rst.getString("correo"));
                    entity.setTelefono(rst.getString("telefono"));

                    clienteEncontrado = ClienteMapper.toModel(entity);
                }else {
                    return ResponseDomain.error(new ErrorDomain(ErrorType.RESOURCE_NOT_FOUND));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar el cliente "+ e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.RESOURCE_NOT_FOUND));
        }
        return ResponseDomain.success(clienteEncontrado);
    }

    @Override
    public ResponseDomain<ErrorDomain, Boolean> actualizar(Cliente cliente) {
        ClienteEntity entity = ClienteMapper.toEntity(cliente);
        String sql = "UPDATE clientes SET nombre=?, documento=?, correo=?, telefono=? WHERE id=?";

        try (Connection cont = Conexion.getConexion();
        PreparedStatement pst = cont.prepareStatement(sql)){

            pst.setString(1, entity.getNombre());
            pst.setString(2, entity.getDocumento());
            pst.setString(3, entity.getCorreo());
            pst.setString(4, entity.getTelefono());
            pst.setInt(5, entity.getId());

            var rows = pst.executeUpdate();

            if (rows > 0){
               return ResponseDomain.success(true);
            } else {
                return ResponseDomain.error(new ErrorDomain(ErrorType.CLIENT_NOT_FOUND));
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar el cliente :( "+ e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }

    }

    @Override
    public ResponseDomain<ErrorDomain, Boolean> eliminar(int id) {
        String sql = "DELETE FROM clientes WHERE id=?";

        try (Connection cont = Conexion.getConexion();
        PreparedStatement pst = cont.prepareStatement(sql)){

            pst.setInt(1, id);

            var rows = pst.executeUpdate();

            if (rows > 0){
                return  ResponseDomain.success(true);
            } else {
                return ResponseDomain.error(new ErrorDomain(ErrorType.CLIENT_NOT_FOUND));
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getMessage().contains("constraint")) {
                return ResponseDomain.error(new ErrorDomain(ErrorType.CANNOT_DELETE_HAS_DATA));
            }
            System.out.println("Error al eliminar el cliente :( "+ e.getMessage());
            return  ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));
        }
    }
}
