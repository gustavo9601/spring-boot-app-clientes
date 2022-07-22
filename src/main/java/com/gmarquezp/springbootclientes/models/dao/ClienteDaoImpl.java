package com.gmarquezp.springbootclientes.models.dao;

import com.gmarquezp.springbootclientes.models.entities.Cliente;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository // Marca la clase, para poder ser inyectada y tenga una adecuada traduccion de errores o exepciones
@Primary // Da prioridad al momento de inyectar interfaces
public class ClienteDaoImpl implements IClienteDao {

    // EntityManager es una clase que se encarga de la persistencia de datos
    @PersistenceContext // Inyecta la dependencia de EntityManager
    private EntityManager entityManager;

    @Override
    public List<Cliente> findAll() {
        return this.entityManager
                .createQuery("SELECT cliente from Cliente cliente", Cliente.class)
                .getResultList();
    }

    @Override
    public void save(Cliente cliente) {
        // persist(obj) => Almacena el objeto en la base de datos
        // merge(obj) => Si el objeto ya existe en la base de datos, lo actualiza, sino lo crea
        // remove(obj) => Elimina el objeto de la base de datos
        System.out.println("Cliente =\t" + cliente);
        if (cliente.getId() != null) {
            this.entityManager.merge(cliente);
        } else {
            this.entityManager.persist(cliente);
        }
    }

    @Override
    public Cliente findById(Long id) {
        return this.entityManager.find(Cliente.class, id);
    }

    @Override
    public void delete(Long id) {
        Cliente cliente = this.entityManager.find(Cliente.class, id);
        this.entityManager.remove(cliente);
    }
}
