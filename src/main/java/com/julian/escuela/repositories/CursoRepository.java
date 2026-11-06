package com.julian.escuela.repositories;

import com.julian.escuela.entities.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdCursoNot(String nombre, Long id);
}
