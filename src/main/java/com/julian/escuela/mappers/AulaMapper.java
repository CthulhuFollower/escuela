package com.julian.escuela.mappers;

import com.julian.escuela.dto.aula.AulaRequest;
import com.julian.escuela.dto.aula.AulaResponse;
import com.julian.escuela.entities.Aula;
import org.springframework.stereotype.Component;

@Component
public class AulaMapper implements CommonMapper<AulaRequest, AulaResponse, Aula>{

    @Override
    public Aula requestAEntidad(AulaRequest aulaRequest) {

        return aulaRequest == null
                ? null
                : Aula.crear(
                        aulaRequest.nombre(),
                        aulaRequest.capacidad()
                );
    }

    @Override
    public AulaResponse entidadAResponse(Aula entidad) {

        return entidad == null
                ? null
                : new AulaResponse(
                entidad.getId(),
                entidad.getNombre(),
                entidad.getCapacidad()
        );

    }
}
