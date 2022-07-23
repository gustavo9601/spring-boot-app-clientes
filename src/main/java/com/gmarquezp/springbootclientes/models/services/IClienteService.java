package com.gmarquezp.springbootclientes.models.services;

import com.gmarquezp.springbootclientes.models.entities.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteService {

    public List<Cliente> findAll();

    // Permitira tener registros paginados
    public Page<Cliente> findAll(Pageable pageable);

    public void save(Cliente cliente);

    public Cliente findById(Long id);

    public void delete(Long id);


}
