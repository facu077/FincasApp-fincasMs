package ar.edu.um.fincasapp.fincasms.web.rest;

import ar.edu.um.fincasapp.fincasms.FincasMsApp;
import ar.edu.um.fincasapp.fincasms.config.SecurityBeanOverrideConfiguration;
import ar.edu.um.fincasapp.fincasms.domain.Campo;
import ar.edu.um.fincasapp.fincasms.repository.CampoRepository;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CampoResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, FincasMsApp.class })
@AutoConfigureMockMvc
@WithMockUser
public class CampoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SEMBRADO = false;
    private static final Boolean UPDATED_SEMBRADO = true;

    private static final Instant DEFAULT_FECHA_PLANTADO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_PLANTADO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_COSECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_COSECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PRODUCTO = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCTO = "BBBBBBBBBB";

    private static final Long DEFAULT_TAMANO = 1L;
    private static final Long UPDATED_TAMANO = 2L;

    @Autowired
    private CampoRepository campoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCampoMockMvc;

    private Campo campo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campo createEntity(EntityManager em) {
        Campo campo = new Campo()
            .nombre(DEFAULT_NOMBRE)
            .sembrado(DEFAULT_SEMBRADO)
            .fechaPlantado(DEFAULT_FECHA_PLANTADO)
            .fechaCosecha(DEFAULT_FECHA_COSECHA)
            .producto(DEFAULT_PRODUCTO)
            .tamano(DEFAULT_TAMANO);
        return campo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campo createUpdatedEntity(EntityManager em) {
        Campo campo = new Campo()
            .nombre(UPDATED_NOMBRE)
            .sembrado(UPDATED_SEMBRADO)
            .fechaPlantado(UPDATED_FECHA_PLANTADO)
            .fechaCosecha(UPDATED_FECHA_COSECHA)
            .producto(UPDATED_PRODUCTO)
            .tamano(UPDATED_TAMANO);
        return campo;
    }

    @BeforeEach
    public void initTest() {
        campo = createEntity(em);
    }

    @Test
    @Transactional
    public void createCampo() throws Exception {
        int databaseSizeBeforeCreate = campoRepository.findAll().size();
        // Create the Campo
        restCampoMockMvc.perform(post("/api/campos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(campo)))
            .andExpect(status().isCreated());

        // Validate the Campo in the database
        List<Campo> campoList = campoRepository.findAll();
        assertThat(campoList).hasSize(databaseSizeBeforeCreate + 1);
        Campo testCampo = campoList.get(campoList.size() - 1);
        assertThat(testCampo.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCampo.isSembrado()).isEqualTo(DEFAULT_SEMBRADO);
        assertThat(testCampo.getFechaPlantado()).isEqualTo(DEFAULT_FECHA_PLANTADO);
        assertThat(testCampo.getFechaCosecha()).isEqualTo(DEFAULT_FECHA_COSECHA);
        assertThat(testCampo.getProducto()).isEqualTo(DEFAULT_PRODUCTO);
        assertThat(testCampo.getTamano()).isEqualTo(DEFAULT_TAMANO);
    }

    @Test
    @Transactional
    public void createCampoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = campoRepository.findAll().size();

        // Create the Campo with an existing ID
        campo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCampoMockMvc.perform(post("/api/campos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(campo)))
            .andExpect(status().isBadRequest());

        // Validate the Campo in the database
        List<Campo> campoList = campoRepository.findAll();
        assertThat(campoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = campoRepository.findAll().size();
        // set the field null
        campo.setNombre(null);

        // Create the Campo, which fails.


        restCampoMockMvc.perform(post("/api/campos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(campo)))
            .andExpect(status().isBadRequest());

        List<Campo> campoList = campoRepository.findAll();
        assertThat(campoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCampos() throws Exception {
        // Initialize the database
        campoRepository.saveAndFlush(campo);

        // Get all the campoList
        restCampoMockMvc.perform(get("/api/campos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].sembrado").value(hasItem(DEFAULT_SEMBRADO.booleanValue())))
            .andExpect(jsonPath("$.[*].fechaPlantado").value(hasItem(DEFAULT_FECHA_PLANTADO.toString())))
            .andExpect(jsonPath("$.[*].fechaCosecha").value(hasItem(DEFAULT_FECHA_COSECHA.toString())))
            .andExpect(jsonPath("$.[*].producto").value(hasItem(DEFAULT_PRODUCTO)))
            .andExpect(jsonPath("$.[*].tamano").value(hasItem(DEFAULT_TAMANO.intValue())));
    }
    
    @Test
    @Transactional
    public void getCampo() throws Exception {
        // Initialize the database
        campoRepository.saveAndFlush(campo);

        // Get the campo
        restCampoMockMvc.perform(get("/api/campos/{id}", campo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(campo.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.sembrado").value(DEFAULT_SEMBRADO.booleanValue()))
            .andExpect(jsonPath("$.fechaPlantado").value(DEFAULT_FECHA_PLANTADO.toString()))
            .andExpect(jsonPath("$.fechaCosecha").value(DEFAULT_FECHA_COSECHA.toString()))
            .andExpect(jsonPath("$.producto").value(DEFAULT_PRODUCTO))
            .andExpect(jsonPath("$.tamano").value(DEFAULT_TAMANO.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingCampo() throws Exception {
        // Get the campo
        restCampoMockMvc.perform(get("/api/campos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCampo() throws Exception {
        // Initialize the database
        campoRepository.saveAndFlush(campo);

        int databaseSizeBeforeUpdate = campoRepository.findAll().size();

        // Update the campo
        Campo updatedCampo = campoRepository.findById(campo.getId()).get();
        // Disconnect from session so that the updates on updatedCampo are not directly saved in db
        em.detach(updatedCampo);
        updatedCampo
            .nombre(UPDATED_NOMBRE)
            .sembrado(UPDATED_SEMBRADO)
            .fechaPlantado(UPDATED_FECHA_PLANTADO)
            .fechaCosecha(UPDATED_FECHA_COSECHA)
            .producto(UPDATED_PRODUCTO)
            .tamano(UPDATED_TAMANO);

        restCampoMockMvc.perform(put("/api/campos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCampo)))
            .andExpect(status().isOk());

        // Validate the Campo in the database
        List<Campo> campoList = campoRepository.findAll();
        assertThat(campoList).hasSize(databaseSizeBeforeUpdate);
        Campo testCampo = campoList.get(campoList.size() - 1);
        assertThat(testCampo.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCampo.isSembrado()).isEqualTo(UPDATED_SEMBRADO);
        assertThat(testCampo.getFechaPlantado()).isEqualTo(UPDATED_FECHA_PLANTADO);
        assertThat(testCampo.getFechaCosecha()).isEqualTo(UPDATED_FECHA_COSECHA);
        assertThat(testCampo.getProducto()).isEqualTo(UPDATED_PRODUCTO);
        assertThat(testCampo.getTamano()).isEqualTo(UPDATED_TAMANO);
    }

    @Test
    @Transactional
    public void updateNonExistingCampo() throws Exception {
        int databaseSizeBeforeUpdate = campoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCampoMockMvc.perform(put("/api/campos").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(campo)))
            .andExpect(status().isBadRequest());

        // Validate the Campo in the database
        List<Campo> campoList = campoRepository.findAll();
        assertThat(campoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCampo() throws Exception {
        // Initialize the database
        campoRepository.saveAndFlush(campo);

        int databaseSizeBeforeDelete = campoRepository.findAll().size();

        // Delete the campo
        restCampoMockMvc.perform(delete("/api/campos/{id}", campo.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Campo> campoList = campoRepository.findAll();
        assertThat(campoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
