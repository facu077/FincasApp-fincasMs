package ar.edu.um.fincasapp.fincasms.web.rest;

import ar.edu.um.fincasapp.fincasms.FincasMsApp;
import ar.edu.um.fincasapp.fincasms.config.SecurityBeanOverrideConfiguration;
import ar.edu.um.fincasapp.fincasms.domain.Ubicacion;
import ar.edu.um.fincasapp.fincasms.repository.UbicacionRepository;

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

import ar.edu.um.fincasapp.fincasms.domain.enumeration.Departamento;
/**
 * Integration tests for the {@link UbicacionResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, FincasMsApp.class })
@AutoConfigureMockMvc
@WithMockUser
public class UbicacionResourceIT {

    private static final Departamento DEFAULT_DEPARTAMENTO = Departamento.MENDOZA;
    private static final Departamento UPDATED_DEPARTAMENTO = Departamento.GUAYMALLEN;

    private static final String DEFAULT_CALLE = "AAAAAAAAAA";
    private static final String UPDATED_CALLE = "BBBBBBBBBB";

    private static final Long DEFAULT_NUMERO = 1L;
    private static final Long UPDATED_NUMERO = 2L;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUbicacionMockMvc;

    private Ubicacion ubicacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ubicacion createEntity(EntityManager em) {
        Ubicacion ubicacion = new Ubicacion()
            .departamento(DEFAULT_DEPARTAMENTO)
            .calle(DEFAULT_CALLE)
            .numero(DEFAULT_NUMERO)
            .descripcion(DEFAULT_DESCRIPCION);
        return ubicacion;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ubicacion createUpdatedEntity(EntityManager em) {
        Ubicacion ubicacion = new Ubicacion()
            .departamento(UPDATED_DEPARTAMENTO)
            .calle(UPDATED_CALLE)
            .numero(UPDATED_NUMERO)
            .descripcion(UPDATED_DESCRIPCION);
        return ubicacion;
    }

    @BeforeEach
    public void initTest() {
        ubicacion = createEntity(em);
    }

    @Test
    @Transactional
    public void createUbicacion() throws Exception {
        int databaseSizeBeforeCreate = ubicacionRepository.findAll().size();
        // Create the Ubicacion
        restUbicacionMockMvc.perform(post("/api/ubicacions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ubicacion)))
            .andExpect(status().isCreated());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeCreate + 1);
        Ubicacion testUbicacion = ubicacionList.get(ubicacionList.size() - 1);
        assertThat(testUbicacion.getDepartamento()).isEqualTo(DEFAULT_DEPARTAMENTO);
        assertThat(testUbicacion.getCalle()).isEqualTo(DEFAULT_CALLE);
        assertThat(testUbicacion.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testUbicacion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createUbicacionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ubicacionRepository.findAll().size();

        // Create the Ubicacion with an existing ID
        ubicacion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUbicacionMockMvc.perform(post("/api/ubicacions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ubicacion)))
            .andExpect(status().isBadRequest());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDepartamentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = ubicacionRepository.findAll().size();
        // set the field null
        ubicacion.setDepartamento(null);

        // Create the Ubicacion, which fails.


        restUbicacionMockMvc.perform(post("/api/ubicacions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ubicacion)))
            .andExpect(status().isBadRequest());

        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUbicacions() throws Exception {
        // Initialize the database
        ubicacionRepository.saveAndFlush(ubicacion);

        // Get all the ubicacionList
        restUbicacionMockMvc.perform(get("/api/ubicacions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ubicacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].departamento").value(hasItem(DEFAULT_DEPARTAMENTO.toString())))
            .andExpect(jsonPath("$.[*].calle").value(hasItem(DEFAULT_CALLE)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }
    
    @Test
    @Transactional
    public void getUbicacion() throws Exception {
        // Initialize the database
        ubicacionRepository.saveAndFlush(ubicacion);

        // Get the ubicacion
        restUbicacionMockMvc.perform(get("/api/ubicacions/{id}", ubicacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ubicacion.getId().intValue()))
            .andExpect(jsonPath("$.departamento").value(DEFAULT_DEPARTAMENTO.toString()))
            .andExpect(jsonPath("$.calle").value(DEFAULT_CALLE))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }
    @Test
    @Transactional
    public void getNonExistingUbicacion() throws Exception {
        // Get the ubicacion
        restUbicacionMockMvc.perform(get("/api/ubicacions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUbicacion() throws Exception {
        // Initialize the database
        ubicacionRepository.saveAndFlush(ubicacion);

        int databaseSizeBeforeUpdate = ubicacionRepository.findAll().size();

        // Update the ubicacion
        Ubicacion updatedUbicacion = ubicacionRepository.findById(ubicacion.getId()).get();
        // Disconnect from session so that the updates on updatedUbicacion are not directly saved in db
        em.detach(updatedUbicacion);
        updatedUbicacion
            .departamento(UPDATED_DEPARTAMENTO)
            .calle(UPDATED_CALLE)
            .numero(UPDATED_NUMERO)
            .descripcion(UPDATED_DESCRIPCION);

        restUbicacionMockMvc.perform(put("/api/ubicacions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedUbicacion)))
            .andExpect(status().isOk());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeUpdate);
        Ubicacion testUbicacion = ubicacionList.get(ubicacionList.size() - 1);
        assertThat(testUbicacion.getDepartamento()).isEqualTo(UPDATED_DEPARTAMENTO);
        assertThat(testUbicacion.getCalle()).isEqualTo(UPDATED_CALLE);
        assertThat(testUbicacion.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testUbicacion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingUbicacion() throws Exception {
        int databaseSizeBeforeUpdate = ubicacionRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUbicacionMockMvc.perform(put("/api/ubicacions").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ubicacion)))
            .andExpect(status().isBadRequest());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUbicacion() throws Exception {
        // Initialize the database
        ubicacionRepository.saveAndFlush(ubicacion);

        int databaseSizeBeforeDelete = ubicacionRepository.findAll().size();

        // Delete the ubicacion
        restUbicacionMockMvc.perform(delete("/api/ubicacions/{id}", ubicacion.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
