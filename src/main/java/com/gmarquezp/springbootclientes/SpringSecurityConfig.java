package com.gmarquezp.springbootclientes;

import com.gmarquezp.springbootclientes.models.services.JpaUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
// Anotacion para habilitar el Secured y PreAuthorize, para asegurar un controllador o endpoint
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean // Para que Spring lo reconozca como un Bean
    // Funcion que servira para usar el algoritmo de encriptacion de passwords
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Autowired // Handler success personalizado
    private SimpleUrlAuthenticationSuccessHandler successHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("jpaUserDetailsService")
    private JpaUserDetailService jpaUserDetailsService;

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {

        /*
        * 1. Implementacion personalizada con el servicio de autenticacion
        * */

        builder.userDetailsService(this.jpaUserDetailsService);


        /*
        * 2. Implementacion con JDBC
        * */
  /*      builder.jdbcAuthentication()
                .dataSource(this.dataSource) // Definidnimos la conexion
                .passwordEncoder(passwordEncoder()) // Definimos el algoritmo de encriptacion de passwords
                // Consultas que se ejecutaran de forma automatica para comparar el login
                .usersByUsernameQuery("select username, password, enabled from users where username=?")
                .authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id = u.id)  where u.username=?");
*/

        /*
        * 3. Implementacion con usuarios en Memoria
        * */
        /*
        PasswordEncoder encoder = passwordEncoder();
        // Especificando el algoritmo que usara para codificar las passwords
        UserBuilder users = User.builder().passwordEncoder(encoder::encode);

        // Creando usuarios en Memoria
        builder.inMemoryAuthentication()
                .withUser(users.username("admin").password("admin").roles("ADMIN"))
                .withUser(users.username("user").password("user").roles("USER"))
                .withUser(users.username("mix").password("mix").roles("USER", "ADMIN"));*/


    }

    /*
     * Funcion para configurar los accesos a las rutas
     * */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Interceptor en cada una de las rutas
        http.authorizeRequests()
                .antMatchers("/", "/css/**", "/js/**", "/images/**", "/clientes", "/locale").permitAll() // Acceso sin autenticacion
                .antMatchers("/uploads/**").hasRole("USER")
                // Se usaran anotaciones en los controladores para autorizar o no una accion @Secured y @PreAuthorize
                // .antMatchers("/clientes/ver/**").hasRole("USER") // Acceso con rol USER
                // .antMatchers("/clientes/crear/**").hasAnyRole("ADMIN") // .hasAnyRole() // multiples roles
                // .antMatchers("/facturas/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(this.successHandler) // Especificando el comportamiendo cuando sea satisfactorio el login
                .permitAll()
                .loginPage("/login") // Ruta de login, debe existir un controlador con esta ruta
                .defaultSuccessUrl("/clientes", true) // Ruta de redireccionamiento cuando se loguee correctamente
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/error_403"); // Ruta de acceso denegado
        // .csrf().disable();

        System.out.println("*".repeat(100));
        System.out.println("Configurando Spring Security");
    }
}
