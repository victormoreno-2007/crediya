package com.crediya.repository;

import com.crediya.models.Cliente;

import java.util.List;

public interface ClienteRepository {
    void registrar(Cliente cliente);

    List<Cliente> listarTodos();

    Cliente buscarPorDocumento(String documento);
}
