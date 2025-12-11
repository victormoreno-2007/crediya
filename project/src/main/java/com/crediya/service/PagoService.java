// package com.crediya.service;

// import com.crediya.domain.models.Pago;
// import com.crediya.domain.models.Prestamo;
// import com.crediya.data.persistence.impl.PagoDAO;

// import java.time.LocalDate;

// public class PagoService {
//     private final PagoDAO pagoDAO;

//     public PagoService(PagoDAO pagoDAO) {
//         this.pagoDAO = pagoDAO;
//     }

//     public boolean registrarPago(Prestamo prestamo, double monto) {
//         Pago pago = new Pago(prestamo, LocalDate.now(), monto);
//         return pagoDAO.registrarPago(pago);
//     }
// }
