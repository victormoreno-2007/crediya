package com.crediya.service;

import com.crediya.models.Clientes;
import com.crediya.persistence.impl.ClienteDAO;
import com.crediya.util.GestorArchivos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ReporteServicio {
    private final ClienteDAO clienteDAO;
    private final GestorArchivos gestorArchivos;

    public ReporteServicio(){
        this.clienteDAO = new ClienteDAO();
        this.gestorArchivos = new GestorArchivos();
    }

    public void crearArchivos(){
        System.out.println("Generando archivos");

        List<Clientes> listarClientes = clienteDAO.listarTodos();

        if (listarClientes.isEmpty()){
            System.out.println("Aun no hay clientes disponibles");
            return;
        }

        List<String> lineasParaArchivo = listarClientes.stream()
                .map(c -> "ID: " + c.getId() + " | Nombre: " + c.getNombre() + " | Doc: " + c.getDocumento())
                .collect(Collectors.toList());

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nombreArchivo = "reporte_clientes_" + timestamp + ".txt";

        gestorArchivos.GuardarReportes(nombreArchivo, lineasParaArchivo);


    }


}
