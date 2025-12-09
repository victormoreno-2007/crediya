package com.crediya.persistence;

import com.crediya.models.Clientes;

import java.util.List;

public interface ClienteRepositorio {
    void registrar(Clientes cliente);

    List<Clientes> listarTodos();

    Clientes buscarPorDocumento(String documento);
}
