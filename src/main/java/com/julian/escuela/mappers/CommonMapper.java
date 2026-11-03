package com.julian.escuela.mappers;


public interface CommonMapper<REQUEST, RESPONSE, ENTITY> {

    ENTITY requestAEntidad(REQUEST request);

    RESPONSE entidadAResponse(ENTITY entidad);

}
