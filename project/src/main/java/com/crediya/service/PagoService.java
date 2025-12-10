package com.crediya.service;

import com.crediya.models.Pago;
import com.crediya.models.Prestamo;
import com.crediya.persistence.impl.PagoDAO;

import java.time.LocalDate;

public class PagoService {
    private final PagoDAO pagoDAO;

    public PagoService(PagoDAO pagoDAO) {
        this.pagoDAO = pagoDAO;
    }

    public boolean registrarPago(Prestamo prestamo, double monto) {
        Pago pago = new Pago(prestamo, LocalDate.now(), monto);
        return pagoDAO.registrarPago(pago);
    }
}
