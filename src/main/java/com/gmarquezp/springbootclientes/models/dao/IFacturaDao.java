package com.gmarquezp.springbootclientes.models.dao;

import com.gmarquezp.springbootclientes.models.entities.Factura;
import org.springframework.data.repository.CrudRepository;

public interface IFacturaDao extends CrudRepository<Factura, Long> {

}
