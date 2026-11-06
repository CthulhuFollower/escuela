package com.julian.escuela.dto.datos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumen de un maestro")
public record DatosMaestro(
        @Schema(description = "Nombre completo del maestro", example = "Laura Martinez Lopez")
        String nombre,
        @Schema(description = "Email del maestro", example = "laura@escuela.com")
        String email,
        @Schema(description = "Telefono del maestro", example = "5512345678")
        String telefono
) {
}
