package com.gmarquezp.springbootclientes.view.xml;

import com.gmarquezp.springbootclientes.models.entities.Cliente;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

// Enlace simbolico del objeto root para el XML
@XmlRootElement(name = "clientes") // <clientes></clientes>
public class ClienteList {

    /*
    * Es en singular ya que especificara un arreglo de etiquetas de clientes
    *
    * <cliente> content </cliente>
    * */
    @XmlElement(name = "cliente")
    public List<Cliente> clientes;

    public ClienteList() {
    }

    public ClienteList(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
}
