package com.julian.escuela.mappers;

import com.julian.escuela.dto.calificaciones.CalificacionRequest;
import com.julian.escuela.dto.calificaciones.CalificacionResponse;
import com.julian.escuela.entities.Calificacion;
import com.julian.escuela.entities.Inscripcion;
import com.julian.escuela.utils.StringCustomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalificacionMapper {

    private final InscripcionMapper inscripcionMapper;

    public Calificacion requestAEntidad(CalificacionRequest request, Inscripcion inscripcion) {
        return request == null ? null : Calificacion.crear(inscripcion, request.calificacion());
    }

    public CalificacionResponse entidadAResponse(Calificacion entidad) {
        return entidad == null
                ? null
                : new CalificacionResponse(
                        entidad.getIdCalificacion(),
                        inscripcionMapper.entidadADatosInscripcion(entidad.getInscripcion()),
                        entidad.getCalificacion(),
                        StringCustomUtils.LocalDateAString(
                                entidad.getFechaRegistro() == null ? null : entidad.getFechaRegistro().toLocalDate()
                        )
                );
    }
}
