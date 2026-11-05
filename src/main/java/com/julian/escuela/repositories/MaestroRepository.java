package com.julian.escuela.repositories;

import com.julian.escuela.entities.Maestro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaestroRepository extends JpaRepository<Maestro, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByTelefono(String telefono);

    boolean existsByEmailIgnoreCaseAndIdMaestroNot(String email, Long id);

    boolean existsByTelefonoAndIdMaestroNot(String telefono, Long id);

}
