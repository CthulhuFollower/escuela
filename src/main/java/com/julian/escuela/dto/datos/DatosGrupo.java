package com.julian.escuela.dto.datos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumen de un grupo")
public record DatosGrupo(

        @Schema(description = "Nombre del curso", example = "Matematicas I")
        String curso,

        @Schema(description = "Nombre completo del maestro", example = "Laura Martinez Lopez")
        String maestro,

        @Schema(description = "Nombre del aula", example = "Aula 101")
        String aula,

        @Schema(description = "Periodo academico", example = "2026-01")
        String periodo

) {
}
