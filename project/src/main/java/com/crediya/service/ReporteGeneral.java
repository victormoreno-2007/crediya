package com.crediya.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.crediya.domain.models.Prestamo;

import com.crediya.domain.repository.IReportes;

public class ReporteGeneral implements  IReportes {

    @Override
    public String generarReporetes(List<Prestamo> prestamos) {
        double totalPrestado = prestamos.stream().mapToDouble(Prestamo::getMonto).sum();

        double totalPagado = prestamos.stream().filter(p -> "PAGADO".equalsIgnoreCase(p.getEstado())).mapToDouble(Prestamo::getMonto).sum();

        double saldoPendiente = prestamos.stream().filter(p -> !"PAGADO".equalsIgnoreCase(p.getEstado()) && !"CANCELADO".equalsIgnoreCase(p.getEstado())).mapToDouble(Prestamo::getMonto).sum();

        StringBuilder sb = new StringBuilder();

        sb.append("===== REPORETE GENERAL ===\n");
        sb.append("total Prestados ").append(totalPrestado).append("\n");
        sb.append("total Pagado ").append(totalPagado).append("\n");
        sb.append("saldo Pendiente ").append(saldoPendiente);

        return sb.toString();
    }
    
}


