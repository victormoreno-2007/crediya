package com.crediya.service;

import com.crediya.domain.errors.ErrorDomain;
import com.crediya.domain.models.Cliente;
import com.crediya.domain.models.Empleado;
import com.crediya.domain.models.Prestamo;
import com.crediya.domain.repository.PagoRepository;
import com.crediya.domain.repository.PrestamoRepository;
import com.crediya.domain.response.ResponseDomain;

import java.util.List;

public class PrestamoService {

    private final PagoRepository pagoRepository;
    private final PrestamoRepository prestamoRepository;
    private final MotorPrestamos motor;

    public PrestamoService(PrestamoRepository prestamoRepository, PagoRepository pagoRepository) {
        this.prestamoRepository = prestamoRepository;
        this.motor = new MotorPrestamos();
        this.pagoRepository = pagoRepository;
    }

    public ResponseDomain<ErrorDomain, Integer> solicitarPrestamo(Cliente cliente, Empleado empleado, double monto, double interesAnual, int cuotas) {

        double cuotaMensual = motor.calcularCuota(monto, interesAnual, cuotas);
        double montoTotal = motor.calcularMontoTotal(cuotaMensual, cuotas);


        Prestamo prestamo = new Prestamo();
        prestamo.setCliente(cliente);
        prestamo.setEmpleado(empleado);
        prestamo.setMonto(monto);
        prestamo.setInteres(interesAnual);
        prestamo.setCuotas(cuotas);
        prestamo.setCuotaMensual(cuotaMensual); // Dato calculado
        prestamo.setMontoTotal(montoTotal);     // Dato calculado
        prestamo.setSaldoPendiente(montoTotal);
        return prestamoRepository.guardar(prestamo);
    }

    public ResponseDomain<ErrorDomain, List<Prestamo>> obtenerHistorialPrestamos() {
        return prestamoRepository.listarTodosPrestamos();
    }
    public ResponseDomain<ErrorDomain, Boolean> actualizarPrestamo(Prestamo prestamo){
        double totalPagado = pagoRepository.sumarPagosPorPrestamo(prestamo.getId());

        double nuevoSaldo = prestamo.getMontoTotal() - totalPagado;

        prestamo.setSaldoPendiente(nuevoSaldo);

        if (nuevoSaldo <= 0){
            prestamo.setEstado("PAGADO");
            if (nuevoSaldo < 0) {
                prestamo.setSaldoPendiente(0);
            }
        } else {
            prestamo.setEstado("PENDIENTE");
        }
        return prestamoRepository.actualizar(prestamo);
    }

    public ResponseDomain<ErrorDomain, Boolean> eliminarPrestamo(int id) {
        return prestamoRepository.eliminar(id);
    }

    public ResponseDomain<ErrorDomain, Prestamo> buscarPrestamoPorId(int id) {
        return prestamoRepository.buscarPorId(id);
    }

    public ResponseDomain<ErrorDomain, List<Prestamo>> listarPrestamosPorCliente(int idCliente) {
        return prestamoRepository.buscarPorCliente(idCliente);
    }
}