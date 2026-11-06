package com.julian.escuela.mappers;

import com.julian.escuela.dto.datos.DatosAula;
import com.julian.escuela.dto.datos.DatosMaestro;
import com.julian.escuela.dto.grupos.GrupoRequest;
import com.julian.escuela.dto.grupos.GrupoResponse;
import com.julian.escuela.entities.Aula;
import com.julian.escuela.entities.Curso;
import com.julian.escuela.entities.Grupo;
import com.julian.escuela.entities.Maestro;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GrupoMapper {

    private final CursoMapper cursoMapper;

    public Grupo requestAEntidad(
            GrupoRequest request,
            Curso curso,
            Maestro maestro,
            Aula aula
    ) {
        return request == null
                ? null
                : Grupo.crear(
                curso,
                maestro,
                aula,
                request.periodo()
        );
    }

    public GrupoResponse entidadAResponse(Grupo entidad) {
        if (entidad == null) return null;

        Maestro maestro = entidad.getMaestro();
        Aula aula = entidad.getAula();

        List<String> horarios = entidad.getHorarios().stream()
                .map(horario ->
                        horario.getDia().getDescripcion()
                                + " " + horario.getHoraInicio()
                                + " - " + horario.getHoraFin())
                .toList();

        return new GrupoResponse(
                entidad.getId(),
                cursoMapper.entidadADatosCurso(entidad.getCurso()),
                new DatosMaestro(
                        maestro.obtenerNombreCompleto(),
                        maestro.getEmail(),
                        maestro.getTelefono()
                ),
                new DatosAula(
                        aula.getNombre(),
                        aula.getCapacidad()
                ),
                horarios,
                entidad.getPeriodo()
        );
    }
}
