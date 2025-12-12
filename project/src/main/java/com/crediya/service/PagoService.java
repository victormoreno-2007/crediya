package com.crediya.service;

import com.crediya.domain.errors.ErrorDomain;
import com.crediya.domain.models.Pago;
import com.crediya.domain.models.Prestamo;
import com.crediya.domain.repository.PagoRepository;
import com.crediya.domain.response.ResponseDomain;

import java.time.LocalDate;
import java.util.List;

public class PagoService {

    private final PagoRepository pagoRepository;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    public ResponseDomain<ErrorDomain, Integer> registrarPago(int idPrestamo, double monto){
        Prestamo prestamo = new Prestamo();
        prestamo.setId(idPrestamo);

        Pago pago = new Pago();
        pago.setMonto(monto);
        pago.setFechaPago(LocalDate.now());
        pago.setPrestamo(prestamo);

        return pagoRepository.guardar(pago);

    }
    public ResponseDomain<ErrorDomain, List<Pago>> obtenerHistorialPagos(){
        return pagoRepository.listarPagos();
    }


}
