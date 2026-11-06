package com.julian.escuela.dto.calificaciones;

import com.julian.escuela.dto.datos.DatosInscripcion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Informacion de una calificacion")
public record CalificacionResponse(
        @Schema(description = "ID de la calificacion", example = "2")
        Long id,

        @Schema(description = "Resumen de la inscripcion asociada")
        DatosInscripcion inscripcion,

        @Schema(description = "Calificacion entre 0 y 10", example = "8.5")
        BigDecimal calificacion,

        @Schema(description = "Fecha de registro en formato dd/MM/yyyy", example = "11/02/2026")
        String fechaRegistro
) {
}
