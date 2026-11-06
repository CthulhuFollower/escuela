package com.julian.escuela.services.horarios;

import com.julian.escuela.dto.horarios.HorariosRequest;
import com.julian.escuela.dto.horarios.HorariosResponse;
import com.julian.escuela.entities.Horario;
import com.julian.escuela.services.CrudService;

import java.util.List;

public interface HorariosService extends CrudService<HorariosRequest, HorariosResponse> {

    @Override
    default List<HorariosResponse> listar() {
        return List.of();
    }

    @Override
    default HorariosResponse obtenerPorId(Long id) {
        return null;
    }

    @Override
    default HorariosResponse registrar(HorariosRequest request) {
        return null;
    }

    @Override
    default HorariosResponse actualizar(HorariosRequest request, Long id) {
        return null;
    }

    @Override
    default void eliminar(Long id) {

    }
}
