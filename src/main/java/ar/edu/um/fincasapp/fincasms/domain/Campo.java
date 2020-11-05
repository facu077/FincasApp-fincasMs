package ar.edu.um.fincasapp.fincasms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Campo.
 */
@Entity
@Table(name = "campo")
public class Campo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "sembrado")
    private Boolean sembrado;

    @Column(name = "fecha_plantado")
    private Instant fechaPlantado;

    @Column(name = "fecha_cosecha")
    private Instant fechaCosecha;

    @Column(name = "producto")
    private String producto;

    @Column(name = "tamano")
    private Long tamano;

    @ManyToOne
    @JsonIgnoreProperties(value = "campos", allowSetters = true)
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

    public Campo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isSembrado() {
        return sembrado;
    }

    public Campo sembrado(Boolean sembrado) {
        this.sembrado = sembrado;
        return this;
    }

    public void setSembrado(Boolean sembrado) {
        this.sembrado = sembrado;
    }

    public Instant getFechaPlantado() {
        return fechaPlantado;
    }

    public Campo fechaPlantado(Instant fechaPlantado) {
        this.fechaPlantado = fechaPlantado;
        return this;
    }

    public void setFechaPlantado(Instant fechaPlantado) {
        this.fechaPlantado = fechaPlantado;
    }

    public Instant getFechaCosecha() {
        return fechaCosecha;
    }

    public Campo fechaCosecha(Instant fechaCosecha) {
        this.fechaCosecha = fechaCosecha;
        return this;
    }

    public void setFechaCosecha(Instant fechaCosecha) {
        this.fechaCosecha = fechaCosecha;
    }

    public String getProducto() {
        return producto;
    }

    public Campo producto(String producto) {
        this.producto = producto;
        return this;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Long getTamano() {
        return tamano;
    }

    public Campo tamano(Long tamano) {
        this.tamano = tamano;
        return this;
    }

    public void setTamano(Long tamano) {
        this.tamano = tamano;
    }

    public Finca getFinca() {
        return finca;
    }

    public Campo finca(Finca finca) {
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
        if (!(o instanceof Campo)) {
            return false;
        }
        return id != null && id.equals(((Campo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Campo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", sembrado='" + isSembrado() + "'" +
            ", fechaPlantado='" + getFechaPlantado() + "'" +
            ", fechaCosecha='" + getFechaCosecha() + "'" +
            ", producto='" + getProducto() + "'" +
            ", tamano=" + getTamano() +
            "}";
    }
}
