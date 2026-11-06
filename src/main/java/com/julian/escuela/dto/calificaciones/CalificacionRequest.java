package com.julian.escuela.dto.calificaciones;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Datos necesarios para registrar o actualizar una calificacion")
public record CalificacionRequest(
        @Schema(description = "ID de la inscripcion", example = "15")
        @NotNull(message = "El ID de la inscripcion es requerido")
        @Positive(message = "El ID de la inscripcion debe ser positivo")
        Long idInscripcion,

        @Schema(description = "Calificacion entre 0 y 10", example = "8.5")
        @NotNull(message = "La calificacion es requerida")
        @DecimalMin(value = "0", message = "La calificacion debe estar entre 0 y 10")
        @DecimalMax(value = "10", message = "La calificacion debe estar entre 0 y 10")
        BigDecimal calificacion
) {
}
