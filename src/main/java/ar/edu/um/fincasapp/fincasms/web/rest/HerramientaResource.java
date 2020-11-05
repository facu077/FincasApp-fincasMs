package ar.edu.um.fincasapp.fincasms.web.rest;

import ar.edu.um.fincasapp.fincasms.domain.Herramienta;
import ar.edu.um.fincasapp.fincasms.repository.HerramientaRepository;
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
 * REST controller for managing {@link ar.edu.um.fincasapp.fincasms.domain.Herramienta}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HerramientaResource {

    private final Logger log = LoggerFactory.getLogger(HerramientaResource.class);

    private static final String ENTITY_NAME = "fincasMsHerramienta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HerramientaRepository herramientaRepository;

    public HerramientaResource(HerramientaRepository herramientaRepository) {
        this.herramientaRepository = herramientaRepository;
    }

    /**
     * {@code POST  /herramientas} : Create a new herramienta.
     *
     * @param herramienta the herramienta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new herramienta, or with status {@code 400 (Bad Request)} if the herramienta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/herramientas")
    public ResponseEntity<Herramienta> createHerramienta(@Valid @RequestBody Herramienta herramienta) throws URISyntaxException {
        log.debug("REST request to save Herramienta : {}", herramienta);
        if (herramienta.getId() != null) {
            throw new BadRequestAlertException("A new herramienta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Herramienta result = herramientaRepository.save(herramienta);
        return ResponseEntity.created(new URI("/api/herramientas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /herramientas} : Updates an existing herramienta.
     *
     * @param herramienta the herramienta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated herramienta,
     * or with status {@code 400 (Bad Request)} if the herramienta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the herramienta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/herramientas")
    public ResponseEntity<Herramienta> updateHerramienta(@Valid @RequestBody Herramienta herramienta) throws URISyntaxException {
        log.debug("REST request to update Herramienta : {}", herramienta);
        if (herramienta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Herramienta result = herramientaRepository.save(herramienta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, herramienta.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /herramientas} : get all the herramientas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of herramientas in body.
     */
    @GetMapping("/herramientas")
    public List<Herramienta> getAllHerramientas() {
        log.debug("REST request to get all Herramientas");
        return herramientaRepository.findAll();
    }

    /**
     * {@code GET  /herramientas/:id} : get the "id" herramienta.
     *
     * @param id the id of the herramienta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the herramienta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/herramientas/{id}")
    public ResponseEntity<Herramienta> getHerramienta(@PathVariable Long id) {
        log.debug("REST request to get Herramienta : {}", id);
        Optional<Herramienta> herramienta = herramientaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(herramienta);
    }

    /**
     * {@code DELETE  /herramientas/:id} : delete the "id" herramienta.
     *
     * @param id the id of the herramienta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/herramientas/{id}")
    public ResponseEntity<Void> deleteHerramienta(@PathVariable Long id) {
        log.debug("REST request to delete Herramienta : {}", id);
        herramientaRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
