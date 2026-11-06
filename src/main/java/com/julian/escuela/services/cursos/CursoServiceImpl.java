package com.julian.escuela.services.cursos;

import com.julian.escuela.dto.cursos.CursoRequest;
import com.julian.escuela.dto.cursos.CursoResponse;
import com.julian.escuela.entities.Curso;
import com.julian.escuela.exceptions.ConflictoException;
import com.julian.escuela.exceptions.EntidadRelacionadaException;
import com.julian.escuela.mappers.CursoMapper;
import com.julian.escuela.repositories.CursoRepository;
import com.julian.escuela.repositories.GrupoRespository;
import com.julian.escuela.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final CursoMapper cursoMapper;
    private final GrupoRespository grupoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponse> listar() {
        log.info("Listando los cursos");
        return cursoRepository.findAll().stream().map(cursoMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CursoResponse obtenerPorId(Long id) {
        return cursoMapper.entidadAResponse(obtenerCurso(id));
    }

    @Override
    public CursoResponse registrar(CursoRequest request) {
        log.info("Registrando nuevo curso...");

        validarDatosUnicos(request);

        Curso curso = cursoMapper.requestAEntidad(request);
        cursoRepository.saveAndFlush(curso);

        log.info("Curso {} registrado", curso.getNombre());
        return cursoMapper.entidadAResponse(curso);
    }

    @Override
    public CursoResponse actualizar(CursoRequest request, Long id) {
        log.info("Actualizando curso con id: {}", id);

        Curso curso = obtenerCurso(id);
        validarCambiosUnicos(request, id);

        curso.actualizar(request.nombre(), request.descripcion(), request.creditos());
        cursoRepository.saveAndFlush(curso);

        log.info("Curso {} actualizado", curso.getNombre());
        return cursoMapper.entidadAResponse(curso);
    }

    @Override
    public void eliminar(Long id) {
        Curso curso = obtenerCurso(id);

        if (grupoRepository.existsByCurso_IdCurso(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el curso ya que tiene grupos asignados"
            );

        log.info("Eliminando curso con id: {}", id);
        cursoRepository.delete(curso);
        cursoRepository.flush();
    }

    private Curso obtenerCurso(Long id) {
        return ServiceUtils.obtenerEntidadOException(cursoRepository, id, Curso.class);
    }

    public void validarDatosUnicos(CursoRequest request) {
        if (cursoRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new ConflictoException("Ya existe un curso registrado con el nombre: " + request.nombre());
    }

    public void validarCambiosUnicos(CursoRequest request, Long id) {
        if (cursoRepository.existsByNombreIgnoreCaseAndIdCursoNot(request.nombre().trim(), id))
            throw new ConflictoException("Ya existe un curso registrado con el nombre: " + request.nombre());
    }
}
