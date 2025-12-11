package com.crediya.data.mapper;

import com.crediya.data.entities.PrestamoEntity;
import com.crediya.models.Cliente;
import com.crediya.models.Empleado;
import com.crediya.models.Prestamo;

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

        if (entity.getClienteId() != null ) {
            Cliente cliente = new Cliente(); // O new Clientes()
            cliente.setId(entity.getClienteId());
            model.setCliente(cliente);
        }

        if (entity.getEmpleadoId() != null) {
            Empleado empleado = new Empleado();
            empleado.setId(entity.getEmpleadoId());
            model.setEmpleado(empleado);
        }
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


        if (model.getCliente() != null) {
            entity.setClienteId(model.getCliente().getId());
        }

        if (model.getEmpleado() != null) {
            entity.setEmpleadoId(model.getEmpleado().getId());
        }

        return entity;
    }
}
