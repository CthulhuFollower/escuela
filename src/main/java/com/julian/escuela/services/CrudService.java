package com.julian.escuela.services;

import java.util.List;

public interface CrudService <REQUEST, RESPONSE> {

    List<RESPONSE> listar();

    RESPONSE obtenerPorId(Long id);

    RESPONSE registrar(REQUEST request);

    RESPONSE actualizar(REQUEST request, Long id);

    void eliminar(Long id);

}
