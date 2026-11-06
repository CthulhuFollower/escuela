package com.julian.escuela.mappers;

import com.julian.escuela.dto.datos.DatosGrupo;
import com.julian.escuela.dto.horarios.HorariosRequest;
import com.julian.escuela.dto.horarios.HorariosResponse;
import com.julian.escuela.entities.Grupo;
import com.julian.escuela.entities.Horario;
import com.julian.escuela.enums.DiaSemana;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class HorariosMapper{

    private final GrupoMapper grupoMapper;

    public Horario requestAEntidad(HorariosRequest request, Grupo grupo) {

        return request == null
                ? null
                : Horario.crear(
                        grupo,
                DiaSemana.obtenerDiaSemanaPorDescripcion(request.dia()),
                request.horaInicio(),
                request.horaFin()

        );
    }

    public HorariosResponse entidadAResponse(Horario entidad) {
        
        if (entidad == null) return null;

        Grupo grupo = entidad.getGrupo();

        String horario = entidad.getDia().getDescripcion() + " " + entidad.getHoraInicio() + " " + entidad.getHoraFin();
        
        return new HorariosResponse(
                entidad.getId(),
                new DatosGrupo(
                        grupo.getCurso().getNombre(),
                        grupo.getMaestro().obtenerNombreCompleto(),
                        grupo.getAula().getNombre(),
                        grupo.getPeriodo()
                ),
                horario
        );

    }
}
