package com.gmarquezp.springbootclientes.models.dao;

import com.gmarquezp.springbootclientes.models.entities.Factura;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IFacturaDao extends CrudRepository<Factura, Long> {

    /*
    * Usando JPQL para obtener el query en uno solo
    *  join fetch, permite usar las relaciones definidias en la entidad
    * */
    @Query("SELECT f FROM Factura f " +
            "join fetch f.cliente c " +
            "join fetch f.items l " +
            "join  fetch  l.producto " +
            "where f.id = ?1")
    public Factura fetchWithClienteWithItemFacturaWithProducto(Long id);

}
