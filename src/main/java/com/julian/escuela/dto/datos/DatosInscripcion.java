package com.julian.escuela.dto.datos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumen de una inscripcion")
public record DatosInscripcion(
        @Schema(description = "Resumen del alumno inscrito")
        DatosAlumno alumno,

        @Schema(description = "Resumen del grupo asociado")
        DatosGrupo grupo,

        @Schema(description = "Fecha de inscripcion en formato dd/MM/yyyy", example = "11/02/2026")
        String fechaInscripcion
) {
}
