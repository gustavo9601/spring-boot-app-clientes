package com.gmarquezp.springbootclientes.controllers;

import com.gmarquezp.springbootclientes.models.entities.Cliente;
import com.gmarquezp.springbootclientes.models.entities.Factura;
import com.gmarquezp.springbootclientes.models.entities.ItemFactura;
import com.gmarquezp.springbootclientes.models.entities.Producto;
import com.gmarquezp.springbootclientes.models.services.IClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@Secured({"ROLE_ADMIN"}) // Protegiendo el endpoint principal con el rol
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private IClienteService clienteService;

    private final Logger logger = LoggerFactory.getLogger(FacturaController.class);

    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value = "clienteId") Long clienteId,
                        RedirectAttributes flash,
                        Model model) {

        Cliente cliente = this.clienteService.findById(clienteId);

        if (cliente == null) {
            flash.addFlashAttribute("error", "Cliente no existe");
            return "redirect:/clientes";
        }

        Factura factura = new Factura();
        factura.setCliente(cliente); // Relacioando la factura con el cliente

        model.addAttribute("titulo", "Crear Factura");
        model.addAttribute("factura", factura);

        return "facturas/form";
    }


    /*
     * produces = {"application/json"} // Si se requiere rtornar un JSON se puede especificar en el produces el tipo de retorno
     * @ResponseBody // Indica que debe suprimir renderizar una vista, y usara el retorno especificado en el produces
     * */
    @GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
    public @ResponseBody List<Producto> cargarProductos(@PathVariable(value = "term") String term) {
        return this.clienteService.findByNombre(term);
    }


    @PostMapping("/form")
    public String guardar(@Valid Factura factura, // Mapea los campos a factura y con @Valid los valida de acuerdo a las anotaciones
                          BindingResult result, // devuelve el resultado de la validacion
                          @RequestParam(name = "item_id[]", required = false) Long[] itemsId, // Campos adicionales que se deben mapear por el request
                          @RequestParam(name = "cantidad[]", required = false) Integer[] cantidades,
                          @RequestParam(name = "cliente_id", required = true) Long clienteId,
                          @RequestParam Map<String, String> params, // Captura todo el request
                          RedirectAttributes flash,
                          Model model
    ) {


        logger.info("params " + params);

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Crear Factura");
            model.addAttribute("error", "Error al guardar");
            return "redirect:/facturas/form/" + clienteId;
        }

        for (int i = 0; i < itemsId.length; i++) {
            // Encontrando el producto por id
            Producto producto = this.clienteService.findProductoById(itemsId[i]);
            logger.info("Producto: " + producto);

            // Asociando el item o linea de factura con cantidad y producto
            ItemFactura item = new ItemFactura();
            item.setCantidad(cantidades[i]);
            item.setProducto(producto);
            logger.info("Item: " + item);

            // Agregando el item a la factura
            factura.addItem(item);

            // Asociando el cliente
            Cliente cliente = this.clienteService.findById(clienteId);
            factura.setCliente(cliente);
        }
        logger.info("Factura=\t" + factura);
        logger.info("Total factura=\t" + factura.getTotal());

        this.clienteService.saveFactura(factura);

        flash.addFlashAttribute("success", "Factura creada con éxito");
        return "redirect:/clientes/ver/" + factura.getCliente().getId();
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id,
                      RedirectAttributes flash,
                      Model model) {
        // Factura factura = this.clienteService.findFacturaById(id);
        // Usando con JPQL el query para hacer solo uno
        Factura factura = this.clienteService.fetchWithClienteWithItemFacturaWithProducto(id);
        logger.info("Factura encontrada=\t" + factura);

        if (factura == null) {
            flash.addFlashAttribute("error", "Factura no encontrada");
            return "redirect:/clientes";
        }

        model.addAttribute("titulo", "Factura: " + factura.getId());
        model.addAttribute("factura", factura);
        return "facturas/ver";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id,
                           RedirectAttributes flash,
                           Model model) {

        Factura factura = this.clienteService.findFacturaById(id);
        if (factura == null) {
            flash.addFlashAttribute("error", "Factura no encontrada");
            return "redirect:/clientes";
        }

        this.clienteService.deleteFactura(factura);
        model.addAttribute("titulo", "Facturas");
        flash.addFlashAttribute("success", "Factura eliminada");
        return "redirect:/clientes/ver/" + factura.getCliente().getId();
    }

}
