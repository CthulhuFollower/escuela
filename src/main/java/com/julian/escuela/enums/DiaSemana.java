package com.julian.escuela.enums;

import com.julian.escuela.exceptions.RecursoNoEncontradoException;
import com.julian.escuela.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DiaSemana {
    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miércoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes"),
    SABADO("Sábado");

    private final String descripcion;

    public static DiaSemana obtenerDiaSemanaPorDescripcion(String descripcion){
        StringCustomUtils.validarNoVacio(descripcion, "La descripcion es requerida");
        String descripcionNormalizada = StringCustomUtils.normalizarTexto(descripcion);

        for (DiaSemana diaSemana:values()){
            if (StringCustomUtils.normalizarTexto(diaSemana.descripcion).equalsIgnoreCase(descripcionNormalizada))
                return diaSemana;
        }

        throw new RecursoNoEncontradoException("No existe un dia de la semana con la descripcion " + descripcion);
    }
}
