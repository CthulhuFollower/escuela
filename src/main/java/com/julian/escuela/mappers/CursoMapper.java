package com.julian.escuela.mappers;

import com.julian.escuela.dto.cursos.CursoRequest;
import com.julian.escuela.dto.cursos.CursoResponse;
import com.julian.escuela.dto.datos.DatosCurso;
import com.julian.escuela.entities.Curso;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper implements CommonMapper<CursoRequest, CursoResponse, Curso>{
    @Override
    public Curso requestAEntidad(CursoRequest cursoRequest) {
        return cursoRequest == null
                ? null
                : Curso.crear(
                        cursoRequest.nombre(),
                        cursoRequest.descripcion(),
                        cursoRequest.creditos()
                );
    }

    @Override
    public CursoResponse entidadAResponse(Curso entidad) {
        return entidad == null
                ? null
                : new CursoResponse(
                        entidad.getIdCurso(),
                        entidad.getNombre(),
                        entidad.getDescripcion(),
                        entidad.getCreditos()
                );
    }

    public DatosCurso entidadADatosCurso(Curso entidad){
        return entidad == null
                ? null
                : new DatosCurso(
                        entidad.getNombre(),
                entidad.getDescripcion(),
                entidad.getCreditos()
        );
    }
}
