package com.crediya.models;

public class Clientes extends Persona{
    private String telefono;

    public Clientes(){
        super();
    }

    public Clientes(int id, String nombre, String documento, String correo, String telefono) {
        super(id, nombre, documento, correo);
        this.telefono = telefono;
    }

    public Clientes(String nombre, String documento, String correo, String telefono) {
        super(0, nombre, documento, correo);
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return super.toString() + " - Tel: " + telefono;
    }
}
