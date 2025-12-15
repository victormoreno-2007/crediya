package com.crediya.service;

import com.crediya.domain.errors.ErrorDomain;
import com.crediya.domain.errors.ErrorType;
import com.crediya.domain.models.Pago;
import com.crediya.domain.models.Prestamo;
import com.crediya.domain.repository.PagoRepository;
import com.crediya.domain.repository.PrestamoRepository;
import com.crediya.domain.response.ResponseDomain;

import java.time.LocalDate;
import java.util.List;

public class PagoService {

    private final PagoRepository pagoRepository;
    private final PrestamoRepository prestamoRepository;

    public PagoService(PagoRepository pagoRepository, PrestamoRepository prestamoRepository) {
        this.pagoRepository = pagoRepository;
        this.prestamoRepository = prestamoRepository;
    }

    public ResponseDomain<ErrorDomain, Integer> registrarPago(int idPrestamo, double monto){
        var respuestaPrestamo = prestamoRepository.buscarPorId(idPrestamo);

        if (respuestaPrestamo.hasError()) {
            return ResponseDomain.error(respuestaPrestamo.getError());
        }

        Prestamo prestamoReal = respuestaPrestamo.getModel();
        double saldoActual = prestamoReal.getSaldoPendiente();
        double montoFinalAPagar = monto;
        double cambio = 0.0;

        if (monto > saldoActual) {
            montoFinalAPagar = saldoActual;
            cambio = monto - saldoActual;

            System.out.println("El pago excedía la deuda.");
            System.out.println("Se tomaron: $" + montoFinalAPagar);
            System.out.println("Su cambio es: $" + cambio);
        }

        double nuevoSaldo = saldoActual - montoFinalAPagar;
        prestamoReal.setSaldoPendiente(nuevoSaldo);

        if (nuevoSaldo <= 0) {
            prestamoReal.setEstado("PAGADO");
            System.out.println("¡FELICIDADES! El préstamo ha sido pagado en su totalidad.");
        }

        if (prestamoRepository.actualizar(prestamoReal).isSuccess()) {
            Pago pago = new Pago();
            pago.setMonto(montoFinalAPagar);
            pago.setFechaPago(LocalDate.now());
            pago.setPrestamo(prestamoReal);

            return pagoRepository.guardar(pago);
        }

        return ResponseDomain.error(new ErrorDomain(ErrorType.DATABASE_ERROR));

    }
    public ResponseDomain<ErrorDomain, List<Pago>> obtenerHistorialPagos(){
        return pagoRepository.listarPagos();
    }

    public ResponseDomain<ErrorDomain, Boolean> actualizarPago(Pago pago){
        return pagoRepository.actualizar(pago);
    }

    public ResponseDomain<ErrorDomain, Boolean> eliminarPago(int idPago) {

        var respuestaPago = pagoRepository.buscarPorId(idPago);

        if (respuestaPago.isError()) {
            return ResponseDomain.error(respuestaPago.getErrorDomain());
        }

        Pago pagoAEliminar = respuestaPago.getModel();
        Prestamo prestamoAsociado = pagoAEliminar.getPrestamo();

        double saldoRestaurado = prestamoAsociado.getSaldoPendiente() + pagoAEliminar.getMonto();
        prestamoAsociado.setSaldoPendiente(saldoRestaurado);

        if (saldoRestaurado > 0) {
            prestamoAsociado.setEstado("PENDIENTE");
        }
        var respuestaUpdate = prestamoRepository.actualizar(prestamoAsociado);

        if (respuestaUpdate.isSuccess()) {
            return pagoRepository.eliminar(idPago);
        } else {
            return ResponseDomain.error(respuestaUpdate.getErrorDomain());
        }
    }

    public ResponseDomain<ErrorDomain, Pago> buscarPagoPorId(int id) {
        return pagoRepository.buscarPorId( id);
    }


}
