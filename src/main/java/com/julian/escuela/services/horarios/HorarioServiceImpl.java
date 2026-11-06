package com.julian.escuela.services.horarios;

import com.julian.escuela.dto.horarios.HorariosRequest;
import com.julian.escuela.dto.horarios.HorariosResponse;

import java.util.List;

public class HorarioServiceImpl implements HorariosService{



    @Override
    public List<HorariosResponse> listar() {
        return ;
    }

    @Override
    public HorariosResponse obtenerPorId(Long id) {
        return HorariosService.super.obtenerPorId(id);
    }

    @Override
    public HorariosResponse registrar(HorariosRequest request) {
        return HorariosService.super.registrar(request);
    }

    @Override
    public HorariosResponse actualizar(HorariosRequest request, Long id) {
        return HorariosService.super.actualizar(request, id);
    }

    @Override
    public void eliminar(Long id) {
        HorariosService.super.eliminar(id);
    }
}
