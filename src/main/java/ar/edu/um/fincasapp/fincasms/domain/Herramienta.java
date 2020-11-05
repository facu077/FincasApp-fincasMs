package ar.edu.um.fincasapp.fincasms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import ar.edu.um.fincasapp.fincasms.domain.enumeration.TipoHerramienta;

/**
 * A Herramienta.
 */
@Entity
@Table(name = "herramienta")
public class Herramienta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoHerramienta tipo;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne
    @JsonIgnoreProperties(value = "herramientas", allowSetters = true)
    private Finca finca;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Herramienta nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoHerramienta getTipo() {
        return tipo;
    }

    public Herramienta tipo(TipoHerramienta tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(TipoHerramienta tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Herramienta descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Finca getFinca() {
        return finca;
    }

    public Herramienta finca(Finca finca) {
        this.finca = finca;
        return this;
    }

    public void setFinca(Finca finca) {
        this.finca = finca;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Herramienta)) {
            return false;
        }
        return id != null && id.equals(((Herramienta) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Herramienta{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
