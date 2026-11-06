package com.julian.escuela.dto.horarios;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Schema(description = "Datos necesarios para registrar o actualizar un horario")
public record HorariosRequest (
        @Schema(description = "ID del grupo", example = "1")
        @NotNull(message = "El ID del grupo es requerido")
        @Positive(message = "El ID del grupo debe ser positivo")
        Long idGrupo,

        @Schema(description = "Dia de la semana", example = "Lunes")
        @NotBlank(message = "El dia es requerido")
        String dia,

        @Schema(description = "Hora de inicio en formato HH:mm", example = "08:00")
        @NotBlank(message = "La hora de inicio es requerida")
        @Pattern(regexp = "^([01][0-9]|2[0-3]):[0-5][0-9]$",
                message = "La hora de inicio debe tener el formato HH:mm y estar entre 00:00 y 23:59")
        String horaInicio,

        @Schema(description = "Hora de fin en formato HH:mm", example = "10:00")
        @NotBlank(message = "La hora de fin es requerida")
        @Pattern(regexp = "^([01][0-9]|2[0-3]):[0-5][0-9]$",
                message = "La hora de fin debe tener el formato HH:mm y estar entre 00:00 y 23:59")
        String horaFin
)
{
}
