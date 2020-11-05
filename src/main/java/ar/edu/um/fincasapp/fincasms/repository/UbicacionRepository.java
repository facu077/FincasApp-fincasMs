package ar.edu.um.fincasapp.fincasms.repository;

import ar.edu.um.fincasapp.fincasms.domain.Ubicacion;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Ubicacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
}
