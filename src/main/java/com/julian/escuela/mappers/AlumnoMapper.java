package com.julian.escuela.mappers;

import com.julian.escuela.dto.alumnos.AlumnoResponse;
import com.julian.escuela.dto.alumnos.AlumnosRequest;
import com.julian.escuela.dto.datos.DatosCalificacion;
import com.julian.escuela.entities.Alumno;
import com.julian.escuela.utils.StringCustomUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlumnoMapper implements CommonMapper<AlumnosRequest, AlumnoResponse, Alumno>{

    @Override
    public Alumno requestAEntidad(AlumnosRequest request){

        return request == null
                ? null
                : Alumno.crear(
                        request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno()
        );
    }

    public Alumno requestAEntidad(AlumnosRequest request, String email, String matricula){
        if(request == null) return null;

        Alumno alumno = requestAEntidad(request);
        alumno.asignarDatosAcademicos(email, matricula);
        return alumno;
    }

    @Override
    public AlumnoResponse entidadAResponse(Alumno entidad){

        return entidad == null
                ? null
                : new AlumnoResponse(
                        entidad.getId(),
                entidad.obtenerNombreCompleto(),
                entidad.getEmail(),
                entidad.getMatricula(),
                StringCustomUtils.LocalDateAString(entidad.getFechaIngreso()),
                entidadADatosCalificacion(entidad),
                entidad.calcularPromedio()
        );
    }

    private List<DatosCalificacion> entidadADatosCalificacion(Alumno entidad){

        if (entidad == null || entidad.getInscripciones() == null || entidad.getInscripciones().isEmpty())
            return List.of();

        return entidad.getInscripciones().stream().map(inscripcion -> new DatosCalificacion(
                inscripcion.getGrupo().getCurso().getNombre(),
                inscripcion.getGrupo().getPeriodo(),
                inscripcion.getCalificacion() != null
                ? inscripcion.getCalificacion().getCalificacion() : null
        )).toList();
    }

}
