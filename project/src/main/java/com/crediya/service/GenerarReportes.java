package com.crediya.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.crediya.domain.models.Prestamo;
import com.crediya.domain.repository.IReportes;

public class GenerarReportes {
    public void generarReporeteEnArchivo(List<Prestamo> lista, String tipoReportes){
        IReportes estrategia = ReporteFactory.creaReportes(tipoReportes);

        String contenido = estrategia.generarReporetes(lista);

        String nomArchivo = "Reportes" + tipoReportes + ".txt";

        try(BufferedWriter writer =  new BufferedWriter(new FileWriter(nomArchivo))){
            writer.write(contenido);
            System.out.println("reporte generado :) ");
            System.out.println(contenido);
        }
    catch (IOException e) {
        System.out.println("error al escribir el archivo" + e.getMessage());
    } catch (IllegalArgumentException e){
        System.out.println("Error "+ e.getMessage());
    }
}
}
