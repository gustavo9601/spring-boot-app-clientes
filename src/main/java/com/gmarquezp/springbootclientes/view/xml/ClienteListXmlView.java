package com.gmarquezp.springbootclientes.view.xml;

import com.gmarquezp.springbootclientes.models.entities.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


// @Component
public class ClienteListXmlView extends MarshallingView {

   /* @Autowired
    public ClienteListXmlView(Jaxb2Marshaller marshaller) {
        super(marshaller);
    }*/

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        // Se eliminan atributos que vienen del page Cliente
        model.remove("titulo");
        model.remove("page");


        Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");

        model.remove("clientes");

        model.put("clienteList", new ClienteList(clientes.getContent()));


        super.renderMergedOutputModel(model, request, response);
    }

}
