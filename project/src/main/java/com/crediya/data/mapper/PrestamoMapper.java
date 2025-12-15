package com.crediya.data.mapper;

import com.crediya.data.entities.PrestamoEntity;
import com.crediya.domain.models.Cliente;
import com.crediya.domain.models.Empleado;
import com.crediya.domain.models.Prestamo;

public class PrestamoMapper {

    public static Prestamo toModel(PrestamoEntity entity){

        if (entity == null){
            return null;
        }
        Prestamo model = new Prestamo();

        model.setId(entity.getId());
        model.setMonto(entity.getMonto());
        model.setInteres(entity.getInteres());
        model.setCuotas(entity.getCuotas());
        model.setFechaInicio(entity.getFechaInicio());
        model.setEstado(entity.getEstado());
        model.setSaldoPendiente(entity.getSaldoPendiente());
        model.setCliente(entity.getCliente());
        model.setEmpleado(entity.getEmpleado());

        return model;
    }

    public static PrestamoEntity toEntity(Prestamo model) {
        if (model == null) return null;

        PrestamoEntity entity = new PrestamoEntity();

        entity.setId(model.getId());
        entity.setMonto(model.getMonto());
        entity.setInteres(model.getInteres());
        entity.setCuotas(model.getCuotas());
        entity.setFechaInicio(model.getFechaInicio());
        entity.setEstado(model.getEstado());
        entity.setSaldoPendiente(model.getSaldoPendiente());
        entity.setCliente(model.getCliente());
        entity.setEmpleado(model.getEmpleado());



        return entity;
    }
}
