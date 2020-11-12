package ar.edu.um.fincasapp.fincasms.web.rest;

import ar.edu.um.fincasapp.fincasms.FincasMsApp;
import ar.edu.um.fincasapp.fincasms.config.SecurityBeanOverrideConfiguration;
import ar.edu.um.fincasapp.fincasms.domain.Finca;
import ar.edu.um.fincasapp.fincasms.repository.FincaRepository;

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
 * Integration tests for the {@link FincaResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, FincasMsApp.class })
@AutoConfigureMockMvc
@WithMockUser
public class FincaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_USER_LOGIN = "user";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    @Autowired
    private FincaRepository fincaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFincaMockMvc;

    private Finca finca;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Finca createEntity(EntityManager em) {
        Finca finca = new Finca()
            .nombre(DEFAULT_NOMBRE)
            .userLogin(DEFAULT_USER_LOGIN);
        return finca;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Finca createUpdatedEntity(EntityManager em) {
        Finca finca = new Finca()
            .nombre(UPDATED_NOMBRE)
            .userLogin(UPDATED_USER_LOGIN);
        return finca;
    }

    @BeforeEach
    public void initTest() {
        finca = createEntity(em);
    }

    @Test
    @Transactional
    public void createFinca() throws Exception {
        int databaseSizeBeforeCreate = fincaRepository.findAll().size();
        // Create the Finca
        restFincaMockMvc.perform(post("/api/fincas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(finca)))
            .andExpect(status().isCreated());

        // Validate the Finca in the database
        List<Finca> fincaList = fincaRepository.findAll();
        assertThat(fincaList).hasSize(databaseSizeBeforeCreate + 1);
        Finca testFinca = fincaList.get(fincaList.size() - 1);
        assertThat(testFinca.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testFinca.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
    }

    @Test
    @Transactional
    public void createFincaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fincaRepository.findAll().size();

        // Create the Finca with an existing ID
        finca.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFincaMockMvc.perform(post("/api/fincas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(finca)))
            .andExpect(status().isBadRequest());

        // Validate the Finca in the database
        List<Finca> fincaList = fincaRepository.findAll();
        assertThat(fincaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = fincaRepository.findAll().size();
        // set the field null
        finca.setNombre(null);

        // Create the Finca, which fails.


        restFincaMockMvc.perform(post("/api/fincas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(finca)))
            .andExpect(status().isBadRequest());

        List<Finca> fincaList = fincaRepository.findAll();
        assertThat(fincaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = fincaRepository.findAll().size();
        // set the field null
        finca.setUserLogin(null);

        // Create the Finca, which fails.


        restFincaMockMvc.perform(post("/api/fincas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(finca)))
            .andExpect(status().isBadRequest());

        List<Finca> fincaList = fincaRepository.findAll();
        assertThat(fincaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFincas() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get all the fincaList
        restFincaMockMvc.perform(get("/api/fincas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finca.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN)));
    }

    @Test
    @Transactional
    public void getFinca() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        // Get the finca
        restFincaMockMvc.perform(get("/api/fincas/{id}", finca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(finca.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN));
    }
    @Test
    @Transactional
    public void getNonExistingFinca() throws Exception {
        // Get the finca
        restFincaMockMvc.perform(get("/api/fincas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFinca() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        int databaseSizeBeforeUpdate = fincaRepository.findAll().size();

        // Update the finca
        Finca updatedFinca = fincaRepository.findById(finca.getId()).get();
        // Disconnect from session so that the updates on updatedFinca are not directly saved in db
        em.detach(updatedFinca);
        updatedFinca
            .nombre(UPDATED_NOMBRE)
            .userLogin(UPDATED_USER_LOGIN);

        restFincaMockMvc.perform(put("/api/fincas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFinca)))
            .andExpect(status().isOk());

        // Validate the Finca in the database
        List<Finca> fincaList = fincaRepository.findAll();
        assertThat(fincaList).hasSize(databaseSizeBeforeUpdate);
        Finca testFinca = fincaList.get(fincaList.size() - 1);
        assertThat(testFinca.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFinca.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    @Transactional
    public void updateNonExistingFinca() throws Exception {
        int databaseSizeBeforeUpdate = fincaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFincaMockMvc.perform(put("/api/fincas").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(finca)))
            .andExpect(status().isBadRequest());

        // Validate the Finca in the database
        List<Finca> fincaList = fincaRepository.findAll();
        assertThat(fincaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFinca() throws Exception {
        // Initialize the database
        fincaRepository.saveAndFlush(finca);

        int databaseSizeBeforeDelete = fincaRepository.findAll().size();

        // Delete the finca
        restFincaMockMvc.perform(delete("/api/fincas/{id}", finca.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Finca> fincaList = fincaRepository.findAll();
        assertThat(fincaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
