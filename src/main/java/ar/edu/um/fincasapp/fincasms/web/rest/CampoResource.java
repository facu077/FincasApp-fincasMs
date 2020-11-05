package ar.edu.um.fincasapp.fincasms.web.rest;

import ar.edu.um.fincasapp.fincasms.domain.Campo;
import ar.edu.um.fincasapp.fincasms.repository.CampoRepository;
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
 * REST controller for managing {@link ar.edu.um.fincasapp.fincasms.domain.Campo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CampoResource {

    private final Logger log = LoggerFactory.getLogger(CampoResource.class);

    private static final String ENTITY_NAME = "fincasMsCampo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CampoRepository campoRepository;

    public CampoResource(CampoRepository campoRepository) {
        this.campoRepository = campoRepository;
    }

    /**
     * {@code POST  /campos} : Create a new campo.
     *
     * @param campo the campo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new campo, or with status {@code 400 (Bad Request)} if the campo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/campos")
    public ResponseEntity<Campo> createCampo(@Valid @RequestBody Campo campo) throws URISyntaxException {
        log.debug("REST request to save Campo : {}", campo);
        if (campo.getId() != null) {
            throw new BadRequestAlertException("A new campo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Campo result = campoRepository.save(campo);
        return ResponseEntity.created(new URI("/api/campos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /campos} : Updates an existing campo.
     *
     * @param campo the campo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated campo,
     * or with status {@code 400 (Bad Request)} if the campo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the campo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/campos")
    public ResponseEntity<Campo> updateCampo(@Valid @RequestBody Campo campo) throws URISyntaxException {
        log.debug("REST request to update Campo : {}", campo);
        if (campo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Campo result = campoRepository.save(campo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, campo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /campos} : get all the campos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of campos in body.
     */
    @GetMapping("/campos")
    public List<Campo> getAllCampos() {
        log.debug("REST request to get all Campos");
        return campoRepository.findAll();
    }

    /**
     * {@code GET  /campos/:id} : get the "id" campo.
     *
     * @param id the id of the campo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the campo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/campos/{id}")
    public ResponseEntity<Campo> getCampo(@PathVariable Long id) {
        log.debug("REST request to get Campo : {}", id);
        Optional<Campo> campo = campoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(campo);
    }

    /**
     * {@code DELETE  /campos/:id} : delete the "id" campo.
     *
     * @param id the id of the campo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/campos/{id}")
    public ResponseEntity<Void> deleteCampo(@PathVariable Long id) {
        log.debug("REST request to delete Campo : {}", id);
        campoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
