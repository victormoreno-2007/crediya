package com.crediya.data.persistence;

import com.crediya.domain.models.Empleado;

import java.util.List;

public interface EmpleadoRepositorio {
    void registrar(Empleado empleado);

    List<Empleado> listarTodos();

    Empleado buscarPorDOcumento(String documento);
}
