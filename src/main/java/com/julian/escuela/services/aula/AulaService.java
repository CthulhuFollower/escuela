package com.julian.escuela.services.aula;

import com.julian.escuela.dto.aula.AulaRequest;
import com.julian.escuela.dto.aula.AulaResponse;
import com.julian.escuela.services.CrudService;

import java.util.List;

public interface AulaService extends CrudService<AulaRequest, AulaResponse> {
    @Override
    default List<AulaResponse> listar() {
        return List.of();
    }

    @Override
    default AulaResponse obtenerPorId(Long id) {
        return null;
    }

    @Override
    default AulaResponse registrar(AulaRequest aulaRequest) {
        return null;
    }

    @Override
    default AulaResponse actualizar(AulaRequest aulaRequest, Long id) {
        return null;
    }

    @Override
    default void eliminar(Long id) {

    }
}
