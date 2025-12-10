package com.crediya.persistence.impl;

import com.crediya.connection.Conexion;
import com.crediya.models.Clientes;
import com.crediya.persistence.ClienteRepositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements ClienteRepositorio {
    @Override
    public void registrar(Clientes cliente) {
        String sql = "INSERT INTO clientes(nombre, documento, correo, telefono) VALUES(?,?,?,?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDocumento());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTelefono());

            ps.executeUpdate();

            System.out.println("Cliente registrado exitosamente");

        } catch (SQLException e) {
            System.out.println("ocurrio un error al registrar el cliente "+ e.getMessage());
        }
    }

    @Override
    public List<Clientes> listarTodos() {
        List<Clientes> lista = new ArrayList<>();
        String sql = "SELECT nombre, documento, correo, telefono FROM clientes";

        try(Connection con =  Conexion.getConexion();
        PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                Clientes ct = new Clientes(
                        rs.getString("nombre"),
                        rs.getString("documento"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                );
                lista.add(ct);
            }
        }
        catch (SQLException e) {
            System.out.println("osurrio un error al listar los clientes "+ e.getMessage());
        }
        return  lista;
    }

    @Override
    public Clientes buscarPorDocumento(String documento) {
        String sql = "SELECT nombre, documento, correo, telefono FROM clientes WHERE documento=?";
        Clientes ct = null;

        try(Connection con = Conexion.getConexion();
        PreparedStatement ps = con.prepareStatement(sql) ){

            ps.setString(1, documento);

            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    ct = new Clientes(
                            rs.getString("nombre"),
                            rs.getString("documento"),
                            rs.getString("correo"),
                            rs.getString("telefono")
                    );
                    ct.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            System.out.println("ocurrio un error al buscar al cliente");
        }
        return ct;
    }
}