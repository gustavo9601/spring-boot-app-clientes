package com.gmarquezp.springbootclientes.models.services;

import com.gmarquezp.springbootclientes.models.dao.IUsuarioDao;
import com.gmarquezp.springbootclientes.models.entities.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("jpaUserDetailsService")
// Clase que personalizara la forma de validar la autenticacion que venia por default en Spring Security
public class JpaUserDetailService implements UserDetailsService {

    @Autowired
    IUsuarioDao usuarioDao;

    Logger logger = LoggerFactory.getLogger(JpaUserDetailService.class);

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = this.usuarioDao.findByUsername(username);

        logger.info("Usuario: " + usuario);

        if (usuario == null) {
            logger.error("Usuario no encontrado");
            throw new UsernameNotFoundException("El usuario no existe");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        // Modelamos los roles encontrados, y los pusehamos a authorities
        usuario.getRoles()
                .forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
                });

        if(authorities.isEmpty()){
            logger.error("Usuario No tiene roles");
            throw new UsernameNotFoundException("El usuario no tiene roles asignados");
        }

        // Retornamos el usuario propio de Spring Security
        return new User(usuario.getUsername(), usuario.getPassword(), authorities);
    }
}
