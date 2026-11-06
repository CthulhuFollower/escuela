package com.julian.escuela.repositories;

import com.julian.escuela.entities.Grupo;
import com.julian.escuela.entities.Curso;
import com.julian.escuela.entities.Maestro;
import com.julian.escuela.entities.Aula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRespository extends JpaRepository<Grupo, Long> {

    boolean existsByMaestro_IdMaestro(Long id);

    boolean existsByAulaId(Long id);

    boolean existsByCurso_IdCurso(Long id);

    boolean existsByCursoAndMaestroAndAulaAndPeriodo(Curso curso, Maestro maestro, Aula aula, String periodo);

    boolean existsByCursoAndMaestroAndAulaAndPeriodoAndIdNot(
            Curso curso, Maestro maestro, Aula aula, String periodo, Long id);

}
