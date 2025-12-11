package com.crediya.domain.repository;

import com.crediya.domain.errors.ErrorDomain;
import com.crediya.domain.models.Empleado;
import com.crediya.domain.response.ResponseDomain;

import java.util.List;

public interface EmpleadoRepository {
    ResponseDomain<ErrorDomain, Integer> guardar(Empleado empleado);

    ResponseDomain<ErrorDomain, List<Empleado>> listarTodos();

    ResponseDomain<ErrorDomain, Empleado> buscarPorDocumento(String documento);
}
