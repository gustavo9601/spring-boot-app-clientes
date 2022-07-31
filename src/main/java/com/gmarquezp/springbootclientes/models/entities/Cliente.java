package com.gmarquezp.springbootclientes.models.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "clientes")
// Se serializa si se requiere guardar en memoria el objeto en bytes
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 1, max = 200)
    private String nombre;
    @NotEmpty
    private String apellido;
    @NotEmpty
    @Email
    private String email;

    private String foto;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fecha;


    @Embedded // Ahora se extienden todos los atributo de la clase @Embeddable en esta
    private Auditoria auditoria = new Auditoria();

    /*
     * @OneToMany
     * // Un cliente tiene muchas facturas
     * One => Cliente
     * Many => Facturas
     * fetch = FetchType.LAZY // Para cargar el query unicamente cuando sea necesario
     * cascade = CascadeType.ALL // Operaciones en casacada => Si se elimina un cliente se eliminan todas sus facturas
     *
     * // Para que la relacion sea de una forma inversa (Bidireccional), es decir, que la factura tenga un cliente
     * // Debe ser el nombre en minuscula de la relacion en la otra entidad, para que en Factura quede la llave foranea
     * mappedBy = "cliente"
     * */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cliente")
    // @JsonIgnore // Para que no se muestre el objeto en el JSON
    @JsonManagedReference // Para que no se muestre el objeto en el JSON
            List<Factura> facturas;


    public Cliente() {
        this.facturas = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public Cliente addFactura(Factura factura) {
        facturas.add(factura);
        return this;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", foto='" + this.getFoto() + '\'' +
                ", fecha=" + fecha +
                ", createdAt=" + this.auditoria.getCreatedAt() +
                ", updatedAt=" + this.auditoria.getUpdatedAt() +
                '}';
    }
}
