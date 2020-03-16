package com.daw.club.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity()       //JPA annotations
//@Table(name="Clientes")
public class Cliente implements Serializable {

    @Id         //JPA annotations
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto-incremental
    private Integer id;

    @Size(min = 4, max = 25, message = "La longitud ${validatedValue} debe estar entre {min} y {max} caracteres")
    private String nombre;
    @Pattern(regexp = "\\d{7,8}(-?[a-zA-Z])?", message = "{cliente.dni.formato}")
    private String dni;
    private boolean socio;

    public Cliente() {
        id = 0;
        nombre = "Desconocido";
        socio = false;
    }

    public Cliente(Integer id, String nombre, String dni, boolean socio) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.socio = socio;
    }

    /**
     * Copy constructor
     */
    public Cliente(Cliente c) {
        this.id = c.id;
        this.nombre = c.nombre;
        this.dni = c.dni;
        this.socio = c.socio;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the dni
     */
    public String getDni() {
        return dni;
    }

    /**
     * @param dni the dni to set
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * @return the socio
     */
    public boolean isSocio() {
        return socio;
    }

    /**
     * @param socio the socio to set
     */
    public void setSocio(boolean socio) {
        this.socio = socio;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.id;
        return hash;
    }

    @Override
    public String toString() {
        return "Cliente[ id=" + id + " ]";
    }

}
