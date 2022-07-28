package com.gmarquezp.springbootclientes.controllers;

import com.gmarquezp.springbootclientes.models.dao.IClienteDao;
import com.gmarquezp.springbootclientes.models.entities.Cliente;
import com.gmarquezp.springbootclientes.models.services.IClienteService;
import com.gmarquezp.springbootclientes.models.services.IUploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping(value = "/clientes")
public class ClienteController {

    @Autowired // Inyecta el objeto de la clase que implemente la Interfaz IClienteDao
    @Qualifier("clienteService")
    private IClienteService clienteService;

    @Autowired
    private IUploadFileService uploadFileService;

    private final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @GetMapping({"", "/"})
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, // para poder recibir la pagina por param ?page=1
                         Model model,
                         Authentication authentication,
                         HttpServletRequest request) {

        // Opteniendo el contexto de autenticacion de forma estatica
        Authentication authenticationStatic = SecurityContextHolder.getContext().getAuthentication();

        // of(pagina_Actual, cantidad_registros_mostrar);
        Pageable pageable = PageRequest.of(page, 5);

        Page<Cliente> clientes = this.clienteService.findAll(pageable);


        // Todos los registros sin paginar
        // List<Cliente> clientes = this.clienteService.findAll();

        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes);
        System.out.println("Clientes: " + clientes.getContent());

        System.out.println("Autenticacion por parametro=\t" + authentication);
        System.out.println("Autenticacion estatica=\t" + authenticationStatic);


        // Validando el rol del usuario de forma programatica
        if (this.hasRole("ROLE_ADMIN")) {
            System.out.println("El usuario es administrador - Forma programatica");
        } else if (this.hasRole("ROLE_USER")) {
            System.out.println("El usuario es usuario - Forma programatica");
        } else {
            System.out.println("El usuario no esta autenticado - Forma programatica");
        }

        // Validando el rol del usuario con HttpRequest
        if (request.isUserInRole("ROLE_ADMIN")) {
            System.out.println("El usuario es administrador - Forma HttpRequest");
        } else if (request.isUserInRole("ROLE_USER")) {
            System.out.println("El usuario es usuario - Forma HttpRequest");
        } else {
            System.out.println("El usuario no esta autenticado - Forma HttpRequest");
        }

        return "clientes/listar";
    }

    // @PreAuthorize("hasAnyRole('ROLE_ADMIN')") // Solo para el rol admin
    @Secured("ROLE_ADMIN") // Solo para el rol admin
    @GetMapping({"/crear"})
    // Map<String, Object> model == Model model
    public String crear(Map<String, Object> model) {

        Cliente cliente = new Cliente();

        model.put("titulo", "Crear cliente");
        model.put("cliente", cliente);

        return "clientes/crear";
    }

    @PostMapping({"/crear"})
    // @Valid indicara que usara las validaciones sobre la entidad
    public String guardar(@Valid Cliente cliente,
                          BindingResult result,
                          Model model,
                          RedirectAttributes flash, // Para pasar mensajes entre vistas
                          @RequestParam(name = "fileFoto", required = false) MultipartFile foto // Para recibir el archivo
    ) {

        if (result.hasErrors()) {
            System.out.println(" === Error en el formulario === ");
            System.out.println(result.getAllErrors());
            model.addAttribute("titulo", "Crear cliente");
            flash.addFlashAttribute("messageDanger", "No se pudo crear");

            return "clientes/crear";
        }

        // Validando la foto
        if (!foto.isEmpty()) {

            String nombreArchivo = null;
            try {
                nombreArchivo = this.uploadFileService.store(foto);
            } catch (IOException e) {
                e.printStackTrace();
            }

            flash.addFlashAttribute("messageInfo", "Imagen subida: " + nombreArchivo);
            // Seteamos el nombre de la foto, para que se almacena solo el nombre en la BD
            cliente.setFoto(nombreArchivo);
        }

        System.out.println("Cliente a crear =\t" + cliente);
        this.clienteService.save(cliente);
        // Los mensajes flash se eliminan con el siguiente request
        flash.addFlashAttribute("messageSuccess", "Se creo exitosamente el cliente " + cliente.getNombre());
        return "redirect:/clientes";
    }


    @GetMapping({"/editar/{id}"})
    public String editar(@PathVariable(value = "id") Long id,
                         Model model,
                         RedirectAttributes flash
    ) {
        if (id > 0) {
            Cliente cliente = this.clienteService.findById(id);
            if (cliente != null) {
                model.addAttribute("titulo", "Editar cliente");
                model.addAttribute("cliente", cliente);
                flash.addFlashAttribute("messageSuccess", "Se actualizo exitosamente el cliente " + cliente.getNombre());

                return "clientes/crear";
            }
        }

        flash.addFlashAttribute("messageDanger", "No se pudo editar");

        return "redirect:/clientes";
    }

    @GetMapping({"/eliminar/{id}"})
    public String eliminar(@PathVariable(value = "id") Long id,
                           RedirectAttributes flash) {

        Cliente cliente = this.clienteService.findById(id);

        if (cliente != null && !cliente.getFoto().isBlank()) {

            logger.info("Eliminando la foto: " + cliente.getFoto());
            this.uploadFileService.delete(cliente.getFoto());

            this.clienteService.delete(id);
            flash.addFlashAttribute("messageSuccess", "Se elimino exitosamente el cliente");
        } else {
            flash.addFlashAttribute("messageDanger", "No se pudo eliminar");
        }

        return "redirect:/clientes";
    }

    @Secured("ROLE_USER") // Añade la seguridad al endpoint
    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id,
                      Model model,
                      RedirectAttributes flash) {

        // Cliente cliente = this.clienteService.findById(id);
        // Usando JPQL para traer el cliente con sus facturas en un solo query
        Cliente cliente = this.clienteService.fetchByIdWithFacturas(id);

        if (cliente == null) {

            flash.addFlashAttribute("messageDanger", "No se encontro usuario con ID:\t" + id);
            return "redirect:/clientes";


        }
        model.addAttribute("tutulo", "Detalle cliente");
        model.addAttribute("cliente", cliente);
        return "clientes/ver";
    }


    // .+ // Indica que soporta en el parametro la separacion por ., en caso contrario spring las trunca
    // Este endpoint retornara la imagen en Binario
    @GetMapping(value = "ver-foto/{nombreFoto:.+}")
    public ResponseEntity<Resource> verFotoRecurso(@PathVariable String nombreFoto) {

        Resource recurso = null;
        try {
            recurso = this.uploadFileService.load(nombreFoto);
        } catch (MalformedURLException e) {
            logger.error("Error al cargar la imagen: " + e.getMessage());
            e.printStackTrace();
        }

        // Respondemos el binario, modificando las cabeceras para que las reconozca el navegador
        // En el body enviamos el recurso
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);

    }


    private Boolean hasRole(String role) {
        // Obteniendo la autenticacion de forma estatica
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (securityContext == null) {
            return false;
        }

        Authentication authentication = securityContext.getAuthentication();

        if (authentication == null) {
            return false;
        }

        // Obteniendo los roles de la autenticacion
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        System.out.println("Authorities: " + authorities);


        // Forma declarativa recorriendo el rol
        /*for (GrantedAuthority authority : authorities) {
            if(authority.getAuthority().equals(role)){
                return true;
            }
        }*/

        return authorities.stream()
                // .filter(authority -> authority.getAuthority().equals(role))
                .anyMatch(authority -> authority.getAuthority().equals(role));


    }

}
