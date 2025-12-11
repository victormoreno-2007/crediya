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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepositoryImpl implements ClienteRepository {

    @Override
    public ResponseDomain<ErrorDomain, Integer> registrar(Cliente clienteModelo) {
        ClienteEntity entity = ClienteMapper.toEntity(clienteModelo);
        var exists = buscarPorDocumento(clienteModelo.getDocumento());
        if(exists != null) {
            return new ResponseDomain(new DuplicateDocumentException("The document field is asociado a other user xd"));
        }

        String sql = "INSERT INTO clientes (nombre, documento, correo, telefono) VALUES (?, ?, ?, ?)";

        try (Connection cont = Conexion.getConexion();
             PreparedStatement pst = cont.prepareStatement(sql)){
            pst.setString(1, entity.getNombre());
            pst.setString(2, entity.getDocumento());
            pst.setString(3, entity.getCorreo());
            pst.setString(4, entity.getTelefono());

            var rows = pst.executeUpdate();

            return new ResponseDomain(rows);
        } catch (SQLException e) {
            System.out.printf("Error al guardar el cliente "+ e.getMessage());
            return new ResponseDomain(new ErrorDomain(ErrorType.DUPLICATE_ID_FIELD));
        }

    }

    @Override
    public ResponseDomain<ErrorDomain, List<Cliente>> listarTodos() {
        List<Cliente> listaNegocio = new ArrayList<>();
        String sql = "SELECT nombre, documento, correo, telefono FROM clientes";

        try (Connection cont = Conexion.getConexion();
        PreparedStatement pst = cont.prepareStatement(sql);
             ResultSet rst = pst.executeQuery()){

            while (rst.next()){
                ClienteEntity entity = new ClienteEntity();
                entity.setNombre(rst.getString("Nombre"));
                entity.setDocumento(rst.getString("documento"));
                entity.setCorreo(rst.getString("correo"));
                entity.setTelefono(rst.getString("telefono"));

                Cliente modelo = ClienteMapper.toModel(entity);

                listaNegocio.add(modelo);

            }

        } catch (SQLException e) {
            System.out.printf("Error al listar los clientes :( "+ e.getMessage());
            return ResponseDomain.error(new ErrorDomain(ErrorType.DUPLICATE_PRIMARY_KEY));
        }
        return ResponseDomain.success(listaNegocio);
    }

    @Override
    public Cliente buscarPorDocumento(String documento) {
        String sql = "SELECT nombre, documento, correo, telefono FROM clientes WHERE documento=?";
        Cliente clienteEncontrado = null;

        try (Connection cont = Conexion.getConexion();
        PreparedStatement pst = cont.prepareStatement(sql)) {

            pst.setString(1, documento);

            try (ResultSet rst = pst.executeQuery()){

                if (rst.next()){
                    ClienteEntity entity = new ClienteEntity();
                    entity.setNombre(rst.getString("nombre"));
                    entity.setDocumento(rst.getString("documento"));
                    entity.setCorreo(rst.getString("correo"));
                    entity.setTelefono(rst.getString("telefono"));

                    clienteEncontrado = ClienteMapper.toModel(entity);
                }
            }
        } catch (SQLException e) {
            System.out.printf("Error al buscar el cliente :( "+ e.getMessage());
        }
    return clienteEncontrado;
    }
}
