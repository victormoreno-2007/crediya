package com.crediya.domain.repository;

import com.crediya.domain.errors.ErrorDomain;
import com.crediya.domain.models.Prestamo;
import com.crediya.domain.response.ResponseDomain;

import java.util.List;

public interface PrestamoRepository {
    ResponseDomain<ErrorDomain, Integer> guardar(Prestamo prestamo);

    ResponseDomain<ErrorDomain, List<Prestamo>> listarTodosPrestamos();

}
