package ar.edu.um.fincasapp.fincasms.web.rest;

import ar.edu.um.fincasapp.fincasms.domain.Cosecha;
import ar.edu.um.fincasapp.fincasms.repository.CosechaRepository;
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
 * REST controller for managing {@link ar.edu.um.fincasapp.fincasms.domain.Cosecha}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CosechaResource {

    private final Logger log = LoggerFactory.getLogger(CosechaResource.class);

    private static final String ENTITY_NAME = "fincasMsCosecha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CosechaRepository cosechaRepository;

    public CosechaResource(CosechaRepository cosechaRepository) {
        this.cosechaRepository = cosechaRepository;
    }

    /**
     * {@code POST  /cosechas} : Create a new cosecha.
     *
     * @param cosecha the cosecha to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cosecha, or with status {@code 400 (Bad Request)} if the cosecha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cosechas")
    public ResponseEntity<Cosecha> createCosecha(@Valid @RequestBody Cosecha cosecha) throws URISyntaxException {
        log.debug("REST request to save Cosecha : {}", cosecha);
        if (cosecha.getId() != null) {
            throw new BadRequestAlertException("A new cosecha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cosecha result = cosechaRepository.save(cosecha);
        return ResponseEntity.created(new URI("/api/cosechas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cosechas} : Updates an existing cosecha.
     *
     * @param cosecha the cosecha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cosecha,
     * or with status {@code 400 (Bad Request)} if the cosecha is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cosecha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cosechas")
    public ResponseEntity<Cosecha> updateCosecha(@Valid @RequestBody Cosecha cosecha) throws URISyntaxException {
        log.debug("REST request to update Cosecha : {}", cosecha);
        if (cosecha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cosecha result = cosechaRepository.save(cosecha);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cosecha.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cosechas} : get all the cosechas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cosechas in body.
     */
    @GetMapping("/cosechas")
    public List<Cosecha> getAllCosechas() {
        log.debug("REST request to get all Cosechas");
        return cosechaRepository.findAll();
    }

    /**
     * {@code GET  /cosechas/:id} : get the "id" cosecha.
     *
     * @param id the id of the cosecha to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cosecha, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cosechas/{id}")
    public ResponseEntity<Cosecha> getCosecha(@PathVariable Long id) {
        log.debug("REST request to get Cosecha : {}", id);
        Optional<Cosecha> cosecha = cosechaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cosecha);
    }

    /**
     * {@code DELETE  /cosechas/:id} : delete the "id" cosecha.
     *
     * @param id the id of the cosecha to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cosechas/{id}")
    public ResponseEntity<Void> deleteCosecha(@PathVariable Long id) {
        log.debug("REST request to delete Cosecha : {}", id);
        cosechaRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
