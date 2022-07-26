package com.gmarquezp.springbootclientes.models.services;

import com.gmarquezp.springbootclientes.models.entities.Cliente;
import com.gmarquezp.springbootclientes.models.entities.Factura;
import com.gmarquezp.springbootclientes.models.entities.Producto;
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

    public List<Producto> findByNombre(String nombre);

    public void saveFactura(Factura factura);

    public Producto findProductoById(Long id);

    public Factura findFacturaById(Long id);

    public void deleteFactura(Factura factura);

    public Factura fetchWithClienteWithItemFacturaWithProducto(Long id);

    public Cliente fetchByIdWithFacturas(Long id);
}
