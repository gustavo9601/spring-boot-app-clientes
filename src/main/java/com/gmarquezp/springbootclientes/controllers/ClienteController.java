package com.gmarquezp.springbootclientes.controllers;

import com.gmarquezp.springbootclientes.models.dao.IClienteDao;
import com.gmarquezp.springbootclientes.models.entities.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/clientes")
public class ClienteController {

    @Autowired // Inyecta el objeto de la clase que implemente la Interfaz IClienteDao
    private IClienteDao clienteDao;


    @GetMapping({"", "/"})
    public String listar(Model model) {

        List<Cliente> clientes = this.clienteDao.findAll();

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
                          Model model
                          ) {

        if(result.hasErrors()){
            System.out.println(" === Error en el formulario === ");
            System.out.println(result.getAllErrors());
            model.addAttribute("titulo", "Crear cliente");
            return "clientes/crear";
        }

        this.clienteDao.save(cliente);
        return "redirect:/clientes";
    }
}
