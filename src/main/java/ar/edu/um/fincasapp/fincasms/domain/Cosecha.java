package ar.edu.um.fincasapp.fincasms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Cosecha.
 */
@Entity
@Table(name = "cosecha")
public class Cosecha implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "producto", nullable = false)
    private String producto;

    @NotNull
    @Column(name = "peso", nullable = false)
    private Long peso;

    @ManyToOne
    @JsonIgnoreProperties(value = "cosechas", allowSetters = true)
    private Finca finca;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProducto() {
        return producto;
    }

    public Cosecha producto(String producto) {
        this.producto = producto;
        return this;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Long getPeso() {
        return peso;
    }

    public Cosecha peso(Long peso) {
        this.peso = peso;
        return this;
    }

    public void setPeso(Long peso) {
        this.peso = peso;
    }

    public Finca getFinca() {
        return finca;
    }

    public Cosecha finca(Finca finca) {
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
        if (!(o instanceof Cosecha)) {
            return false;
        }
        return id != null && id.equals(((Cosecha) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cosecha{" +
            "id=" + getId() +
            ", producto='" + getProducto() + "'" +
            ", peso=" + getPeso() +
            "}";
    }
}
