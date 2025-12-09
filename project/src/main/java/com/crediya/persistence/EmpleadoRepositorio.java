package com.crediya.persistence;

import java.util.List;

import com.crediya.models.Empleado;

public interface EmpleadoRepositorio {
    void registrar(Empleado empleado);

    List<Empleado> listarTodos();

    Empleado buscarPorDOcumento(String documento);
}
