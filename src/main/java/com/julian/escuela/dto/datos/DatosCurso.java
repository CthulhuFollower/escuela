package com.julian.escuela.dto.datos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de un curso")
public record DatosCurso(
        @Schema(description = "Nombre del curso", example = "Matematicas I")
        String nombre,

        @Schema(description = "Descripcion del curso", example = "Curso de calculo integral")
        String descripcion,

        @Schema(description = "Creditos del curso", example = "5")
        String creditos){

}