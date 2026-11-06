package com.julian.escuela.dto.cursos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informacion de un curso")
public record CursoResponse(
        @Schema(description = "ID del curso", example = "1")
        Long id,

        @Schema(description = "Nombre del curso", example = "Matematicas I")
        String nombre,

        @Schema(description = "Descripcion del curso", example = "Fundamentos matematicos para nivel basico")
        String descripcion,

        @Schema(description = "Creditos del curso", example = "6")
        Integer creditos
) {
}
