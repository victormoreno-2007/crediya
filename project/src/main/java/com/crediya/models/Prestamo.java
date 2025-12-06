package com.crediya.models;

import java.time.LocalDate;

public class Prestamo {
    private int id;
    private double monto;
    private double interes;
    private int cuotas;
    private LocalDate fechaInicio;
    private String estado;
    private double saldoPendiente;
    private Clientes cliente;
    private Empleado empleado;

    public Prestamo() {
    }

    public Prestamo(int id, double monto, double interes, LocalDate fechaInicio, int cuotas, String estado, double saldoPendiente, Clientes cliente, Empleado empleado) {
        this.id = id;
        this.monto = monto;
        this.interes = interes;
        this.fechaInicio = fechaInicio;
        this.cuotas = cuotas;
        this.estado = estado;
        this.saldoPendiente = saldoPendiente;
        this.cliente = cliente;
        this.empleado = empleado;
    }

    public Prestamo(double monto, double interes, int cuotas, Clientes cliente, Empleado empleado){
        this.monto = monto;
        this.interes = interes;
        this.cuotas = cuotas;
        this.cliente = cliente;
        this.empleado = empleado;
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

    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public double getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setSaldoPendiente(double saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getCuotas() {
        return cuotas;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    @Override
    public String toString() {
        // Mostramos info útil, no direcciones de memoria
        return "Préstamo #" + id + " | Cliente: " + (cliente != null ? cliente.getNombre() : "N/A") + " | Saldo: $" + saldoPendiente;
    }

}
