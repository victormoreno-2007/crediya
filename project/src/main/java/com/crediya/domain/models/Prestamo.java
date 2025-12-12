package com.crediya.domain.models;

import java.time.LocalDate;

public class Prestamo {
    private int id;
    private double monto;
    private double interes;
    private int cuotas;
    private LocalDate fechaInicio;
    private String estado;
    private double saldoPendiente;
    private Cliente cliente;
    private Empleado empleado;

   
    private double montoTotal;
    private double cuotaMensual;

    public Prestamo() {
    }

    public Prestamo(int id, double monto, double interes, LocalDate fechaInicio,
                    int cuotas, String estado, double saldoPendiente,
                    Cliente cliente, Empleado empleado) {
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

    public Prestamo(double monto, double interes, int cuotas,
                    Cliente cliente, Empleado empleado) {
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
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

    public double getMontoTotal() {
        if (montoTotal == 0 && cuotas > 0) {
            this.montoTotal = getCuotaMensual() * cuotas;
            this.montoTotal = Math.round(this.montoTotal * 100.0) / 100.0;
        }
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public double getCuotaMensual() {
        if (cuotaMensual == 0 && cuotas > 0 && monto > 0) {
            double i = (interes / 100.0) / 12.0;
            if (i == 0) {
                this.cuotaMensual = monto / cuotas;
            } else {
                this.cuotaMensual = monto * (i * Math.pow(1 + i, cuotas)) / (Math.pow(1 + i, cuotas) - 1);
            }
            this.cuotaMensual = Math.round(this.cuotaMensual * 100.0) / 100.0;
        }
        return cuotaMensual;
    }

    public void setCuotaMensual(double cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }

    @Override
    public String toString() {
        String nombreCliente = (getCliente() != null) ? getCliente().getNombre() : "N/A";
        String nombreEmpleado = (getEmpleado() != null) ? getEmpleado().getNombre() : "N/A";
        return   "\tID: " + getId() + "\n" +
                "\tCLIENTE: " + nombreCliente + "\n" +
                "\tEMPLEADO: " + nombreEmpleado+ "\n" +
                "\tFECHA: "+ getFechaInicio() + "\n" +
                "\tESTADO: "+ getEstado() + "\n" +
                "\tMONTO PRESTADO: "+ getMonto() + "\n" +
                "\tCUOTAS: "+ getCuotas() + " X $"+ getCuotaMensual()+ "\n" +
                "\tTOTAL A PAGAR: "+ getMontoTotal()+ "\n"+
                "\tSALDO PENDIENTE: " + getSaldoPendiente() +
                "\n" + "============================"+ "\n" + "\n";
    }
}
