package com.julian.escuela.dto.horarios;

import com.julian.escuela.dto.datos.DatosGrupo;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informacion de un horario")
public record HorariosResponse(

        @Schema(description = "ID del horario", example = "1")
        Long id,

        @Schema(description = "Resumen del grupo asociado")
        DatosGrupo grupo,

        @Schema(description = "Dia y horas del horario", example = "Lunes 08:00 10:00")
        String horario
) {}
