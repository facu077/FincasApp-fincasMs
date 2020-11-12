package ar.edu.um.fincasapp.fincasms.repository;

import ar.edu.um.fincasapp.fincasms.domain.Finca;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Finca entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FincaRepository extends JpaRepository<Finca, Long> {
    List<Finca> findByUserLogin(String userLogin);
}
