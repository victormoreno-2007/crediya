package com.crediya.domain.repository;

import java.util.List;

import com.crediya.domain.models.Prestamo;

public interface IReportes {
    
    String generarReporetes(List<Prestamo> prestamos);
}
