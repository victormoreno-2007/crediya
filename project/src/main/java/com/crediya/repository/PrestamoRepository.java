package com.crediya.repository;

import com.crediya.models.Prestamo;

import java.util.List;

public interface PrestamoRepository {
    void guardar(Prestamo prestamo);

    List<Prestamo> listarTodos();

}
