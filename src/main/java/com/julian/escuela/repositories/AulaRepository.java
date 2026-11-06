package com.julian.escuela.repositories;

import com.julian.escuela.entities.Aula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AulaRepository extends JpaRepository<Aula, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);

}
