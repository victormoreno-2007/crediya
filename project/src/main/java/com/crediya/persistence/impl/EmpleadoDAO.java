package com.crediya.persistence.impl;

import com.crediya.connection.Conexion;
import com.crediya.models.Empleado;
import com.crediya.persistence.EmpleadoRepositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        ){

        }
    }

    @Override
    public Empleado buscarPorDOcumento(String documento) {
        return null;
    }
}
