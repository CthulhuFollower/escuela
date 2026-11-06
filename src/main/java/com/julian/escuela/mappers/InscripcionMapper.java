package com.julian.escuela.mappers;

import com.julian.escuela.dto.datos.DatosAlumno;
import com.julian.escuela.dto.datos.DatosGrupo;
import com.julian.escuela.dto.datos.DatosInscripcion;
import com.julian.escuela.dto.inscripciones.InscripcionRequest;
import com.julian.escuela.dto.inscripciones.InscripcionResponse;
import com.julian.escuela.entities.Alumno;
import com.julian.escuela.entities.Grupo;
import com.julian.escuela.entities.Inscripcion;
import com.julian.escuela.utils.StringCustomUtils;
import org.springframework.stereotype.Component;

@Component
public class InscripcionMapper {

    public Inscripcion requestAEntidad(InscripcionRequest request, Alumno alumno, Grupo grupo) {
        return request == null ? null : Inscripcion.crear(alumno, grupo);
    }

    public InscripcionResponse entidadAResponse(Inscripcion entidad) {
        if (entidad == null) return null;

        DatosInscripcion datos = entidadADatosInscripcion(entidad);

        return new InscripcionResponse(
                entidad.getIdInscripcion(),
                datos.alumno(),
                datos.grupo(),
                entidad.getCalificacion() == null ? null : entidad.getCalificacion().getCalificacion(),
                datos.fechaInscripcion()
        );
    }

    public DatosInscripcion entidadADatosInscripcion(Inscripcion entidad) {
        if (entidad == null) return null;

        Alumno alumno = entidad.getAlumno();
        Grupo grupo = entidad.getGrupo();

        return new DatosInscripcion(
                new DatosAlumno(
                        alumno.obtenerNombreCompleto(),
                        alumno.getMatricula(),
                        alumno.getEmail(),
                        StringCustomUtils.LocalDateAString(alumno.getFechaIngreso())
                ),
                new DatosGrupo(
                        grupo.getCurso().getNombre(),
                        grupo.getMaestro().obtenerNombreCompleto(),
                        grupo.getAula().getNombre(),
                        grupo.getPeriodo()
                ),
                StringCustomUtils.LocalDateAString(
                        entidad.getFechaInscripcion() == null ? null : entidad.getFechaInscripcion().toLocalDate()
                )
        );
    }
}
