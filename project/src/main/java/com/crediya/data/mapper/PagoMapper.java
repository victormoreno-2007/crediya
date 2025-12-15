package com.crediya.data.mapper;

import com.crediya.data.entities.PagoEntity;
import com.crediya.domain.models.Pago;
public class PagoMapper {
    public static PagoEntity toEntity(Pago pago) {
        if (pago == null) return null;

        PagoEntity entity = new PagoEntity();
        entity.setId(pago.getId());
        entity.setMonto(pago.getMonto());
        entity.setFechaPago(pago.getFechaPago());
        entity.setPrestamo(pago.getPrestamo());

        return entity;
    }

    public static Pago toModel(PagoEntity entity) {
        if (entity == null) return null;

        Pago pago = new Pago();
        pago.setId(entity.getId());
        pago.setMonto(entity.getMonto());
        pago.setFechaPago(entity.getFechaPago());

        pago.setPrestamo(entity.getPrestamo());

        return pago;
    }
}