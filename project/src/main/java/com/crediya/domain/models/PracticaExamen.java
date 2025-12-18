package com.crediya.domain.models;

import com.crediya.connection.Conexion;
import com.crediya.domain.repository.ClienteRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// Clase modelo simple (Simulando tu entidad)
class PrestamoE {
    private int id;
    private String cliente;
    private double monto;
    private String estado; // "APROBADO", "PENDIENTE", "RECHAZADO", "PAGADO"
    private LocalDate fechaSolicitud;

    public PrestamoE (int id, String cliente, double monto, String estado, String fecha) {
        this.id = id;
        this.cliente = cliente;
        this.monto = monto;
        this.estado = estado;
        this.fechaSolicitud = LocalDate.parse(fecha); // Formato YYYY-MM-DD
    }

    // Getters y toString para imprimir
    public  int getId() {return id;}
    public String getEstado() { return estado; }
    public LocalDate getFechaSolicitud() { return fechaSolicitud; }
    public double getMonto() { return monto; }
    public String getCliente() { return cliente; }

    @Override
    public String toString() {
        return "Prestamo{id=" + id + ", cliente='" + cliente + "', monto=" + monto + ", estado='" + estado + "', fecha=" + fechaSolicitud + "}";
    }
}

public class PracticaExamen {
    public static void main(String[] args) {
        // DATOS DE PRUEBA (DUMMY DATA)
        List<PrestamoE> prestamos = Arrays.asList(
                new PrestamoE(1, "Ana", 1500.00, "APROBADO", "2024-01-15"),
                new PrestamoE(2, "Luis", 5000.00, "PENDIENTE", "2024-03-10"),
                new PrestamoE(3, "Ana", 2000.00, "PAGADO", "2023-12-05"), // Cliente repetido
                new PrestamoE(4, "Carlos", 3500.00, "RECHAZADO", "2024-02-20"),
                new PrestamoE(5, "Sofia", 10000.00, "APROBADO", "2024-03-01"),
                new PrestamoE(6, "Luis", 500.00, "PENDIENTE", "2024-03-15") // Cliente repetido y fecha reciente
        );

        System.out.println("--- RESULTADOS ---");

        System.out.println("--- RETO A: Ordenado por Fecha ---");
        prestamos.stream()
                .sorted(java.util.Comparator.comparing(PrestamoE::getFechaSolicitud))
                .forEach(System.out::println);


        System.out.println("RETO B");
        // lsita mayores a 2000 y despues de la fecha de fechaCorte
        LocalDate fechaCorte = LocalDate.of(2024, 1, 1);
        prestamos.stream()
                .filter(p -> p.getMonto() > 2000)
                .filter(p -> p.getFechaSolicitud().isAfter(fechaCorte))
                .forEach(System.out::println
                );

        // LISTADO SIN DUPLICADO
        prestamos.stream().map(PrestamoE::getCliente)
                .distinct().forEach(System.out::println);

        // AGRUPAR POR ESTADO
        Map<String, List<PrestamoE>> grupos = prestamos.stream().collect(Collectors.groupingBy(PrestamoE::getEstado));

        grupos.forEach((estado, listaDePrestamos) -> {System.out.println("estado " + estado);
        listaDePrestamos.forEach(p -> System.out.println("  - "+ p));});


        // SUMAR MONTO DE TODO
       double totalPrestamo = prestamos.stream().mapToDouble(PrestamoE::getMonto).sum();
        System.out.println("total dinero: "+ totalPrestamo);

        // Obtener una lista solo con los IDs de los préstamos "RECHAZADO".
        List<Integer> idsRechazados = prestamos.stream().filter(p -> "RECHAZADO".equalsIgnoreCase(p.getEstado()))
                .map(PrestamoE::getId)
                .collect(Collectors.toList());

        // extraer el prestamo mas caro
        PrestamoE caro = prestamos.stream().max(Comparator.comparing(PrestamoE::getMonto)).get();
        System.out.println("mas caro uff : "+ caro);

        // metodo para listar todos ejmeplo con Cliente



    }
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes"; // Asegúrate que la tabla se llame así en tu BD

        try (Connection conn = Conexion.getConexion(); // Tu método para conectar
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Aquí reconstruyes el objeto Cliente con los datos de la base de datos
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                // cliente.setApellido(rs.getString("apellido"));
                //  cliente.setEmail(rs.getString("email"));
                // ... seteas el resto de atributos ...

                clientes.add(cliente); // Lo agregas a la lista
            }

        } catch (SQLException e) {
            e.printStackTrace(); // O manejar la excepción como prefieras
        }

        return clientes;
    }

    // 1. Llamas al método nuevo
    List<Cliente> todos = listarTodos();

    // 2. Ahora sí puedes usar .map porque tienes una LISTA
    List<String> correos = todos.stream()
            .map(c -> c.getNombre()) // La transformación
            .collect(Collectors.toList());

    // convertir a mayusculas
    // xmap(c -> c.getNombre().toUpperCase())

    // RETO B: Filtrar VIP Recientes
// Primero definimos la fecha de referencia (1 de Enero 2024)
    // LocalDate inicio2024 = LocalDate.parse("2024-01-01");

    // List<Prestamo> vipRecientes = prestamos.stream()
          //   .filter(p -> p.getMonto() > 2000 && p.getFechaSolicitud().isAfter(inicio2024))
          //   .collect(Collectors.toList());

            // System.out.println("--- FILTRADOS (>2000 y > 2024) ---");
// vipRecientes.forEach(System.out::println);
}