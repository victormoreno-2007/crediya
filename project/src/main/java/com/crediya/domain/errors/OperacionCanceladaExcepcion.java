package com.crediya.domain.errors;

public class OperacionCanceladaExcepcion extends RuntimeException{
    public OperacionCanceladaExcepcion(){
        super("Operacion cancelada por el usuario");
    }

}
