package com.julian.escuela.dto.aula;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos necesarios para registrar o actualizar un aula")
public record AulaRequest(

        @Schema(description = "Nombre del aula", example = "Aula 101")
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 1, max = 30, message = "El nombre debe tener entre 1 y 30 caracteres")
        String nombre,

        @Schema(description = "Capacidad del aula", example = "6")
        @NotNull(message = "La capacidad es requerida")
        @Positive(message = "La capacidad debe ser mayor que 0")
        @Max(value = 9999, message = "La capacidad no puede ser mayor de 9999")
        Integer capacidad
) {
}
