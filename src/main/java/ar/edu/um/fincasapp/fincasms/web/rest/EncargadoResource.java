package ar.edu.um.fincasapp.fincasms.web.rest;

import ar.edu.um.fincasapp.fincasms.domain.Encargado;
import ar.edu.um.fincasapp.fincasms.repository.EncargadoRepository;
import ar.edu.um.fincasapp.fincasms.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ar.edu.um.fincasapp.fincasms.domain.Encargado}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EncargadoResource {

    private final Logger log = LoggerFactory.getLogger(EncargadoResource.class);

    private static final String ENTITY_NAME = "fincasMsEncargado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EncargadoRepository encargadoRepository;

    public EncargadoResource(EncargadoRepository encargadoRepository) {
        this.encargadoRepository = encargadoRepository;
    }

    /**
     * {@code POST  /encargados} : Create a new encargado.
     *
     * @param encargado the encargado to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new encargado, or with status {@code 400 (Bad Request)} if the encargado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/encargados")
    public ResponseEntity<Encargado> createEncargado(@Valid @RequestBody Encargado encargado) throws URISyntaxException {
        log.debug("REST request to save Encargado : {}", encargado);
        if (encargado.getId() != null) {
            throw new BadRequestAlertException("A new encargado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Encargado result = encargadoRepository.save(encargado);
        return ResponseEntity.created(new URI("/api/encargados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /encargados} : Updates an existing encargado.
     *
     * @param encargado the encargado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated encargado,
     * or with status {@code 400 (Bad Request)} if the encargado is not valid,
     * or with status {@code 500 (Internal Server Error)} if the encargado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/encargados")
    public ResponseEntity<Encargado> updateEncargado(@Valid @RequestBody Encargado encargado) throws URISyntaxException {
        log.debug("REST request to update Encargado : {}", encargado);
        if (encargado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Encargado result = encargadoRepository.save(encargado);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, encargado.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /encargados} : get all the encargados.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of encargados in body.
     */
    @GetMapping("/encargados")
    public List<Encargado> getAllEncargados() {
        log.debug("REST request to get all Encargados");
        return encargadoRepository.findAll();
    }

    /**
     * {@code GET  /encargados/:id} : get the "id" encargado.
     *
     * @param id the id of the encargado to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the encargado, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/encargados/{id}")
    public ResponseEntity<Encargado> getEncargado(@PathVariable Long id) {
        log.debug("REST request to get Encargado : {}", id);
        Optional<Encargado> encargado = encargadoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(encargado);
    }

    /**
     * {@code DELETE  /encargados/:id} : delete the "id" encargado.
     *
     * @param id the id of the encargado to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/encargados/{id}")
    public ResponseEntity<Void> deleteEncargado(@PathVariable Long id) {
        log.debug("REST request to delete Encargado : {}", id);
        encargadoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
