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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
        Pageable pageable = PageRequest.of(page, 2);

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
                          RedirectAttributes flash // Para pasar mensajes entre vistas
    ) {

        if (result.hasErrors()) {
            System.out.println(" === Error en el formulario === ");
            System.out.println(result.getAllErrors());
            model.addAttribute("titulo", "Crear cliente");
            flash.addFlashAttribute("messageDanger", "No se pudo crear");

            return "clientes/crear";
        }

        // Los mensajes flash se eliminan con el siguiente request
        flash.addFlashAttribute("messageSuccess", "Se creo exitosamente el cliente " + cliente.getNombre());
        this.clienteService.save(cliente);
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

}
