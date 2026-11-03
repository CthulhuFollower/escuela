package com.julian.escuela.mappers;

import com.julian.escuela.dto.cursos.CursoRequest;
import com.julian.escuela.dto.cursos.CursoResponse;
import com.julian.escuela.dto.datos.DatosCurso;
import com.julian.escuela.entities.Curso;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper implements CommonMapper<CursoRequest, CursoResponse, Curso>{
    //Implementar


    @Override
    public Curso requestAEntidad(CursoRequest cursoRequest) {
        return null;
    }

    @Override
    public CursoResponse entidadAResponse(Curso entidad) {
        return null;
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
