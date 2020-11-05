package ar.edu.um.fincasapp.fincasms.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import ar.edu.um.fincasapp.fincasms.domain.enumeration.Departamento;

/**
 * A Ubicacion.
 */
@Entity
@Table(name = "ubicacion")
public class Ubicacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "departamento", nullable = false)
    private Departamento departamento;

    @Column(name = "calle")
    private String calle;

    @Column(name = "numero")
    private Long numero;

    @Column(name = "descripcion")
    private String descripcion;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public Ubicacion departamento(Departamento departamento) {
        this.departamento = departamento;
        return this;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getCalle() {
        return calle;
    }

    public Ubicacion calle(String calle) {
        this.calle = calle;
        return this;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public Long getNumero() {
        return numero;
    }

    public Ubicacion numero(Long numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Ubicacion descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ubicacion)) {
            return false;
        }
        return id != null && id.equals(((Ubicacion) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ubicacion{" +
            "id=" + getId() +
            ", departamento='" + getDepartamento() + "'" +
            ", calle='" + getCalle() + "'" +
            ", numero=" + getNumero() +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
