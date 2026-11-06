package com.julian.escuela.dto.inscripciones;

import com.julian.escuela.dto.datos.DatosAlumno;
import com.julian.escuela.dto.datos.DatosGrupo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Informacion de una inscripcion")
public record InscripcionResponse(
        @Schema(description = "ID de la inscripcion", example = "1")
        Long id,

        @Schema(description = "Resumen del alumno inscrito")
        DatosAlumno alumno,

        @Schema(description = "Resumen del grupo asociado")
        DatosGrupo grupo,

        @Schema(description = "Calificacion de la inscripcion, o null si no existe", example = "8.0")
        BigDecimal calificacion,

        @Schema(description = "Fecha de inscripcion en formato dd/MM/yyyy", example = "11/02/2026")
        String fechaInscripcion
) {
}
