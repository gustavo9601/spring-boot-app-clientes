package com.gmarquezp.springbootclientes.models.dao;

import com.gmarquezp.springbootclientes.models.entities.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


// CrudRepository<T, ID> // Abastrae metodos de crud
// JPARepository<T, ID>  || PagingAndSortingRepository// Iual que crud, pero trae exras para apginacion etc.
// No es necesaria la anotacion
public interface IClienteDaoRepository extends PagingAndSortingRepository<Cliente, Long> {


}
