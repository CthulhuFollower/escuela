package com.julian.escuela.services.maestros;

import com.julian.escuela.dto.maestros.MaestroRequest;
import com.julian.escuela.dto.maestros.MaestroResponse;
import com.julian.escuela.services.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MaestroService extends CrudService<MaestroRequest, MaestroResponse> {


    @Override
    default List<MaestroResponse> listar() {

        return List.of();
    }

    @Override
    default MaestroResponse obtenerPorId(Long id) {
        return null;
    }

    @Override
    default MaestroResponse registrar(MaestroRequest request) {
        return null;
    }

    @Override
    default MaestroResponse actualizar(MaestroRequest request, Long id) {
        return null;
    }

    @Override
    default void eliminar(Long id) {

    }


}
