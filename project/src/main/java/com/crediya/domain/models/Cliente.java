package com.crediya.domain.models;

public class Cliente extends Persona{
    private String telefono;

    public Cliente(){

    }

    public Cliente(int id, String nombre, String documento, String correo, String telefono) {
        super(id, nombre, documento, correo);
        this.telefono = telefono;
    }

    public Cliente(String nombre, String documento, String correo, String telefono) {
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
        return   "\tID: " + getId() + "\n" +
                 "\tNOMBRE: " + getNombre() + "\n" +
                 "\tDOCUMENTO: " + getDocumento()+ "\n" +
                 "\tCORREO: " + getCorreo() + "\n" +
                 "\tTELEFONO: " + getTelefono() + "\n" +
                "============================"+ "\n" + "\n";
    }

}
