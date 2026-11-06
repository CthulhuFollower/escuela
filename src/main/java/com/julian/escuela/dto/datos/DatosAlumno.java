package com.julian.escuela.dto.datos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumen de un alumno")
public record DatosAlumno(
        @Schema(description = "Nombre completo del alumno", example = "Juan Perez Lopez")
        String nombre,

        @Schema(description = "Matricula del alumno", example = "PELOJU2605")
        String matricula,

        @Schema(description = "Email institucional del alumno", example = "juan.perez@alumnos.com")
        String email,

        @Schema(description = "Fecha de ingreso en formato dd/MM/yyyy", example = "10/01/2025")
        String fechaIngreso
) {
}
