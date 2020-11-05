package ar.edu.um.fincasapp.fincasms.web.rest;

import ar.edu.um.fincasapp.fincasms.domain.Ubicacion;
import ar.edu.um.fincasapp.fincasms.repository.UbicacionRepository;
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
 * REST controller for managing {@link ar.edu.um.fincasapp.fincasms.domain.Ubicacion}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UbicacionResource {

    private final Logger log = LoggerFactory.getLogger(UbicacionResource.class);

    private static final String ENTITY_NAME = "fincasMsUbicacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UbicacionRepository ubicacionRepository;

    public UbicacionResource(UbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    /**
     * {@code POST  /ubicacions} : Create a new ubicacion.
     *
     * @param ubicacion the ubicacion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ubicacion, or with status {@code 400 (Bad Request)} if the ubicacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ubicacions")
    public ResponseEntity<Ubicacion> createUbicacion(@Valid @RequestBody Ubicacion ubicacion) throws URISyntaxException {
        log.debug("REST request to save Ubicacion : {}", ubicacion);
        if (ubicacion.getId() != null) {
            throw new BadRequestAlertException("A new ubicacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ubicacion result = ubicacionRepository.save(ubicacion);
        return ResponseEntity.created(new URI("/api/ubicacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ubicacions} : Updates an existing ubicacion.
     *
     * @param ubicacion the ubicacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ubicacion,
     * or with status {@code 400 (Bad Request)} if the ubicacion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ubicacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ubicacions")
    public ResponseEntity<Ubicacion> updateUbicacion(@Valid @RequestBody Ubicacion ubicacion) throws URISyntaxException {
        log.debug("REST request to update Ubicacion : {}", ubicacion);
        if (ubicacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ubicacion result = ubicacionRepository.save(ubicacion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ubicacion.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ubicacions} : get all the ubicacions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ubicacions in body.
     */
    @GetMapping("/ubicacions")
    public List<Ubicacion> getAllUbicacions() {
        log.debug("REST request to get all Ubicacions");
        return ubicacionRepository.findAll();
    }

    /**
     * {@code GET  /ubicacions/:id} : get the "id" ubicacion.
     *
     * @param id the id of the ubicacion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ubicacion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ubicacions/{id}")
    public ResponseEntity<Ubicacion> getUbicacion(@PathVariable Long id) {
        log.debug("REST request to get Ubicacion : {}", id);
        Optional<Ubicacion> ubicacion = ubicacionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ubicacion);
    }

    /**
     * {@code DELETE  /ubicacions/:id} : delete the "id" ubicacion.
     *
     * @param id the id of the ubicacion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ubicacions/{id}")
    public ResponseEntity<Void> deleteUbicacion(@PathVariable Long id) {
        log.debug("REST request to delete Ubicacion : {}", id);
        ubicacionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
