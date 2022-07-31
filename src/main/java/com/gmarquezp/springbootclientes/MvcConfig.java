package com.gmarquezp.springbootclientes;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration // Para que al scaneer se ejecute en la Configuracion
public class MvcConfig implements WebMvcConfigurer {


    private Logger logger = org.slf4j.LoggerFactory.getLogger(MvcConfig.class);

    /*
     * Funcion para añadir handler al resolver recursos
     * */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        registry.addResourceHandler("/uploads/**") // Ruta que se usara en las vistas
                .addResourceLocations("file:/C://uploads/"); // Equivalencia o mapeo de la ruta en el servidor local
    }

    /*
     * Funcion que permite añadir o homologar el funcionamiento de una ruta a una vista
     * */
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error_403").setViewName("errores/403");
    }


    /*
     * Se ejecutara para setear el lenguage del proyecto
     * */
    @Bean
    public LocaleResolver localeResolver() {
        // El resolver se guardara en la sesion con cada cambio
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        // Seteamos el lenguage por defecto
        sessionLocaleResolver.setDefaultLocale(new Locale("es", "ES"));
        logger.info("Se ha seteado el lenguage por defecto a español");
        return sessionLocaleResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        // Interceptor que se ejecutara cuando se cambie el lenguage
        localeChangeInterceptor.setParamName("lang"); // ?lang=  // sera el parametro por url
        return localeChangeInterceptor;
    }

    /*@Bean // Bean para añadir el comportamiendo de XML, para serializar el objeto
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        // ClienteListXmlView // Clase wrapper, que envuelva el objeto para poderlo serializar correctamente
        jaxb2Marshaller.setClassesToBeBound(new Class[] {ClienteListXmlView.class});
        return jaxb2Marshaller;
    }*/

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Registrando el interceptor para el cambio de lenguaje
        registry.addInterceptor(localeChangeInterceptor());
    }
}
