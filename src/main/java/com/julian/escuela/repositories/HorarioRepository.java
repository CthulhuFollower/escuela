package com.julian.escuela.repositories;

import com.julian.escuela.entities.Horario;
import com.julian.escuela.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    boolean existsByGrupoId(Long id);

    // En altas, idExcluido es null; en actualizaciones, es el ID del horario editado.
    @Query("""
            select case when count(h) > 0 then true else false end
            from Horario h
            where h.grupo.id = :idGrupo
              and h.dia = :dia
              and h.horaInicio < :horaFin
              and h.horaFin > :horaInicio
              and (:idExcluido is null or h.id <> :idExcluido)
            """)
    boolean existeTraslapePorGrupo(
            @Param("idGrupo") Long idGrupo,
            @Param("dia") DiaSemana dia,
            @Param("horaInicio") String horaInicio,
            @Param("horaFin") String horaFin,
            @Param("idExcluido") Long idExcluido);

    @Query("""
            select case when count(h) > 0 then true else false end
            from Horario h
            where h.grupo.aula.id = :idAula
              and h.grupo.periodo = :periodo
              and h.grupo.id <> :idGrupo
              and h.dia = :dia
              and h.horaInicio < :horaFin
              and h.horaFin > :horaInicio
              and (:idExcluido is null or h.id <> :idExcluido)
            """)
    boolean existeTraslxapePorAula(
            @Param("idAula") Long idAula,
            @Param("periodo") String periodo,
            @Param("idGrupo") Long idGrupo,
            @Param("dia") DiaSemana dia,
            @Param("horaInicio") String horaInicio,
            @Param("horaFin") String horaFin,
            @Param("idExcluido") Long idExcluido);
}
