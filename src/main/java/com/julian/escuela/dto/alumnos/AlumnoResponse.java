package com.julian.escuela.dto.alumnos;

import com.julian.escuela.dto.datos.DatosCalificacion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Informacion de un alumno")
public record AlumnoResponse(
        @Schema(description = "ID del alumno", example = "1")
        Long id,

        @Schema(description = "Nombre completo del alumno", example = "Juan Perez Lopez")
        String nombre,

        @Schema(description = "Email del alumno", example = "juan.perez@alumnos.com")
        String email,

        @Schema(description = "Matricula del alumno", example = "A2025001")
        String matricula,

        @Schema(description = "Fecha de ingreso del alumno", example = "2025-01-10T08:00:00")
        String fechaIngreso,

        @Schema(description = "Cursos inscritos con su periodo y calificacion")
        List<DatosCalificacion> calificaciones,

        @Schema(description = "Promedio del alumno", example = "9.9")
        BigDecimal promedio
) {
}
