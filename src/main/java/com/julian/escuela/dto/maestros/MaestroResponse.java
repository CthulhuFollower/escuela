package com.julian.escuela.dto.maestros;

import com.julian.escuela.dto.datos.DatosCurso;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Informacion de un maestro")
public record MaestroResponse(
        @Schema(description = "ID del maestro", example = "1")
        Long id,

        @Schema(description = "Nombre completo del maestro", example = "Maaximo Decimo Meridio")
        String nombre,

        @Schema(description = "Email del maestro", example = "test@text.com")
        String email,

        @Schema(description = "Telefono del maestro", example = "1234567890")
        String telefono,

        @Schema(description = "Datos de los cursos del maestro")
        List<DatosCurso> cursos
) {

}
