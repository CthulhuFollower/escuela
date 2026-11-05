package com.julian.escuela.dto.cursos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para registrar o actualizar un curso")
public record CursoRequest(
        @Schema(description = "Nombre del curso", example = "Matematicas I")
        @NotBlank(message = "El nombre es requerido")
        @Size(max = 100, message = "El nombre debe tener como maximo 100 caracteres")
        String nombre,

        @Schema(description = "Descripcion opcional del curso", example = "Fundamentos matematicos para nivel basico")
        @Size(max = 200, message = "La descripcion debe tener como maximo 200 caracteres")
        String descripcion,

        @Schema(description = "Creditos del curso, entre 1 y 99", example = "6")
        @NotNull(message = "Los creditos son requeridos")
        @Positive(message = "Los creditos deben ser mayores que 0")
        @Max(value = 99, message = "Los creditos no pueden exceder 99")
        Integer creditos
) {
}
