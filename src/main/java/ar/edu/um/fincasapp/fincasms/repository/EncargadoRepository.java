package ar.edu.um.fincasapp.fincasms.repository;

import ar.edu.um.fincasapp.fincasms.domain.Encargado;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Encargado entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EncargadoRepository extends JpaRepository<Encargado, Long> {
}
