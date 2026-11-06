package com.julian.escuela.repositories;

import com.julian.escuela.entities.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    boolean existsByInscripcion_IdInscripcion(Long idInscripcion);

    boolean existsByInscripcion_IdInscripcionAndIdCalificacionNot(Long idInscripcion, Long id);
}
