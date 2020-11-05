package ar.edu.um.fincasapp.fincasms.repository;

import ar.edu.um.fincasapp.fincasms.domain.Herramienta;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Herramienta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HerramientaRepository extends JpaRepository<Herramienta, Long> {
}
