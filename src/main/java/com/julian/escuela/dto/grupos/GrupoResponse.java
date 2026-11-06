package com.julian.escuela.dto.grupos;

import com.julian.escuela.dto.datos.DatosAula;
import com.julian.escuela.dto.datos.DatosCurso;
import com.julian.escuela.dto.datos.DatosMaestro;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Informacion de un grupo")
public record GrupoResponse(

        @Schema(description = "ID del grupo", example = "1")
        Long id,

        @Schema(description = "Datos del curso")
        DatosCurso curso,

        @Schema(description = "Datos del maestro")
        DatosMaestro maestro,

        @Schema(description = "Datos del aula")
        DatosAula aula,

        @Schema(description = "Horarios del grupo", example = "[\"Lunes 08:00 - 10:00\"]")
        List<String> horarios,

        @Schema(description = "Periodo academico", example = "2026-01")
        String periodo

) {
}
