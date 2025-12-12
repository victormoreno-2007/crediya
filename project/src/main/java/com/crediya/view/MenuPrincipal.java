package com.crediya.view;

import com.crediya.data.repositories.ClienteRepositoryImpl;
import com.crediya.data.repositories.EmpleadoRepositoryImpl;
import com.crediya.data.repositories.PagoRepositoryImpl;
import com.crediya.data.repositories.PrestamoRepositoryImpl;
import com.crediya.domain.errors.ErrorType;
import com.crediya.domain.models.Cliente;
import com.crediya.domain.models.Empleado;
import com.crediya.service.PagoService;
import com.crediya.service.PrestamoService;
import com.crediya.util.ScannerMenu;

public class MenuPrincipal {

    private final ClienteRepositoryImpl clienteRepo;
    private final EmpleadoRepositoryImpl empleadoRepo;
    private final PrestamoService prestamoService;
    private final PagoService pagoService;

    private final ScannerMenu scanner;

    public MenuPrincipal() {
        this.clienteRepo = new ClienteRepositoryImpl();
        this.empleadoRepo = new EmpleadoRepositoryImpl();


        PrestamoRepositoryImpl prestamoRepo = new PrestamoRepositoryImpl();
        this.prestamoService = new PrestamoService(prestamoRepo);

        PagoRepositoryImpl pagoRepo = new PagoRepositoryImpl();
        this.pagoService = new PagoService(pagoRepo);

        this.scanner = new ScannerMenu();
    }

    public void iniciar() {
        boolean salir = false;
        int opcion;

        do {
            System.out.println(textoMenuPrincipal());

            opcion = scanner.leerEntero("Escoge una opción:");

            switch (opcion) {
                case 1 -> menuClientes();
                case 2 -> menuEmpleados();
                case 3 -> menuPrestamos();
                case 4 -> menuPagos();
                case 0 -> {
                    salir = true;
                    System.out.println("¡Gracias por usar Crediya! Hasta luego :)");
                    scanner.cerrar();
                }
                default -> System.out.println(" Opción no válida. Intente de nuevo :(");
            }

        } while (!salir);
    }

   // clientecitos
    private void menuClientes() {
        System.out.println("\n--- GESTIÓN DE CLIENTES ---");
        System.out.println("1. Registrar Cliente");
        System.out.println("2. Listar Clientes");
        System.out.println("3. Buscar Cliente");
        System.out.println("0. Volver");

        int op = scanner.leerEntero("Seleccione:");

        switch (op) {
            case 1 -> {
                String nombre = scanner.leerTexto("Nombre:");
                String doc = scanner.leerTextoValido("Documento:", "^\\d{7,10}$", ErrorType.NO_VALIDE_IMPUT);
                String correo = scanner.leerTextoValido("Correo:", "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", ErrorType.NO_VALIDE_IMPUT);
                String tel = scanner.leerTextoValido("Teléfono:", "^\\d{7,10}$", ErrorType.NO_VALIDE_IMPUT);

                Cliente c = new Cliente(nombre, doc, correo, tel);
                clienteRepo.registrar(c).when(
                        error -> System.out.println("Error: " + error.getErrorType().getReason()),
                        id -> System.out.println("Cliente registrado con ID: " + id)
                );
            }
            case 2 -> clienteRepo.listarTodos().when(
                    error -> System.out.println("Error: " + error),
                    lista -> lista.forEach(System.out::println)
            );
            case 3 -> {
                String doc = scanner.leerTextoValido("Ingrese documento: ","^\\d{7,10}$", ErrorType.NO_VALIDE_IMPUT );
                clienteRepo.buscarPorDocumento(doc).when(
                        error -> System.out.println("Error en la busqueda :( "+ error.getErrorType().getReason()),
                        cliente -> System.out.println("Cliente encontrado :) "+ "\n" + cliente.toString())
                );

            }
            case 0 -> System.out.println("Volviendo...");
            default -> System.out.println("Opción inválida");
        }
    }

    // empleaditos
    private void menuEmpleados() {
        System.out.println("\n--- GESTIÓN DE EMPLEADOS ---");
        System.out.println("1. Registrar Empleado");
        System.out.println("2. Listar Empleados");
        System.out.println("0. Volver");

        int op = scanner.leerEntero("Seleccione:");

        switch (op) {
            case 1 -> {
                String nombre = scanner.leerTexto("Nombre:");
                String doc = scanner.leerTextoValido("Documento: ", "^\\d{7,10}$", ErrorType.NO_VALIDE_IMPUT);
                String correo = scanner.leerTextoValido("Correo: ", "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", ErrorType.NO_VALIDE_IMPUT);
                String rol = scanner.leerTexto("Rol:");
                double salario = scanner.leerDecimalValido("Salario:" ,"^\\d+(\\.\\d+)?$", ErrorType.NO_VALIDE_IMPUT);

                Empleado e = new Empleado(nombre, doc, correo, rol, salario);
                empleadoRepo.guardar(e).when(
                        error -> System.out.println("Error: " + error.getErrorType().getReason()),
                        id -> System.out.println("Empleado registrado con ID: " + id)
                );
            }
            case 2 -> empleadoRepo.listarTodos().when(
                    error -> System.out.println("Error: " + error),
                    lista -> lista.forEach(System.out::println)
            );
            case 0 -> System.out.println("Volviendo...");
        }
    }

    // prestamitos
    private void menuPrestamos() {
        System.out.println("\n--- GESTIÓN DE PRÉSTAMOS ---");
        System.out.println("1. Solicitar Préstamo");
        System.out.println("2. Ver Historial");
        System.out.println("0. Volver");

        int op = scanner.leerEntero("Seleccione:");

        switch (op) {
            case 1 -> {
                String docCliente = scanner.leerTextoValido("Doc. Cliente: ", "^\\d{7,10}$", ErrorType.NO_VALIDE_IMPUT);
                String docEmpleado = scanner.leerTextoValido("Doc. Empleado: ","^\\d{7,10}$", ErrorType.NO_VALIDE_IMPUT);

                var respC = clienteRepo.buscarPorDocumento(docCliente);
                var respE = empleadoRepo.buscarPorDocumento(docEmpleado);

                if (respC.hasError() || respE.hasError()) {
                    System.out.println("Error: Cliente o Empleado no encontrados. Regístrelos primero.");
                    return;
                }

                double monto = scanner.leerDecimalValido("Monto:" , "^\\d+(\\.\\d+)?$", ErrorType.NO_VALIDE_IMPUT);
                double interes = scanner.leerDecimalValido("Interés Anual (%): ", "^\\d+(\\.\\d+)?$", ErrorType.NO_VALIDE_IMPUT);
                int cuotas = scanner.leerEnteroValido("Cuotas (meses):","^\\d+$", ErrorType.NO_VALIDE_IMPUT
                );

                prestamoService.solicitarPrestamo(respC.getModel(), respE.getModel(), monto, interes, cuotas)
                        .when(
                                error -> System.out.println("Error: " + error.getErrorType().getReason()),
                                id -> System.out.println("Préstamo creado exitosamente. ID: " + id)
                        );
            }
            case 2 -> prestamoService.obtenerHistorialPrestamos().when(
                    error -> System.out.println(" Error: " + error),
                    lista -> lista.forEach(System.out::println)
            );
            case 0 -> System.out.println("Volviendo...");
        }
    }

    // paguitos
    private void menuPagos() {
        System.out.println("\n--- GESTIÓN DE PAGOS ---");
        System.out.println("1. Registrar Pago");
        System.out.println("2. Ver Historial");
        System.out.println("0. Volver");

        int op = scanner.leerEntero("Seleccione:");

        switch (op) {
            case 1 -> {
                int idPrestamo = scanner.leerEnteroValido("ID Préstamo: ", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);
                double monto = scanner.leerDecimalValido("Monto a pagar: ", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);

                pagoService.registrarPago(idPrestamo, monto).when(
                        error -> System.out.println("Error: " + error.getErrorType().getReason()),
                        id -> System.out.println("Pago registrado. Recibo ID: " + id)
                );
            }
            case 2 -> pagoService.obtenerHistorialPagos().when(
                    error -> System.out.println("Error: " + error),
                    lista -> lista.forEach(p -> System.out.println(p))
            );
            case 0 -> System.out.println("Volviendo...");
        }
    }

    // Arte ASCII
    public String textoMenuPrincipal() {
        return """
                  ||===========================================================||
                  ||            BIENVENIDO A TU SISTEMA DE PRESTAMO            ||
                  ||===========================================================||
                  ||   ||===||   ||==\\   ||====  ||==\\   ||    \\  //   //\\     ||
                  ||   ||        ||  ||  ||      ||   || ||     \\//   //  \\    ||
                  ||   ||        ||=//   ||==    ||   || ||      ||   ||==||   ||
                  ||   ||        || \\    ||      ||   || ||      ||   ||  ||   ||
                  ||   ||===||   ||  \\   ||====  ||==//  ||      ||   ||  ||   ||
                  ||===========================================================||
                
                1. Gestión de Clientes
                2. Gestión de Empleados
                3. Gestión de Préstamos
                4. Gestión de Pagos
                0. Salir
                """;
    }
}