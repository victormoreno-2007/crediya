package com.crediya.domain.repository;

import com.crediya.domain.errors.ErrorDomain;
import com.crediya.domain.models.Cliente;
import com.crediya.domain.response.ResponseDomain;

import java.util.List;

public interface ClienteRepository {
    ResponseDomain<ErrorDomain, Integer> registrar(Cliente cliente);

    ResponseDomain<ErrorDomain, List<Cliente>> listarTodos();

    ResponseDomain<ErrorDomain, Cliente> buscarPorDocumento(String documento);
}
