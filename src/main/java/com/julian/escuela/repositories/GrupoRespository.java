package com.julian.escuela.repositories;

import com.julian.escuela.entities.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRespository extends JpaRepository<Grupo, Long> {

    boolean existsByMaestro_IdMaestro(Long id);

    boolean existsByAulaId(Long id);

}
