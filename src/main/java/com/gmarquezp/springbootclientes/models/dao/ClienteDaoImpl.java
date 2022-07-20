package com.gmarquezp.springbootclientes.models.dao;

import com.gmarquezp.springbootclientes.models.entities.Cliente;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository // Marca que es una clase de persistencia
@Primary // Da prioridad al momento de inyectar interfaces
public class ClienteDaoImpl implements IClienteDao {

    // EntityManager es una clase que se encarga de la persistencia de datos
    @PersistenceContext // Inyecta la dependencia de EntityManager
    private EntityManager entityManager;

    @Override
    @Transactional() // Envuelve la consulta dentro de una transaccion
    public List<Cliente> findAll() {
        return this.entityManager
                .createQuery("SELECT cliente from Cliente cliente", Cliente.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void save(Cliente cliente) {
        // persist(obj) => Almacena el objeto en la base de datos
        // merge(obj) => Si el objeto ya existe en la base de datos, lo actualiza, sino lo crea
        // remove(obj) => Elimina el objeto de la base de datos

        this.entityManager.persist(cliente);
    }
}
