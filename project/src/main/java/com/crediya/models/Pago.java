package com.crediya.models;

import java.time.LocalDate;

public class Pago {
    private int id;
    private int prestamoId;
    private double monto;
    private LocalDate fechaPago;

    public Pago(){

    }

    public Pago(int id, int prestamoId, double monto, LocalDate fechaPago) {
        this.id = id;
        this.prestamoId = prestamoId;
        this.monto = monto;
        this.fechaPago = fechaPago;
    }

    public Pago(int prestamoId, double monto, LocalDate fechaPago) {
        this.prestamoId = prestamoId;
        this.monto = monto;
        this.fechaPago = fechaPago;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(int prestamoId) {
        this.prestamoId = prestamoId;
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

    @Override
    public String toString() {
        return "Pago #" + id + " | Valor: $" + monto + " | Fecha: " + fechaPago;
    }

}
