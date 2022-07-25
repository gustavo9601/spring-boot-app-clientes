package com.gmarquezp.springbootclientes.models.services;

import com.gmarquezp.springbootclientes.models.dao.IClienteDaoRepository;
import com.gmarquezp.springbootclientes.models.dao.IFacturaDao;
import com.gmarquezp.springbootclientes.models.dao.IProductoDao;
import com.gmarquezp.springbootclientes.models.entities.Cliente;
import com.gmarquezp.springbootclientes.models.entities.Factura;
import com.gmarquezp.springbootclientes.models.entities.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("clienteService")
public class ClienteServiceImpl implements IClienteService {

    // Usando la interfaz por default Crud repository
    @Autowired
    private IClienteDaoRepository clienteDaoRepository;

    @Autowired
    private IProductoDao productoDao;

    @Autowired
    IFacturaDao facturaDao;

    @Override
    @Transactional(readOnly = true) // Envuelve la consulta dentro de una transaccion, de solo lectura

    public List<Cliente> findAll() {
        return (List<Cliente>) this.clienteDaoRepository.findAll();
    }

    @Override
    @Transactional
    public void save(Cliente cliente) {
        this.clienteDaoRepository.save(cliente);
    }

    @Override
    @Transactional
    public Cliente findById(Long id) {
        return this.clienteDaoRepository.findById(id)
                .orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.clienteDaoRepository.deleteById(id);
    }


    @Override
    @Transactional
    public Page<Cliente> findAll(Pageable pageable) {
        return this.clienteDaoRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public List<Producto> findByNombre(String nombre) {
        return this.productoDao.findByNombre(nombre);
    }

    @Override
    @Transactional
    public void saveFactura(Factura factura) {
        this.facturaDao.save(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findProductoById(Long id) {
        return this.productoDao.findById(id).orElse(null);
    }
}
