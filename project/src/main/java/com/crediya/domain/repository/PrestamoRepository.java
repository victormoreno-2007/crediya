package com.crediya.domain.repository;

import com.crediya.domain.errors.ErrorDomain;
import com.crediya.domain.models.Prestamo;
import com.crediya.domain.response.ResponseDomain;

import java.util.List;

public interface PrestamoRepository {
    ResponseDomain<ErrorDomain, Integer> guardar(Prestamo prestamo);

    ResponseDomain<ErrorDomain, List<Prestamo>> listarTodosPrestamos();

    ResponseDomain<ErrorDomain, Boolean> actualizar(Prestamo prestamo);

    ResponseDomain<ErrorDomain, Boolean> eliminar(int id);

    ResponseDomain<ErrorDomain, Prestamo> buscarPorId(int id);

    ResponseDomain<ErrorDomain, List<Prestamo>> buscarPorCliente(int idCliente);
}
