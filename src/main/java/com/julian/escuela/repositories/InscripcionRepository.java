package com.julian.escuela.repositories;

import com.julian.escuela.entities.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    boolean existsByAlumnoId(Long id);

    boolean existsByGrupoId(Long id);
}
