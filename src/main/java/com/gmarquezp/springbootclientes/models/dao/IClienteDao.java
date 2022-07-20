package com.gmarquezp.springbootclientes.models.dao;

import com.gmarquezp.springbootclientes.models.entities.Cliente;

import java.util.List;

public interface IClienteDao {

    public List<Cliente> findAll();

    public void save (Cliente cliente);

}
