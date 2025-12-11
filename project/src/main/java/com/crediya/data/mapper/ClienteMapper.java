package com.crediya.data.mapper;

import com.crediya.data.entities.ClienteEntity;
import com.crediya.domain.models.Cliente;

public class ClienteMapper {
    public static Cliente toModel(ClienteEntity entity){
        if (entity == null){
            return null;
        }

        Cliente model = new Cliente();

        model.setId(entity.getId());
        model.setNombre(entity.getNombre());
        model.setDocumento(entity.getDocumento());
        model.setCorreo(entity.getCorreo());
        model.setTelefono(entity.getTelefono());

        return model;
    }
    public static ClienteEntity toEntity(Cliente model) {
        if (model == null) return null;

        ClienteEntity entity = new ClienteEntity();

        entity.setId(model.getId());
        entity.setNombre(model.getNombre());
        entity.setDocumento(model.getDocumento());
        entity.setCorreo(model.getCorreo());
        entity.setTelefono(model.getTelefono());

        return entity;

    }




}
