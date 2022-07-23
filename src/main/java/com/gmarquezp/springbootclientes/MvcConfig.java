package com.gmarquezp.springbootclientes;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Para que al scaneer se ejecute en la Configuracion
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        registry.addResourceHandler("/uploads/**") // Ruta que se usara en las vistas
                .addResourceLocations("file:/C://uploads/"); // Equivalencia o mapeo de la ruta en el servidor local
    }
}
