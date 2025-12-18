package com.crediya.service;

import com.crediya.domain.models.Cliente;
import com.crediya.domain.models.Empleado;
import com.crediya.domain.models.Prestamo;
import com.crediya.domain.repository.ClienteRepository;
import com.crediya.domain.repository.EmpleadoRepository;
import com.crediya.domain.repository.PrestamoRepository;
import com.crediya.util.GestorArchivos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ReporteServicio {
    private final ClienteRepository clienteRepo;
    private final EmpleadoRepository empleadoRepo;
    private final PrestamoRepository prestamoRepo;
    private final GestorArchivos gestorArchivos;

    public ReporteServicio(ClienteRepository cRepo, EmpleadoRepository eRepo, PrestamoRepository pRepo){
        this.clienteRepo = cRepo;
        this.empleadoRepo = eRepo;
        this.prestamoRepo = pRepo;
        this.gestorArchivos = new GestorArchivos();
    }

    public void generarReporteClientes() {
        var respuesta = clienteRepo.listarTodos();
        if (respuesta.hasError()) return;
        List<Cliente> clientes = respuesta.getModel();

        List<String> lineas = clientes.stream()
                .map(c -> c.getId() + " | " + c.getNombre() + " | " + c.getDocumento() + " | " + c.getCorreo())
                .collect(Collectors.toList());

        guardarReporte("clientes", lineas);
    }

    public void generarReporteEmpleados() {
        var respuesta = empleadoRepo.listarTodos();
        if (respuesta.hasError()) return;
        List<Empleado> empleados = respuesta.getModel();

        List<String> lineas = empleados.stream()
                .map(e -> e.getId() + " | " + e.getNombre() + " | Rol: " + e.getRol() + " | Salario: $" + e.getSalario())
                .collect(Collectors.toList());

        guardarReporte("empleados", lineas);
    }

    public void generarReportePrestamos() {
        var respuesta = prestamoRepo.listarTodosPrestamos();
        if (respuesta.hasError()) return;

        List<String> lineas = respuesta.getModel().stream()
                .map(p -> "ID: " + p.getId() + " | Cliente: " + p.getCliente().getNombre() +
                        " | Monto: $" + p.getMonto() + " | Saldo: $" + p.getSaldoPendiente() + " | Estado: " + p.getEstado())
                .collect(Collectors.toList());

        guardarReporte("prestamos_general", lineas);
    }

    public void reportePrestamosActivos() {
        System.out.println("\n--- PRÉSTAMOS ACTIVOS (PENDIENTES) ---");
        var respuesta = prestamoRepo.listarTodosPrestamos();
        if (respuesta.hasError()) return;

        respuesta.getModel().stream()
                .filter(p -> "PENDIENTE".equalsIgnoreCase(p.getEstado()))
                .forEach(System.out::println);
    }

    public void reportePrestamosCancelados() {
        System.out.println("\n--- PRÉSTAMOS ACTIVOS (CANCELADOS) ---");
        var respuesta = prestamoRepo.listarTodosPrestamos();
        if (respuesta.hasError()) return;

        respuesta.getModel().stream()
                .filter(p -> "CANCELADOS".equalsIgnoreCase(p.getEstado()))
                .forEach(System.out::println);
    }


    public void reportePrestamosVencidos() {
        var respuesta = prestamoRepo.listarTodosPrestamos();
        if (respuesta.hasError()) return;

        System.out.println("\n--- PRÉSTAMOS VENCIDOS / MOROSOS ---");

        LocalDate hoy = LocalDate.now();

        List<Prestamo> vencidos = respuesta.getModel().stream()
                .filter(p -> "PENDIENTE".equalsIgnoreCase(p.getEstado())) // 1. Que deban plata
                .filter(p -> {
                    // 2. Calculamos si la fecha final ya pasó
                    LocalDate fechaFinEstimada = p.getFechaInicio().plusMonths(p.getCuotas());
                    return fechaFinEstimada.isBefore(hoy); // Si la fecha fin es ANTES de hoy, está vencido
                })
                .collect(Collectors.toList());

        if (vencidos.isEmpty()) {
            System.out.println("No hay clientes morosos hoy.");
        } else {
            vencidos.forEach(p -> {
                System.out.println("VENCIDO | ID Prestamo: " + p.getId());
                System.out.println("Cliente: " + p.getCliente().getNombre() + " (Doc: " + p.getCliente().getDocumento() + ")");
                System.out.println("Debió terminar de pagar el: " + p.getFechaInicio().plusMonths(p.getCuotas()));
                System.out.println("Deuda Actual: $" + p.getSaldoPendiente());
                System.out.println("------------------------------------------------");
            });

            List<String> lineasMorosos = vencidos.stream()
                    .map(p -> "MOROSO: " + p.getCliente().getNombre() + " | Deuda: " + p.getSaldoPendiente())
                    .collect(Collectors.toList());
            guardarReporte("morosos", lineasMorosos);
        }
    }

    public void guardarReporte(String tipo, List<String> lineas) {
        if (lineas.isEmpty()) {
            System.out.println("No hay datos para generar el reporte de " + tipo);
            return;
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nombreArchivo = "reporte_" + tipo + "_" + timestamp + ".txt";

        if (gestorArchivos.GuardarReportes(nombreArchivo, lineas)) {
            System.out.println("Reporte generado: " + nombreArchivo);
        }
    }
}