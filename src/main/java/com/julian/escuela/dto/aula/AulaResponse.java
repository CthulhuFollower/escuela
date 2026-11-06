package com.julian.escuela.dto.aula;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informacion de un aula")
public record AulaResponse(

        @Schema(description = "ID del aula", example = "1")
        Long id,

        @Schema(description = "Nombre del aula", example = "Aula 23")
        String nombre,

        @Schema(description = "Capacidad del aula", example = "30")
        Integer capacidad


) {
}
