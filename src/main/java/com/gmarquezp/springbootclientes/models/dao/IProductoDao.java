package com.gmarquezp.springbootclientes.models.dao;

import com.gmarquezp.springbootclientes.models.entities.Producto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProductoDao extends CrudRepository<Producto, Long> {
    /*
     * Consulta personalizada recibiendo un parametro
     * */
    @Query("select p from Producto p where p.nombre like %?1%")
    public List<Producto> findByNombre(String nombre);

    /*
    * Consulta usando Spring Data, unicamente nombrando correctamente la funcion
    * */
    public List<Producto> findByNombreLikeIgnoreCase(String nombre);

}
