package com.gmarquezp.springbootclientes.models.services;

import com.gmarquezp.springbootclientes.controllers.ClienteController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

    private final Logger logger = LoggerFactory.getLogger(ClienteController.class);
    private static final String ROOT_PATH = "C://uploads";

    @Override
    public Resource load(String fileName) throws MalformedURLException {

        String uriPathCompleta = "file:/" + this.getPathCompleto(fileName);
        logger.info("pathCompleta = " + this.getPathCompleto(fileName));
        logger.info("uriPathCompleta: " + uriPathCompleta);

        Resource recurso = null;

        // Genera la url de recurso
        recurso = new UrlResource(uriPathCompleta);
        // Validacion si no se puede leer o no existe el archivo
        if (!recurso.exists() && !recurso.isReadable()) {
            logger.error("Error al cargar el recurso = " + uriPathCompleta);
            throw new RuntimeException("No se puede leer el archivo :" + uriPathCompleta);
        }

        return recurso;
    }

    @Override
    public String store(MultipartFile file) throws IOException {
        // Especificamos la ruta donde se subiran los archivos
        // Path path = Paths.get("src//main/resources/static/uploads");
        // System.out.println("path: " + path);

        // String rootPath = path.toFile().getAbsolutePath();
        // String rootPath = "C://uploads"; // Lo guardaremos en otra ruta que no sea la raiz del proyecto

        // Obtenemos el binario de la imagen
        byte[] fotoBytes = file.getBytes();
        String nombreArchivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String fullName = this.getPathCompleto(nombreArchivo);
        logger.info("fullName: " + fullName);

        Path pathCompleta = Paths.get(fullName);
        logger.info("pathCompleta: " + pathCompleta);

        // Alamcenando el arvhivo en la ruta
        Files.write(pathCompleta, fotoBytes);

        return nombreArchivo;

    }

    @Override
    public boolean delete(String fileName) {
        Path pathCompleta = Paths.get(this.getPathCompleto(fileName));
        File archivo = pathCompleta.toFile(); // Devuelve el archivo o directorio

        if (archivo.exists() && archivo.canRead()) {
            logger.info("Eliminando archivo: " + archivo.getName());
            if (archivo.delete()) {
                logger.info("Archivo eliminado =\t" + archivo.getName());
                return true;
            } else {
                logger.error("No se pudo eliminar el archivo: " + archivo.getName());
                return false;
            }
        }
        return false;
    }

    public String getPathCompleto(String fileName) {
        return ROOT_PATH + "/" + fileName;
    }

    @Override
    public void deleteAll() {
        logger.info("Eliminando todos los archivos");
        FileSystemUtils.deleteRecursively(Paths.get(ROOT_PATH).toFile());

        /*

        Forma manual

        File directorio = new File(ROOT_PATH);
        File[] listaArchivos = directorio.listFiles();
        for (File archivo : listaArchivos) {
            if (archivo.exists() && archivo.canRead()) {
                logger.info("Eliminando archivo: " + archivo.getName());
                if (archivo.delete()) {
                    logger.info("Archivo eliminado =\t" + archivo.getName());
                } else {
                    logger.error("No se pudo eliminar el archivo: " + archivo.getName());
                }
            }
        }*/
    }

    @Override
    public void initDirectory() {
        logger.info("Iniciando directorio");
        Path path = Paths.get(ROOT_PATH);
        if (!Files.exists(path)) {
            logger.info("Creando directorio");
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                logger.error("No se pudo crear el directorio");
            }
        }
    }
}
