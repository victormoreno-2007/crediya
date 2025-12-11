package com.crediya.domain.repository;

import com.crediya.domain.errors.ErrorDomain;
import com.crediya.domain.models.Pago;
import com.crediya.domain.response.ResponseDomain;

import java.util.List;

public interface PagoRepository {
    ResponseDomain<ErrorDomain, Integer> guardar(Pago pago);

    ResponseDomain<ErrorDomain, List<Pago>> listarPagos();
}
