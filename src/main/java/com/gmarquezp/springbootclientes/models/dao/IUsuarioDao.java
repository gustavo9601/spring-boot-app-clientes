package com.gmarquezp.springbootclientes.models.dao;

import com.gmarquezp.springbootclientes.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IUsuarioDao extends JpaRepository<Usuario, Long> {

    // Por debajo ejecitara la consulta JPQL
    public Usuario findByUsername(String username);

}
