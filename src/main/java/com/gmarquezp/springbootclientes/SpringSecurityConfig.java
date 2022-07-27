package com.gmarquezp.springbootclientes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean // Para que Spring lo reconozca como un Bean
    // Funcion que servira para usar el algoritmo de encriptacion de passwords
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
        PasswordEncoder encoder = passwordEncoder();
        // Especificando el algoritmo que usara para codificar las passwords
        UserBuilder users = User.builder().passwordEncoder(encoder::encode);

        // Creando usuarios en Memoria
        builder.inMemoryAuthentication()
                .withUser(users.username("admin").password("admin").roles("ADMIN"))
                .withUser(users.username("user").password("user").roles("USER"))
                .withUser(users.username("mix").password("mix").roles("USER", "ADMIN"));
    }

    /*
     * Funcion para configurar los accesos a las rutas
     * */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Interceptor en cada una de las rutas
        http.authorizeRequests()
                .antMatchers("/", "/css/**", "/js/**", "/images/**", "/clientes").permitAll() // Acceso sin autenticacion
                .antMatchers("/clientes/ver/**").hasRole("USER") // Acceso con rol USER
                .antMatchers("/uploads/**").hasRole("USER")
                .antMatchers("/clientes/crear/**").hasAnyRole("ADMIN") // .hasAnyRole() // multiples roles
                .antMatchers("/facturas/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .loginPage("/login") // Ruta de login, debe existir un controlador con esta ruta
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/error_403"); // Ruta de acceso denegado
        // .csrf().disable();

        System.out.println("*".repeat(100));
        System.out.println("Configurando Spring Security");
    }
}
