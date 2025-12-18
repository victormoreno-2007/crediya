package com.crediya.service;

import com.crediya.domain.repository.IReportes;

public class ReporteFactory {
    public static IReportes creaReportes(String tipo){
        switch (tipo.toLowerCase()) {
            case "general": return new ReporteGeneral();
            case "morosos": return new Morosos();
            case "estadistica": return new ResumenEstadistico();
        
            default: throw new IllegalArgumentException("reporte no valido "+ tipo);
        }
    }
}
