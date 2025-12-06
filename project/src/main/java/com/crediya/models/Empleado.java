package com.crediya.models;

public class Empleado extends Persona {
    private String rol;
    private double salario;

    public Empleado(){

    }

    public Empleado(int id, String nombre, String documento, String correo, String rol, double salario) {
        super(id, nombre, documento, correo);
        this.rol = rol;
        this.salario = salario;
    }

    public Empleado( String nombre, String documento, String correo, String rol, double salario) {
        super(0, nombre, documento, correo);
        this.rol = rol;
        this.salario = salario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "rol='" + rol + '\'' +
                '}';
    }
}
