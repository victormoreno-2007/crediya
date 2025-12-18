package com.crediya.view;

import java.util.List;

import com.crediya.data.repositories.ClienteRepositoryImpl;
import com.crediya.data.repositories.EmpleadoRepositoryImpl;
import com.crediya.data.repositories.PagoRepositoryImpl;
import com.crediya.data.repositories.PrestamoRepositoryImpl;
import com.crediya.domain.errors.ErrorType;
import com.crediya.domain.errors.OperacionCanceladaExcepcion;
import com.crediya.domain.models.Cliente;
import com.crediya.domain.models.Empleado;
import com.crediya.domain.models.Prestamo;
import com.crediya.domain.repository.PrestamoRepository;
import com.crediya.service.GenerarReportes;
import com.crediya.service.Morosos;
import com.crediya.service.PagoService;
import com.crediya.service.PrestamoService;
import com.crediya.service.ReporteGeneral;
import com.crediya.service.ReporteServicio;
import com.crediya.util.GestorArchivos;
import com.crediya.util.ScannerMenu;

public class MenuPrincipal {

    private final ClienteRepositoryImpl clienteRepo;
    private final EmpleadoRepositoryImpl empleadoRepo;
    private final PrestamoService prestamoService;
    private final PagoService pagoService;
    private final ReporteServicio reporteService;
    private final ScannerMenu scanner;
    private final ReporteGeneral reporteGeneral;
    private final Morosos moroso;
    private final PrestamoRepository prestamoRepository;
    private final GenerarReportes generador;

    public MenuPrincipal() {
        this.clienteRepo = new ClienteRepositoryImpl();
        this.empleadoRepo = new EmpleadoRepositoryImpl();

        PrestamoRepositoryImpl prestamoRepo = new PrestamoRepositoryImpl();
        PagoRepositoryImpl pagoRepo = new PagoRepositoryImpl();

        this.prestamoService = new PrestamoService(prestamoRepo, pagoRepo);
        this.pagoService = new PagoService(pagoRepo, prestamoRepo);

        this.reporteService = new ReporteServicio(clienteRepo, empleadoRepo, prestamoRepo);
        this.reporteGeneral = new ReporteGeneral();
        this.moroso = new Morosos();
        this.prestamoRepository = new PrestamoRepositoryImpl();
        this.generador = new GenerarReportes();

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
                case 5 -> menuReportes();
                case 6 -> menuExamen();
                case 0 -> {
                    salir = true;
                    System.out.println("¡Gracias por usar Crediya! Hasta luego :)");
                    scanner.cerrar();
                }
                default -> System.out.println(" Opción no válida. Intente de nuevo :(");
            }

        } while (!salir);
    }

    // clientesitos
    private void menuClientes() {
        System.out.println("\n--- GESTIÓN DE CLIENTES ---");
        System.out.println("1. Registrar Cliente");
        System.out.println("2. Listar Clientes");
        System.out.println("3. Buscar Cliente");
        System.out.println("4. Actualizar Cliente");
        System.out.println("5. Eliminar Cliente");
        System.out.println("6. Ver Préstamos del Cliente");
        System.out.println("7. Examen");
        System.out.println("0. Volver");

        int op = scanner.leerEntero("Seleccione:");

        try {
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
                case 4 -> {
                    String docBusqueda = scanner.leerTextoValido("Ingrese el Documento del cliente a editar:", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);
                    clienteRepo.buscarPorDocumento(docBusqueda).when(
                            error -> System.out.println("Error: "+ error.getErrorType().getReason()),
                            clienteEncontrado -> {
                                System.out.println("Editando a: " + clienteEncontrado.getNombre());
                                String nombre = scanner.leerTexto("Nuevo Nombre:");
                                String correo = scanner.leerTextoValido("Nuevo Correo:", "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", ErrorType.NO_VALIDE_IMPUT);
                                String tel = scanner.leerTextoValido("Nuevo Teléfono:", "^\\d{7,10}$", ErrorType.NO_VALIDE_IMPUT);

                                clienteEncontrado.setNombre(nombre);
                                clienteEncontrado.setCorreo(correo);
                                clienteEncontrado.setTelefono(tel);

                                clienteRepo.actualizar(clienteEncontrado).when(
                                        error -> System.out.println("Error al actualizar: " + error.getErrorType().getReason()),
                                        succes -> System.out.println("Cliente actualizado exitosamente :)")
                                );
                            }
                    );
                }
                case 5 -> {
                    String docEliminar = scanner.leerTextoValido("Ingrese Documento a eliminar:", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);
                    clienteRepo.buscarPorDocumento(docEliminar).when(
                            error -> System.out.println("Error: " + error.getErrorType().getReason()),
                            clienteEncontrado -> {
                                System.out.println("Estas intentando eliminar al cliente con documento: "+ clienteEncontrado.getDocumento());
                                String confirmacion = scanner.leerTextoValido("¿Seguro? (Escribe 1 para SI):", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);

                                if (confirmacion.equals("1")){
                                    clienteRepo.eliminar(clienteEncontrado.getId()).when(
                                            error -> System.out.println("Error :" + error.getErrorType().getReason()),
                                            good -> System.out.println("Cliente eliminado del sistema con ID: " + clienteEncontrado.getId())
                                    );
                                } else {
                                    System.out.println("Este proceso fue cancelado :(");
                                }
                            }
                    );
                }
                case 6 -> {
                    String doc = scanner.leerTextoValido("Ingrese Documento del Cliente:", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);
                    clienteRepo.buscarPorDocumento(doc).when(
                            error -> System.out.println("Cliente no encontrado."),
                            cliente -> {
                                System.out.println("--- Préstamos de " + cliente.getNombre() + " ---");
                                prestamoService.listarPrestamosPorCliente(cliente.getId()).when(
                                        err -> System.out.println("Error al buscar préstamos."),
                                        lista -> {
                                            if (lista.isEmpty()) System.out.println("Este cliente no tiene préstamos registrados.");
                                            else lista.forEach(p ->
                                                    System.out.println("ID: " + p.getId() + " | Monto: " + p.getMonto() +
                                                            " | Saldo: " + p.getSaldoPendiente() + " | Estado: " + p.getEstado())
                                            );
                                        }
                                );
                            }
                    );
                }
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }
        } catch (OperacionCanceladaExcepcion e) {
            System.out.println("\n🔙 " + e.getMessage() + " Regresando al menú...");
        }
    }

    // empleaditos
    private void menuEmpleados() {
        System.out.println("\n--- GESTIÓN DE EMPLEADOS ---");
        System.out.println("1. Registrar Empleado");
        System.out.println("2. Listar Empleados");
        System.out.println("3. Actualizar Empleados");
        System.out.println("4. Eliminar Empleados");
        System.out.println("0. Volver");

        int op = scanner.leerEntero("Seleccione:");
        try {
            switch (op) {
                case 1 -> {
                    String nombre = scanner.leerTexto("Nombre:");
                    String doc = scanner.leerTextoValido("Documento: ", "^\\d{7,10}$", ErrorType.NO_VALIDE_IMPUT);
                    String correo = scanner.leerTextoValido("Correo: ", "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", ErrorType.NO_VALIDE_IMPUT);
                    String rol = scanner.leerTexto("Rol:");
                    double salario = scanner.leerDecimalValido("Salario:" ,"^\\d*(\\.\\d+)?$", ErrorType.NO_VALIDE_IMPUT);

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
                case 3 -> {
                    String docBusqueda = scanner.leerTextoValido("Ingrese el Documento del empleado a editar:", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);
                    empleadoRepo.buscarPorDocumento(docBusqueda).when(
                            error -> System.out.println("Error: "+ error.getErrorType().getReason()),
                            empleadoEncontrado -> {
                                System.out.println("Editando a: " + empleadoEncontrado.getNombre());
                                String nombre = scanner.leerTexto("Nuevo Nombre:");
                                String correo = scanner.leerTextoValido("Nuevo Correo:", "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", ErrorType.NO_VALIDE_IMPUT);
                                String rol = scanner.leerTexto("Nuevo Rol:");

                                Double salario = scanner.leerDecimalValido("Nuevo Salario" , "^\\d*(\\.\\d+)?$", ErrorType.NO_VALIDE_IMPUT);

                                empleadoEncontrado.setNombre(nombre);
                                empleadoEncontrado.setCorreo(correo);
                                empleadoEncontrado.setRol(rol);
                                empleadoEncontrado.setSalario(salario);

                                empleadoRepo.actualizar(empleadoEncontrado).when(
                                        error -> System.out.println("Error al actualizar: " + error.getErrorType().getReason()),
                                        succes -> System.out.println("Empleado actualizado exitosamente :)")
                                );
                            }
                    );
                }
                case 4 -> {
                    String docEliminar = scanner.leerTextoValido("Ingrese Documento a eliminar:", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);
                    empleadoRepo.buscarPorDocumento(docEliminar).when(
                            error -> System.out.println("Error: " + error.getErrorType().getReason()),
                            succes -> {
                                System.out.println("Estas intentando eliminar al empleado con documento: "+ succes.getDocumento());
                                String confirmacion = scanner.leerTextoValido("¿Seguro? (Escribe 1 para SI):", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);

                                if (confirmacion.equals("1")){
                                    empleadoRepo.eliminar(succes.getId()).when(
                                            error -> System.out.println("Error :" + error.getErrorType().getReason()),
                                            good -> System.out.println("Empleado eliminado del sistema con ID: " + succes.getId())
                                    );
                                } else {
                                    System.out.println("Este proceso fue cancelado :(");
                                }
                            }
                    );
                }
                case 0 -> System.out.println("Volviendo...");
            }
        } catch (OperacionCanceladaExcepcion e) {
            System.out.println("\n🔙 " + e.getMessage() + " Regresando al menú...");
        }
    }

    // prestamitos
    private void menuPrestamos() {
        System.out.println("\n--- GESTIÓN DE PRÉSTAMOS ---");
        System.out.println("1. Solicitar Préstamo");
        System.out.println("2. Ver Historial");
        System.out.println("3. Actualizar Prestamo");
        System.out.println("4. Eliminar Prestamo");
        System.out.println("0. Volver");

        int op = scanner.leerEntero("Seleccione:");
        try {
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

                    double monto = scanner.leerDecimalValido("Monto:" , "^\\d*(\\.\\d+)?$", ErrorType.NO_VALIDE_IMPUT);
                    double interes = scanner.leerDecimalValido("Interés Anual (%): ", "^\\d*(\\.\\d+)?$", ErrorType.NO_VALIDE_IMPUT);
                    int cuotas = scanner.leerEnteroValido("Cuotas (meses):","^\\d+$", ErrorType.NO_VALIDE_IMPUT);

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
                case 3 -> {
                    int docBusqueda = scanner.leerEnteroValido("Ingrese el ID del prestamo a editar:", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);
                    prestamoService.buscarPrestamoPorId(docBusqueda).when(
                            error -> System.out.println("Error: " + error.getErrorType().getReason()),
                            prestamoEncontrado -> {
                                System.out.println("-----------------------------------");
                                System.out.println("Editando Préstamo de: " + prestamoEncontrado.getCliente().getNombre());
                                System.out.println("Monto Actual: " + prestamoEncontrado.getMonto());
                                System.out.println("-----------------------------------");

                                double nuevoMonto = scanner.leerDecimalValido("Nuevo Monto:", "^\\d*(\\.\\d+)?$", ErrorType.NO_VALIDE_IMPUT);
                                double nuevoInteres = scanner.leerDecimalValido("Nuevo Interés:", "^\\d*(\\.\\d+)?$", ErrorType.NO_VALIDE_IMPUT);
                                int nuevasCuotas = Integer.parseInt(scanner.leerTextoValido("Nuevas Cuotas:", "^\\d+$", ErrorType.NO_VALIDE_IMPUT));

                                prestamoEncontrado.setMonto(nuevoMonto);
                                prestamoEncontrado.setInteres(nuevoInteres);
                                prestamoEncontrado.setCuotas(nuevasCuotas);

                                prestamoService.actualizarPrestamo(prestamoEncontrado).when(
                                        err -> System.out.println("Error: " + err.getErrorType().getReason()),
                                        ok -> System.out.println("Préstamo actualizado.")
                                );
                            }
                    );
                }

                case 4 -> {
                    int docEliminar = scanner.leerEnteroValido("Ingrese el id del prestamo a eliminar: ", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);
                    prestamoService.buscarPrestamoPorId(docEliminar).when(
                            error -> System.out.println("Error: " + error.getErrorType().getReason()),
                            succes -> {
                                System.out.println("Estas intentando eliminar el prestamo con ID: "+ succes.getId());

                                String confirmacion = scanner.leerTextoValido("¿Seguro? (Escribe 1 para SI):", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);

                                if (confirmacion.equals("1")){
                                    prestamoService.eliminarPrestamo(succes.getId()).when(
                                            error -> System.out.println("Error :" + error.getErrorType().getReason()),
                                            good -> System.out.println("Prestamo eliminado del sistema con ID: " + succes.getId())
                                    );
                                } else {
                                    System.out.println("Este proceso fue cancelado :(");
                                }
                            }
                    );
                }
                case 0 -> System.out.println("Volviendo...");
            }
        } catch (OperacionCanceladaExcepcion e) {
            System.out.println("\n🔙 " + e.getMessage() + " Regresando al menú...");
        }
    }

    // paguitos
    private void menuPagos() {
        System.out.println("\n--- GESTIÓN DE PAGOS ---");
        System.out.println("1. Registrar Pago");
        System.out.println("2. Ver Historial");
        System.out.println("3. Buscar Pago Por Id");
        System.out.println("4. Actualizar Pago");
        System.out.println("5. Eliminar Pago");
        System.out.println("0. Volver");

        int op = scanner.leerEntero("Seleccione:");

        try {
            switch (op) {
                case 1 -> {
                    int idPrestamo = scanner.leerEnteroValido("ID Préstamo: ", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);
                    double monto = scanner.leerDecimalValido("Monto a pagar: ", "^\\d*(\\.\\d+)?$", ErrorType.NO_VALIDE_IMPUT);

                    pagoService.registrarPago(idPrestamo, monto).when(
                            error -> System.out.println("Error: " + error.getErrorType().getReason()),
                            id -> System.out.println("Pago registrado. Recibo ID: " + id)
                    );
                }
                case 2 -> pagoService.obtenerHistorialPagos().when(
                        error -> System.out.println("Error: " + error),
                        lista -> lista.forEach(p -> System.out.println(p))
                );
                case 3 -> {
                    int idBuscar = scanner.leerEnteroValido("Ingrese el ID del Préstamo a buscar:", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);

                    pagoService.buscarPagoPorId(idBuscar).when(
                            error -> System.out.println("Error" + error.getErrorType().getReason()),
                            p -> {
                                System.out.println("\n===  DETALLE DEL PRÉSTAMO #" + p.getId() + " ===");
                                System.out.println("ID:        $" + p.getId());
                                System.out.println("ID Prestamo:      " + p.getPrestamo().getId());
                                System.out.println("Fecha Pago:     " + p.getFechaPago());
                                System.out.println("Monto: " + p.getMonto());
                            }
                    );
                }
                case 4 -> {
                    int docBusqueda = scanner.leerEnteroValido("Ingrese el ID del prestamo a editar:", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);
                    pagoService.buscarPagoPorId(docBusqueda).when(
                            error -> System.out.println("Error: " + error.getErrorType().getReason()),
                            pagoEncontrado -> {
                                System.out.println("-----------------------------------");
                                System.out.println("Editando Pago de: " + pagoEncontrado.getPrestamo().getCliente());
                                System.out.println("-----------------------------------");

                                double nuevoMonto = scanner.leerDecimalValido("Nuevo Monto: ", "^\\d*(\\.\\d+)?$", ErrorType.NO_VALIDE_IMPUT);
                                int nuevoId = scanner.leerEnteroValido("Nuevo ID del Préstamo:", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);

                                pagoEncontrado.setMonto(nuevoMonto);
                                pagoEncontrado.getPrestamo().setId(nuevoId);

                                pagoService.actualizarPago(pagoEncontrado).when(
                                        err -> System.out.println("Error: " + err.getErrorType().getReason()),
                                        ok -> System.out.println("Pago actualizado.")
                                );
                            }
                    );
                }
                case 5 -> {
                    int docEliminar = scanner.leerEnteroValido("Ingrese el id del pago a eliminar: ", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);
                    pagoService.buscarPagoPorId(docEliminar).when(
                            error -> System.out.println("Error: " + error.getErrorType().getReason()),
                            succes -> {
                                System.out.println("Estas intentando eliminar el pago con ID: "+ succes.getId());

                                String confirmacion = scanner.leerTextoValido("¿Seguro? (Escribe 1 para SI):", "^\\d+$", ErrorType.NO_VALIDE_IMPUT);

                                if (confirmacion.equals("1")){
                                    pagoService.eliminarPago(succes.getId()).when(
                                            error -> System.out.println("Error :" + error.getErrorType().getReason()),
                                            good -> System.out.println("Pago eliminado del sistema con ID: " + succes.getId())
                                    );
                                } else {
                                    System.out.println("Este proceso fue cancelado :(");
                                }
                            }
                    );
                }
                case 0 -> System.out.println("Volviendo...");
            }
        } catch (OperacionCanceladaExcepcion e) {
            System.out.println("\n🔙 " + e.getMessage() + " Regresando al menú...");
        }
    }

    // reporticos
    private void menuReportes() {
        System.out.println("\n--- 📊 MÓDULO DE REPORTES ---");
        System.out.println("1. Generar TXT Clientes");
        System.out.println("2. Generar TXT Empleados");
        System.out.println("3. Generar TXT Préstamos (General)");
        System.out.println("4. Ver Préstamos Activos (Pantalla)");
        System.out.println("5. Ver Cartera Vencida / Morosos (Pantalla + TXT)");
        System.out.println("0. Volver");

        int op = scanner.leerEntero("Seleccione:");
        try {
            switch (op) {
                case 1 -> reporteService.generarReporteClientes();
                case 2 -> reporteService.generarReporteEmpleados();
                case 3 -> reporteService.generarReportePrestamos();
                case 4 -> reporteService.reportePrestamosActivos();
                case 5 -> reporteService.reportePrestamosVencidos();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción no válida.");
            }
        } catch (OperacionCanceladaExcepcion e) {
            System.out.println("\n🔙 " + e.getMessage() + " Regresando al menú...");
        }
    }

    private void menuExamen() {
        System.out.println("1. reporte general");
        System.out.println("2. reporte morosos");
        System.out.println("3. generar reportes");
        System.out.println("0. volver");
        System.out.println("reportes estadisticos");
        
        int op = scanner.leerEntero("Seleccione:");
        GenerarReportes reportes = new GenerarReportes();
        List<Prestamo> listaParaPrestamo = prestamoRepository.listarTodosPrestamos().getModel();

        try {
            switch (op) {
                case 1 -> reporteGeneral.generarReporetes(prestamoRepository.listarTodosPrestamos().getModel());
                case 2 -> moroso.generarReporetes(prestamoRepository.listarTodosPrestamos().getModel());
                case 3 -> generador.generarReporeteEnArchivo(listaParaPrestamo, "estadistica");

                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción no válida.");
            }
        } catch (OperacionCanceladaExcepcion e) {
            System.out.println("\n🔙 " + e.getMessage() + " Regresando al menú...");
        }
    }
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
                5. Reporte Servicio
                6. examen
                0. Salir
                """;
    }
}