package com.crediya.repository;

import com.crediya.models.Empleado;

import java.util.List;

public interface EmpleadoRepository {
    void guardar(Empleado empleado);

    List<Empleado> listarTodos();

    Empleado buscarPorDocumento(String documento);
}
