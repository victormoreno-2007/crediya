package com.crediya.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.crediya.domain.models.Prestamo;
import com.crediya.domain.repository.IReportes;

public class Morosos implements IReportes {
    @Override
    public String generarReporetes(List<Prestamo> prestamos) {
        LocalDate hoy = LocalDate.now();

        List<Prestamo> morosos = prestamos.stream().filter(p -> p.getFechaVencimiento().isBefore(hoy) && !"CANCELADO".equalsIgnoreCase(p.getEstado()) && !"PAGADO".equalsIgnoreCase(p.getEstado())).collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();

        sb.append("===== CLIENTES MOROSOS ===\n");
        if (morosos.isEmpty()) {
            sb.append("no hay clientes morosos");
        } else {
            morosos.forEach(p -> 
                sb.append("cliente ID: ").append(p.getId())
                .append("monto ").append(p.getMonto())
                .append("vencio en ").append(p.getFechaVencimiento()));
                return sb.toString();
            
        }  
        return sb.toString();
    }
    
}
