package com.crediya.data.mapper;

import com.crediya.data.entities.EmpleadoEntity;
import com.crediya.domain.models.Empleado;

public class EmpleadoMapper {
    public static Empleado toModel(EmpleadoEntity entity){
        if (entity == null){
            return null;
        }

        Empleado model = new Empleado();

        model.setId(entity.getId());
        model.setNombre(entity.getNombre());
        model.setDocumento(entity.getDocumento());
        model.setRol(entity.getRol());
        model.setCorreo(entity.getCorreo());
        model.setSalario(entity.getSalario());

        return  model;

    }

    public static EmpleadoEntity toEntity(Empleado model){
        if (model == null) {
            return null;
        }

        EmpleadoEntity entity = new EmpleadoEntity();

        entity.setId(model.getId());
        entity.setNombre(model.getNombre());
        entity.setDocumento(model.getDocumento());
        entity.setRol(model.getRol());
        entity.setCorreo(model.getCorreo());
        entity.setSalario(model.getSalario());

        return entity;

    }

}
