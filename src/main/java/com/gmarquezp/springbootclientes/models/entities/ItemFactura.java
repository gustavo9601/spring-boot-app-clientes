package com.gmarquezp.springbootclientes.models.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "facturas_items")
public class ItemFactura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;

    /*
    * Un producto tiene muchos item id
    * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto; // Creara el producto_id

    @Embedded
    private Auditoria auditoria = new Auditoria();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Double calcularImporte(){
        return this.getCantidad() * this.producto.getPrecio();
    }


    @Override
    public String toString() {
        return "ItemFactura{" +
                "id=" + id +
                ", createdAt=" + this.auditoria.getCreatedAt() +
                ", updatedAt=" + this.auditoria.getUpdatedAt() +
                '}';
    }
}
