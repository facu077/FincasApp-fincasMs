package ar.edu.um.fincasapp.fincasms.web.rest;

import ar.edu.um.fincasapp.fincasms.FincasMsApp;
import ar.edu.um.fincasapp.fincasms.config.SecurityBeanOverrideConfiguration;
import ar.edu.um.fincasapp.fincasms.domain.Herramienta;
import ar.edu.um.fincasapp.fincasms.repository.HerramientaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.um.fincasapp.fincasms.domain.enumeration.TipoHerramienta;
/**
 * Integration tests for the {@link HerramientaResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, FincasMsApp.class })
@AutoConfigureMockMvc
@WithMockUser
public class HerramientaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final TipoHerramienta DEFAULT_TIPO = TipoHerramienta.VEHICULO;
    private static final TipoHerramienta UPDATED_TIPO = TipoHerramienta.MAQUINA;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private HerramientaRepository herramientaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHerramientaMockMvc;

    private Herramienta herramienta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Herramienta createEntity(EntityManager em) {
        Herramienta herramienta = new Herramienta()
            .nombre(DEFAULT_NOMBRE)
            .tipo(DEFAULT_TIPO)
            .descripcion(DEFAULT_DESCRIPCION);
        return herramienta;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Herramienta createUpdatedEntity(EntityManager em) {
        Herramienta herramienta = new Herramienta()
            .nombre(UPDATED_NOMBRE)
            .tipo(UPDATED_TIPO)
            .descripcion(UPDATED_DESCRIPCION);
        return herramienta;
    }

    @BeforeEach
    public void initTest() {
        herramienta = createEntity(em);
    }

    @Test
    @Transactional
    public void createHerramienta() throws Exception {
        int databaseSizeBeforeCreate = herramientaRepository.findAll().size();
        // Create the Herramienta
        restHerramientaMockMvc.perform(post("/api/herramientas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(herramienta)))
            .andExpect(status().isCreated());

        // Validate the Herramienta in the database
        List<Herramienta> herramientaList = herramientaRepository.findAll();
        assertThat(herramientaList).hasSize(databaseSizeBeforeCreate + 1);
        Herramienta testHerramienta = herramientaList.get(herramientaList.size() - 1);
        assertThat(testHerramienta.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testHerramienta.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testHerramienta.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createHerramientaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = herramientaRepository.findAll().size();

        // Create the Herramienta with an existing ID
        herramienta.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHerramientaMockMvc.perform(post("/api/herramientas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(herramienta)))
            .andExpect(status().isBadRequest());

        // Validate the Herramienta in the database
        List<Herramienta> herramientaList = herramientaRepository.findAll();
        assertThat(herramientaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = herramientaRepository.findAll().size();
        // set the field null
        herramienta.setNombre(null);

        // Create the Herramienta, which fails.


        restHerramientaMockMvc.perform(post("/api/herramientas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(herramienta)))
            .andExpect(status().isBadRequest());

        List<Herramienta> herramientaList = herramientaRepository.findAll();
        assertThat(herramientaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = herramientaRepository.findAll().size();
        // set the field null
        herramienta.setTipo(null);

        // Create the Herramienta, which fails.


        restHerramientaMockMvc.perform(post("/api/herramientas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(herramienta)))
            .andExpect(status().isBadRequest());

        List<Herramienta> herramientaList = herramientaRepository.findAll();
        assertThat(herramientaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHerramientas() throws Exception {
        // Initialize the database
        herramientaRepository.saveAndFlush(herramienta);

        // Get all the herramientaList
        restHerramientaMockMvc.perform(get("/api/herramientas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(herramienta.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }
    
    @Test
    @Transactional
    public void getHerramienta() throws Exception {
        // Initialize the database
        herramientaRepository.saveAndFlush(herramienta);

        // Get the herramienta
        restHerramientaMockMvc.perform(get("/api/herramientas/{id}", herramienta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(herramienta.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }
    @Test
    @Transactional
    public void getNonExistingHerramienta() throws Exception {
        // Get the herramienta
        restHerramientaMockMvc.perform(get("/api/herramientas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHerramienta() throws Exception {
        // Initialize the database
        herramientaRepository.saveAndFlush(herramienta);

        int databaseSizeBeforeUpdate = herramientaRepository.findAll().size();

        // Update the herramienta
        Herramienta updatedHerramienta = herramientaRepository.findById(herramienta.getId()).get();
        // Disconnect from session so that the updates on updatedHerramienta are not directly saved in db
        em.detach(updatedHerramienta);
        updatedHerramienta
            .nombre(UPDATED_NOMBRE)
            .tipo(UPDATED_TIPO)
            .descripcion(UPDATED_DESCRIPCION);

        restHerramientaMockMvc.perform(put("/api/herramientas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedHerramienta)))
            .andExpect(status().isOk());

        // Validate the Herramienta in the database
        List<Herramienta> herramientaList = herramientaRepository.findAll();
        assertThat(herramientaList).hasSize(databaseSizeBeforeUpdate);
        Herramienta testHerramienta = herramientaList.get(herramientaList.size() - 1);
        assertThat(testHerramienta.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testHerramienta.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testHerramienta.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingHerramienta() throws Exception {
        int databaseSizeBeforeUpdate = herramientaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHerramientaMockMvc.perform(put("/api/herramientas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(herramienta)))
            .andExpect(status().isBadRequest());

        // Validate the Herramienta in the database
        List<Herramienta> herramientaList = herramientaRepository.findAll();
        assertThat(herramientaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHerramienta() throws Exception {
        // Initialize the database
        herramientaRepository.saveAndFlush(herramienta);

        int databaseSizeBeforeDelete = herramientaRepository.findAll().size();

        // Delete the herramienta
        restHerramientaMockMvc.perform(delete("/api/herramientas/{id}", herramienta.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Herramienta> herramientaList = herramientaRepository.findAll();
        assertThat(herramientaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
