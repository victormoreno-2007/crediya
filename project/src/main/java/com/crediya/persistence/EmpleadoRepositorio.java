package com.crediya.persistence;

import com.crediya.models.Empleado;

import java.util.List;

public interface EmpleadoRepositorio {
    void registrar(Empleado empleado);

    List<Empleado> listarTodos();

    Empleado buscarPorDOcumento(String documento);
}
