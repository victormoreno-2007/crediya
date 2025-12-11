package com.crediya.service;

import java.time.LocalDate;

import com.crediya.models.Cliente;
import com.crediya.models.Empleado;
import com.crediya.models.Prestamo;
import com.crediya.persistence.impl.PrestamoDAO;

public class PrestamoService {
    
    private final MotorPrestamos motor;
    private final PrestamoDAO prestamoDAO;

    public PrestamoService(){
        this.motor = new MotorPrestamos();
        this.prestamoDAO = new PrestamoDAO();
    }

    public Prestamo crearPrestamo(Cliente cliente, Empleado empleado,
                                  double monto, double interesAnual, int cuotas){

        double cuota = motor.calcularCuota(monto, interesAnual, cuotas);
        double montoTotal = motor.calcularMontoTotal(cuota, cuotas);

        Prestamo p = new Prestamo();
        p.setCliente(cliente);
        p.setEmpleado(empleado);
        p.setMonto(monto);
        p.setInteres(interesAnual);
        p.setCuotas(cuotas);
        p.setCuotaMensual(cuota);    
        p.setMontoTotal(montoTotal);  
        p.setSaldoPendiente(montoTotal);  
        p.setFechaInicio(LocalDate.now());
        p.setEstado("PENDIENTE");

        prestamoDAO.guardarPrestamo(p); 

        return p;
    }
}

