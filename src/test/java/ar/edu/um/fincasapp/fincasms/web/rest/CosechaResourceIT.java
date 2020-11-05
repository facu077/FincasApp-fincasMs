package ar.edu.um.fincasapp.fincasms.web.rest;

import ar.edu.um.fincasapp.fincasms.FincasMsApp;
import ar.edu.um.fincasapp.fincasms.config.SecurityBeanOverrideConfiguration;
import ar.edu.um.fincasapp.fincasms.domain.Cosecha;
import ar.edu.um.fincasapp.fincasms.repository.CosechaRepository;

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

/**
 * Integration tests for the {@link CosechaResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, FincasMsApp.class })
@AutoConfigureMockMvc
@WithMockUser
public class CosechaResourceIT {

    private static final String DEFAULT_PRODUCTO = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCTO = "BBBBBBBBBB";

    private static final Long DEFAULT_PESO = 1L;
    private static final Long UPDATED_PESO = 2L;

    @Autowired
    private CosechaRepository cosechaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCosechaMockMvc;

    private Cosecha cosecha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cosecha createEntity(EntityManager em) {
        Cosecha cosecha = new Cosecha()
            .producto(DEFAULT_PRODUCTO)
            .peso(DEFAULT_PESO);
        return cosecha;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cosecha createUpdatedEntity(EntityManager em) {
        Cosecha cosecha = new Cosecha()
            .producto(UPDATED_PRODUCTO)
            .peso(UPDATED_PESO);
        return cosecha;
    }

    @BeforeEach
    public void initTest() {
        cosecha = createEntity(em);
    }

    @Test
    @Transactional
    public void createCosecha() throws Exception {
        int databaseSizeBeforeCreate = cosechaRepository.findAll().size();
        // Create the Cosecha
        restCosechaMockMvc.perform(post("/api/cosechas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cosecha)))
            .andExpect(status().isCreated());

        // Validate the Cosecha in the database
        List<Cosecha> cosechaList = cosechaRepository.findAll();
        assertThat(cosechaList).hasSize(databaseSizeBeforeCreate + 1);
        Cosecha testCosecha = cosechaList.get(cosechaList.size() - 1);
        assertThat(testCosecha.getProducto()).isEqualTo(DEFAULT_PRODUCTO);
        assertThat(testCosecha.getPeso()).isEqualTo(DEFAULT_PESO);
    }

    @Test
    @Transactional
    public void createCosechaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cosechaRepository.findAll().size();

        // Create the Cosecha with an existing ID
        cosecha.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCosechaMockMvc.perform(post("/api/cosechas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cosecha)))
            .andExpect(status().isBadRequest());

        // Validate the Cosecha in the database
        List<Cosecha> cosechaList = cosechaRepository.findAll();
        assertThat(cosechaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkProductoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cosechaRepository.findAll().size();
        // set the field null
        cosecha.setProducto(null);

        // Create the Cosecha, which fails.


        restCosechaMockMvc.perform(post("/api/cosechas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cosecha)))
            .andExpect(status().isBadRequest());

        List<Cosecha> cosechaList = cosechaRepository.findAll();
        assertThat(cosechaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPesoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cosechaRepository.findAll().size();
        // set the field null
        cosecha.setPeso(null);

        // Create the Cosecha, which fails.


        restCosechaMockMvc.perform(post("/api/cosechas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cosecha)))
            .andExpect(status().isBadRequest());

        List<Cosecha> cosechaList = cosechaRepository.findAll();
        assertThat(cosechaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCosechas() throws Exception {
        // Initialize the database
        cosechaRepository.saveAndFlush(cosecha);

        // Get all the cosechaList
        restCosechaMockMvc.perform(get("/api/cosechas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cosecha.getId().intValue())))
            .andExpect(jsonPath("$.[*].producto").value(hasItem(DEFAULT_PRODUCTO)))
            .andExpect(jsonPath("$.[*].peso").value(hasItem(DEFAULT_PESO.intValue())));
    }
    
    @Test
    @Transactional
    public void getCosecha() throws Exception {
        // Initialize the database
        cosechaRepository.saveAndFlush(cosecha);

        // Get the cosecha
        restCosechaMockMvc.perform(get("/api/cosechas/{id}", cosecha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cosecha.getId().intValue()))
            .andExpect(jsonPath("$.producto").value(DEFAULT_PRODUCTO))
            .andExpect(jsonPath("$.peso").value(DEFAULT_PESO.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingCosecha() throws Exception {
        // Get the cosecha
        restCosechaMockMvc.perform(get("/api/cosechas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCosecha() throws Exception {
        // Initialize the database
        cosechaRepository.saveAndFlush(cosecha);

        int databaseSizeBeforeUpdate = cosechaRepository.findAll().size();

        // Update the cosecha
        Cosecha updatedCosecha = cosechaRepository.findById(cosecha.getId()).get();
        // Disconnect from session so that the updates on updatedCosecha are not directly saved in db
        em.detach(updatedCosecha);
        updatedCosecha
            .producto(UPDATED_PRODUCTO)
            .peso(UPDATED_PESO);

        restCosechaMockMvc.perform(put("/api/cosechas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCosecha)))
            .andExpect(status().isOk());

        // Validate the Cosecha in the database
        List<Cosecha> cosechaList = cosechaRepository.findAll();
        assertThat(cosechaList).hasSize(databaseSizeBeforeUpdate);
        Cosecha testCosecha = cosechaList.get(cosechaList.size() - 1);
        assertThat(testCosecha.getProducto()).isEqualTo(UPDATED_PRODUCTO);
        assertThat(testCosecha.getPeso()).isEqualTo(UPDATED_PESO);
    }

    @Test
    @Transactional
    public void updateNonExistingCosecha() throws Exception {
        int databaseSizeBeforeUpdate = cosechaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCosechaMockMvc.perform(put("/api/cosechas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cosecha)))
            .andExpect(status().isBadRequest());

        // Validate the Cosecha in the database
        List<Cosecha> cosechaList = cosechaRepository.findAll();
        assertThat(cosechaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCosecha() throws Exception {
        // Initialize the database
        cosechaRepository.saveAndFlush(cosecha);

        int databaseSizeBeforeDelete = cosechaRepository.findAll().size();

        // Delete the cosecha
        restCosechaMockMvc.perform(delete("/api/cosechas/{id}", cosecha.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cosecha> cosechaList = cosechaRepository.findAll();
        assertThat(cosechaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
