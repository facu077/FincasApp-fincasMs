package ar.edu.um.fincasapp.fincasms.web.rest;

import ar.edu.um.fincasapp.fincasms.FincasMsApp;
import ar.edu.um.fincasapp.fincasms.config.SecurityBeanOverrideConfiguration;
import ar.edu.um.fincasapp.fincasms.domain.Encargado;
import ar.edu.um.fincasapp.fincasms.repository.EncargadoRepository;

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
 * Integration tests for the {@link EncargadoResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, FincasMsApp.class })
@AutoConfigureMockMvc
@WithMockUser
public class EncargadoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    @Autowired
    private EncargadoRepository encargadoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEncargadoMockMvc;

    private Encargado encargado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Encargado createEntity(EntityManager em) {
        Encargado encargado = new Encargado()
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .email(DEFAULT_EMAIL)
            .telefono(DEFAULT_TELEFONO);
        return encargado;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Encargado createUpdatedEntity(EntityManager em) {
        Encargado encargado = new Encargado()
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO);
        return encargado;
    }

    @BeforeEach
    public void initTest() {
        encargado = createEntity(em);
    }

    @Test
    @Transactional
    public void createEncargado() throws Exception {
        int databaseSizeBeforeCreate = encargadoRepository.findAll().size();
        // Create the Encargado
        restEncargadoMockMvc.perform(post("/api/encargados").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(encargado)))
            .andExpect(status().isCreated());

        // Validate the Encargado in the database
        List<Encargado> encargadoList = encargadoRepository.findAll();
        assertThat(encargadoList).hasSize(databaseSizeBeforeCreate + 1);
        Encargado testEncargado = encargadoList.get(encargadoList.size() - 1);
        assertThat(testEncargado.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testEncargado.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testEncargado.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEncargado.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
    }

    @Test
    @Transactional
    public void createEncargadoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = encargadoRepository.findAll().size();

        // Create the Encargado with an existing ID
        encargado.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEncargadoMockMvc.perform(post("/api/encargados").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(encargado)))
            .andExpect(status().isBadRequest());

        // Validate the Encargado in the database
        List<Encargado> encargadoList = encargadoRepository.findAll();
        assertThat(encargadoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = encargadoRepository.findAll().size();
        // set the field null
        encargado.setNombre(null);

        // Create the Encargado, which fails.


        restEncargadoMockMvc.perform(post("/api/encargados").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(encargado)))
            .andExpect(status().isBadRequest());

        List<Encargado> encargadoList = encargadoRepository.findAll();
        assertThat(encargadoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEncargados() throws Exception {
        // Initialize the database
        encargadoRepository.saveAndFlush(encargado);

        // Get all the encargadoList
        restEncargadoMockMvc.perform(get("/api/encargados?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(encargado.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)));
    }
    
    @Test
    @Transactional
    public void getEncargado() throws Exception {
        // Initialize the database
        encargadoRepository.saveAndFlush(encargado);

        // Get the encargado
        restEncargadoMockMvc.perform(get("/api/encargados/{id}", encargado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(encargado.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO));
    }
    @Test
    @Transactional
    public void getNonExistingEncargado() throws Exception {
        // Get the encargado
        restEncargadoMockMvc.perform(get("/api/encargados/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEncargado() throws Exception {
        // Initialize the database
        encargadoRepository.saveAndFlush(encargado);

        int databaseSizeBeforeUpdate = encargadoRepository.findAll().size();

        // Update the encargado
        Encargado updatedEncargado = encargadoRepository.findById(encargado.getId()).get();
        // Disconnect from session so that the updates on updatedEncargado are not directly saved in db
        em.detach(updatedEncargado);
        updatedEncargado
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .email(UPDATED_EMAIL)
            .telefono(UPDATED_TELEFONO);

        restEncargadoMockMvc.perform(put("/api/encargados").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEncargado)))
            .andExpect(status().isOk());

        // Validate the Encargado in the database
        List<Encargado> encargadoList = encargadoRepository.findAll();
        assertThat(encargadoList).hasSize(databaseSizeBeforeUpdate);
        Encargado testEncargado = encargadoList.get(encargadoList.size() - 1);
        assertThat(testEncargado.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testEncargado.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testEncargado.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEncargado.getTelefono()).isEqualTo(UPDATED_TELEFONO);
    }

    @Test
    @Transactional
    public void updateNonExistingEncargado() throws Exception {
        int databaseSizeBeforeUpdate = encargadoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEncargadoMockMvc.perform(put("/api/encargados").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(encargado)))
            .andExpect(status().isBadRequest());

        // Validate the Encargado in the database
        List<Encargado> encargadoList = encargadoRepository.findAll();
        assertThat(encargadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEncargado() throws Exception {
        // Initialize the database
        encargadoRepository.saveAndFlush(encargado);

        int databaseSizeBeforeDelete = encargadoRepository.findAll().size();

        // Delete the encargado
        restEncargadoMockMvc.perform(delete("/api/encargados/{id}", encargado.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Encargado> encargadoList = encargadoRepository.findAll();
        assertThat(encargadoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
