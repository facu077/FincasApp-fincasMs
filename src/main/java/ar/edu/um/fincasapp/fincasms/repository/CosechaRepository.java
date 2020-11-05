package ar.edu.um.fincasapp.fincasms.repository;

import ar.edu.um.fincasapp.fincasms.domain.Cosecha;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Cosecha entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CosechaRepository extends JpaRepository<Cosecha, Long> {
}
