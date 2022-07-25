package com.gmarquezp.springbootclientes;

import com.gmarquezp.springbootclientes.models.services.IUploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootClientesApplication implements CommandLineRunner {


    @Autowired
    IUploadFileService uploadFileService;

    private final Logger logger = LoggerFactory.getLogger(SpringBootClientesApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootClientesApplication.class, args);
    }

    // Implementando la interfaz CommandLineRunner, y en run se ejecutara cada vez que se inicialice el servicio
    @Override
    public void run(String... args) throws Exception {
        logger.info("Eliminando todos los archivos del directorio uploads");
        uploadFileService.deleteAll();
        logger.info("Inicializando directorio");
        uploadFileService.initDirectory();
    }
}
