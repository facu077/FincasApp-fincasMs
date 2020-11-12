package ar.edu.um.fincasapp.fincasms.service;

import ar.edu.um.fincasapp.fincasms.FincasMsApp;
import ar.edu.um.fincasapp.fincasms.config.SecurityBeanOverrideConfiguration;
import ar.edu.um.fincasapp.fincasms.domain.Finca;
import ar.edu.um.fincasapp.fincasms.repository.FincaRepository;
import ar.edu.um.fincasapp.fincasms.web.rest.FincaResource;
import ar.edu.um.fincasapp.fincasms.web.rest.TestUtil;
import ar.edu.um.fincasapp.fincasms.web.rest.errors.BadRequestAlertException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
public class FincasServiceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_USER_LOGIN = "user";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    @Autowired
    private FincasService fincasService;

    @Autowired FincaRepository fincaRepository;

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
    public void addFinca()
    {
        int databaseSizeBeforeCreate = fincaRepository.findAll().size();
        // Create the Finca
        Finca finca = fincasService.addFinca(this.finca);
        // Validate the Finca in the database
        List<Finca> fincaList = fincaRepository.findAll();
        assertThat(fincaList).hasSize(databaseSizeBeforeCreate + 1);
        Finca testFinca = fincaList.get(fincaList.size() - 1);
        // Asserts
        assertThat(testFinca.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testFinca.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(finca).isEqualTo(testFinca);
    }

    @Test
    @Transactional
    public void getCurrentUserFincas()
    {
        // Initialize the database and add some fincas
        fincaRepository.flush();
        Finca fincaOne = new Finca()
            .nombre("Don Miguel")
            .userLogin("user");
        fincaRepository.save(fincaOne);
        Finca fincaTwo = new Finca()
            .nombre("El Rancho")
            .userLogin("user");
        fincaRepository.save(fincaTwo);
        Finca fincaThree = new Finca()
            .nombre("El Ramal")
            .userLogin("OtherUser");
        fincaRepository.save(fincaThree);
        // Get the fincas for current user ("user")
        List<Finca> fincas = fincasService.getCurrentUserFincas();
        // Asserts
        assertThat(fincas.size()).isEqualTo(2);
        assertThat(fincas.contains(fincaOne)).isTrue();
        assertThat(fincas.contains(fincaTwo)).isTrue();
        assertThat(fincas.contains(fincaThree)).isFalse();
    }
}
