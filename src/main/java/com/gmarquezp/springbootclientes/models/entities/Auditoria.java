package com.gmarquezp.springbootclientes.models.entities;

// @Embeddable // Se podra utilizar en otras clases, similar a un trait en PHP

import javax.persistence.*;
import java.util.Date;

/*
 * Es util para separar logica de Entidades que se va a replicar en otras entidades
 * */
@Embeddable
public class Auditoria {
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP) // Para que se guarde en formato de fecha y hora
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;



    /*
     * Eventos que se ejecutan en el ciclo de vida de la entidad
     * Antes o despues de persistirce el objeto
     * */

    @PrePersist
    public void prePersist() {
        this.setCreatedAt(new Date());
        this.setUpdatedAt(new Date());
    }


    @PreUpdate
    public void preUpdate() {
        this.setUpdatedAt(new Date());
    }

    @PreRemove
    public void preRemove() {
        System.out.println("Inicializar algo justo antes de eliminar");
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}