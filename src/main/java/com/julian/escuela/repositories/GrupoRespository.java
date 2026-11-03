package com.julian.escuela.repositories;

import com.julian.escuela.entities.Maestro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRespository extends JpaRepository<Maestro, Long> {

    boolean existByMaestroId(Long id);

}
