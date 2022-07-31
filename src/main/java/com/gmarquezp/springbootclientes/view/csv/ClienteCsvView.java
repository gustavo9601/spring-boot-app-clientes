package com.gmarquezp.springbootclientes.view.csv;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmarquezp.springbootclientes.models.entities.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

// Escuchara el Path /clientes?format=csv
// Bean name, bean resolver
@Component("clientes/listar") // Se especifica la vista que retorna el endpoint
public class ClienteCsvView extends AbstractView {

    // Mdoifica el mime type del archivo a exportar
    public ClienteCsvView() {
        setContentType("text/csv");
    }

    @Override // Para que permita generar contenido descargable
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {

        // Modifica los headers y el nombre del archivo
        response.setHeader("Content-Disposition", "attachment; filename=\"clientes.csv\"");
        response.setContentType(getContentType());


        Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");

        // Especifica el formato, separador, etc del archivo a generar
        ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] header = {"id", "nombre", "apellido", "email"}; // Se debe llamar extactamente que el Objeto
        beanWriter.writeHeader(header);

        for (Cliente cliente : clientes) {
            beanWriter.write(cliente, header);
        }

        beanWriter.close();
    }

}
