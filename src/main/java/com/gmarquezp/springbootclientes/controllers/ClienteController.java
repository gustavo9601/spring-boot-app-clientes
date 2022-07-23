package com.gmarquezp.springbootclientes.controllers;

import com.gmarquezp.springbootclientes.models.dao.IClienteDao;
import com.gmarquezp.springbootclientes.models.entities.Cliente;
import com.gmarquezp.springbootclientes.models.services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/clientes")
public class ClienteController {

    @Autowired // Inyecta el objeto de la clase que implemente la Interfaz IClienteDao
    @Qualifier("clienteService")
    private IClienteService clienteService;


    @GetMapping({"", "/"})
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page // para poder recibir la pagina por param ?page=1
            , Model model) {

        // of(pagina_Actual, cantidad_registros_mostrar);
        Pageable pageable = PageRequest.of(page, 5);

        Page<Cliente> clientes = this.clienteService.findAll(pageable);


        // Todos los registros sin paginar
        // List<Cliente> clientes = this.clienteService.findAll();

        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes);

        return "clientes/listar";
    }

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
            System.out.println("=".repeat(50));
            System.out.println("Subiendo archivo");
            // Especificamos la ruta donde se subiran los archivos
            Path path = Paths.get("src//main/resources/static/uploads");
            System.out.println("path: " + path);

            String rootPath = path.toFile().getAbsolutePath();
            System.out.println("rootPath = " + rootPath);

            try {
                // Obtenemos el binario de la imagen
                byte[] fotoBytes = foto.getBytes();
                Path pathCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
                System.out.println("pathCompleta = " + pathCompleta);

                // Alamcenando el arvhivo en la ruta
                Files.write(pathCompleta, fotoBytes);
                flash.addFlashAttribute("messageInfo", "Imagen subida: " + foto.getOriginalFilename());

                // Seteamos el nombre de la foto, para que se almacena solo el nombre en la BD
                cliente.setFoto(foto.getOriginalFilename());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

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
        if (id > 0) {
            this.clienteService.delete(id);
            flash.addFlashAttribute("messageSuccess", "Se elimino exitosamente el cliente");
        } else {
            flash.addFlashAttribute("messageDanger", "No se pudo eliminar");
        }

        return "redirect:/clientes";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id,
                      Model model,
                      RedirectAttributes flash) {

        Cliente cliente = this.clienteService.findById(id);

        if (cliente == null) {

            flash.addFlashAttribute("messageDanger", "No se encontro usuario con ID:\t" + id);
            return "redirect:/clientes";


        }
        model.addAttribute("tutulo", "Detalle cliente");
        model.addAttribute("cliente", cliente);
        return "clientes/ver";
    }

}
