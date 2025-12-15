package com.crediya.data.entities;

import com.crediya.domain.models.Prestamo;

import java.time.LocalDate;

public class PagoEntity {
    private int id;
    private double monto;
    private LocalDate fechaPago;
    private Prestamo prestamo;


    public PagoEntity() {
    }


    public PagoEntity(int id, double monto, LocalDate fechaPago, Prestamo prestamo) {
        this.id = id;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.prestamo = prestamo;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }
}
