package ar.edu.um.fincasapp.fincasms.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Finca.
 */
@Entity
@Table(name = "finca")
public class Finca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "user_login", nullable = false)
    private String userLogin;

    @OneToOne
    @JoinColumn(unique = true)
    private Ubicacion ubicacion;

    @OneToOne
    @JoinColumn(unique = true)
    private Encargado encargado;

    @OneToMany(mappedBy = "finca")
    private Set<Campo> campos = new HashSet<>();

    @OneToMany(mappedBy = "finca")
    private Set<Cosecha> cosechas = new HashSet<>();

    @OneToMany(mappedBy = "finca")
    private Set<Herramienta> herramientas = new HashSet<>();

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

    public Finca nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public Finca userLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public Finca ubicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
        return this;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Encargado getEncargado() {
        return encargado;
    }

    public Finca encargado(Encargado encargado) {
        this.encargado = encargado;
        return this;
    }

    public void setEncargado(Encargado encargado) {
        this.encargado = encargado;
    }

    public Set<Campo> getCampos() {
        return campos;
    }

    public Finca campos(Set<Campo> campos) {
        this.campos = campos;
        return this;
    }

    public Finca addCampo(Campo campo) {
        this.campos.add(campo);
        campo.setFinca(this);
        return this;
    }

    public Finca removeCampo(Campo campo) {
        this.campos.remove(campo);
        campo.setFinca(null);
        return this;
    }

    public void setCampos(Set<Campo> campos) {
        this.campos = campos;
    }

    public Set<Cosecha> getCosechas() {
        return cosechas;
    }

    public Finca cosechas(Set<Cosecha> cosechas) {
        this.cosechas = cosechas;
        return this;
    }

    public Finca addCosecha(Cosecha cosecha) {
        this.cosechas.add(cosecha);
        cosecha.setFinca(this);
        return this;
    }

    public Finca removeCosecha(Cosecha cosecha) {
        this.cosechas.remove(cosecha);
        cosecha.setFinca(null);
        return this;
    }

    public void setCosechas(Set<Cosecha> cosechas) {
        this.cosechas = cosechas;
    }

    public Set<Herramienta> getHerramientas() {
        return herramientas;
    }

    public Finca herramientas(Set<Herramienta> herramientas) {
        this.herramientas = herramientas;
        return this;
    }

    public Finca addHerramienta(Herramienta herramienta) {
        this.herramientas.add(herramienta);
        herramienta.setFinca(this);
        return this;
    }

    public Finca removeHerramienta(Herramienta herramienta) {
        this.herramientas.remove(herramienta);
        herramienta.setFinca(null);
        return this;
    }

    public void setHerramientas(Set<Herramienta> herramientas) {
        this.herramientas = herramientas;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Finca)) {
            return false;
        }
        return id != null && id.equals(((Finca) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Finca{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
