package ar.edu.um.fincasapp.fincasms.repository;

import ar.edu.um.fincasapp.fincasms.domain.Campo;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Campo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CampoRepository extends JpaRepository<Campo, Long> {
}
