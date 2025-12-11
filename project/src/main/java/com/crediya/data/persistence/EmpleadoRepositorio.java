package com.crediya.data.persistence;

import java.util.List;

import com.crediya.domain.models.Empleado;

public interface EmpleadoRepositorio {
    void registrar(Empleado empleado);

    List<Empleado> listarTodos();

    Empleado buscarPorDOcumento(String documento);
}
