package com.gmarquezp.springbootclientes.view.json;

import com.gmarquezp.springbootclientes.models.entities.Cliente;
import com.gmarquezp.springbootclientes.view.xml.ClienteList;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;


// Escuchara el Path /clientes?format=json
// Bean name, bean resolver
@Component("clientes/listar.json") // Se especifica la vista que retorna el endpoint
// MappingJackson2JsonView // permite mapear
public class ClienteListJsonView extends MappingJackson2JsonView {
    @Override
    protected Object filterModel(Map<String, Object> model) {
        // Elimianmos del model, los atributos que vienen del page Cliente innecesarios
        model.remove("titulo");
        model.remove("page");

        Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");

        model.remove("clientes");

        model.put("clientes", clientes.getContent());

        return super.filterModel(model);
    }
}
