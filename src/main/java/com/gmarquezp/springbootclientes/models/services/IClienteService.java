package com.gmarquezp.springbootclientes.models.services;

import com.gmarquezp.springbootclientes.models.entities.Cliente;

import java.util.List;

public interface IClienteService {

    public List<Cliente> findAll();

    public void save(Cliente cliente);

    public Cliente findById(Long id);

    public void delete(Long id);
}
