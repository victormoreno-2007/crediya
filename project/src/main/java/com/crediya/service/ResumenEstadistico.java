package com.crediya.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.crediya.domain.models.Prestamo;
import com.crediya.domain.repository.IReportes;

public class ResumenEstadistico implements IReportes{

    @Override
    public String generarReporetes(List<Prestamo> prestamos) {
        long total = prestamos.size();
        if (total == 0) {
            return "No hay prestamos";
        }


        long  pagados = prestamos.stream().filter(p -> "PAGADO".equalsIgnoreCase(p.getEstado())).count();

        double porcentaje = (double) pagados / total * 100;

        LocalDate hoy = LocalDate.now();

        double diasMora = prestamos.stream().filter(p -> p.getFechaVencimiento().isBefore(hoy) && !"PAGADO".equalsIgnoreCase(p.getEstado())).mapToLong(p-> ChronoUnit.DAYS.between(p.getFechaInicio(), hoy)).average().orElse(0);

        long ClientesActivos = prestamos.stream().filter(p -> "ACTIVO".equalsIgnoreCase(p.getEstado())).count();

        StringBuilder sb = new StringBuilder();

        sb.append("===== REPORTES ESTADISTICO =====");
        sb.append("porcentajes de prestamos ").append(porcentaje);
        sb.append("promedios de mora ").append(diasMora);
        sb.append("clientes activos").append(ClientesActivos);
        sb.append("archivo creado :) ");
        return sb.toString();
    }
    
}