package com.gmarquezp.springbootclientes.models.entities;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "facturas")
public class Factura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY => incremental
    private Long id;
    @NotEmpty
    private String descripcion;
    private String observacion;

    /*
    * Una factura tiene muchos items
    * */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // creara factura_id en facturas_items
    @JoinColumn(name = "factura_id") // Como no es inversa la relacion, se debe especificar el nombre de la columna foranea en la otra entidad
    List<ItemFactura> items;


    /*
     * ManyToOne
     * // Muchas Facturas pueden tener un Cliente
     * Many => facturas
     * One => cliente
     *
     * fetch = FetchType.LAZY // Para cargar el query unicamente cuando sea necesario
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente; // creara cliente_id

    @Embedded // Ahora se extienden todos los atributo de la clase @Embeddable en esta
    private Auditoria auditoria = new Auditoria();


    public Factura() {
        this.items = new ArrayList<>();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemFactura> getItems() {
        return items;
    }

    public void setItems(List<ItemFactura> items) {
        this.items = items;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public Factura addItem(ItemFactura item) {
        this.items.add(item);
        return this;
    }

    public Double getTotal(){
        Double total = 0D;
        for (ItemFactura item : this.getItems()) {
            total += item.calcularImporte();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", observacion='" + observacion + '\'' +
                ", createdAt=" + this.auditoria.getCreatedAt() +
                ", updatedAt=" + this.auditoria.getUpdatedAt() +
                ", cliente=" + cliente +
                '}';
    }
}
