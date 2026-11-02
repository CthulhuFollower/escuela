package com.julian.escuela.dto.maestros;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para registrar o actualizar un maestro")
public record MaestroRequest(
        @Schema(description = "Nombre del maestro", example = "Maximo")
        @NotBlank(message = "El nombre es requerido")
        @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
        String nombre,

        @Schema(description = "Apellido paterno del maestro", example = "Maximon")
        @NotBlank(message = "El apellido paterno es requerido")
        @Size(min = 1, max = 50, message = "El apellido paterno debe tener entre 1 y 50 caracteres")
        String apellidoPaterno,

        @Schema(description = "Apellido materno del maestro", example = "Maxima")
        @NotBlank(message = "El apellido materno es requerido")
        @Size(min = 1, max = 50, message = "El apellido materno debe tener entre 1 y 50 caracteres")
        String apellidoMaterno,

        @Schema(description = "Email del maestro", example = "maximo@gmail.com")
        @NotBlank(message = "El email es requerido")
        @Size(min = 1, max = 50, message = "El email debe tener entre 8 y 100 caracteres")
        String email,

        @Schema(description = "Telefono del maestro", example = "1234567890")
        @NotBlank(message = "El telefono es requerido")
        @Size(min = 1, max = 50, message = "El telefono debe contener solo 10 digitos")
        String telefono
) {
}
