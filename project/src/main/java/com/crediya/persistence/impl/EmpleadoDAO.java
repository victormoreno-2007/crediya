package com.crediya.persistence.impl;

import com.crediya.connection.Conexion;
import com.crediya.models.Empleado;
import com.crediya.persistence.EmpleadoRepositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO implements EmpleadoRepositorio {
    @Override
    public void registrar(Empleado empleado) {
        String sql = "INSERT INTO empleados(nombre, documento, rol, correo, salario) VALUES(?,?,?,?,?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getDocumento());
            ps.setString(3, empleado.getRol());
            ps.setString(4, empleado.getCorreo());
            ps.setDouble(5, empleado.getSalario());

            System.out.println("Empleado registrado exitosamente");

        } catch (SQLException e) {
            System.out.println("error al guardar el empleado  "+ e.getMessage());
        }
    }

    @Override
    public List<Empleado> listarTodos() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT nombre, documento, rol, correo, salario FROM empleados";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){

            while (rs.next()) {
                Empleado emp = new Empleado(
                        rs.getString("nombre"),
                        rs.getString("documento"),
                        rs.getString("rol"),
                        rs.getString("correo"),
                        rs.getDouble("salario")
                );

                lista.add(emp);
            }

        } catch (SQLException e) {
            System.out.println("tuvistes un eror al listar los empleados "+ e.getMessage());
        }
        return lista;
    }

    @Override
    public Empleado buscarPorDOcumento(String documento) {
        String sql = "SELECT nombre, documento, rol, correo, salario FROM empleados WHERE id=?";
        Empleado emp = null;

        try(Connection con = Conexion.getConexion();
        PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, documento);

            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    emp = new Empleado(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("documento"),
                            rs.getString("correo"),
                            rs.getString("rol"),
                            rs.getDouble("salario")
                    );
                }
            }

            } catch (SQLException ex) {
            System.out.println("erro al buscar el empleado "+ ex.getMessage());
        }
        return emp;
    }
}
