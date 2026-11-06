package com.julian.escuela.services.grupos;

import com.julian.escuela.dto.grupos.GrupoRequest;
import com.julian.escuela.dto.grupos.GrupoResponse;
import com.julian.escuela.entities.Aula;
import com.julian.escuela.entities.Curso;
import com.julian.escuela.entities.Grupo;
import com.julian.escuela.entities.Maestro;
import com.julian.escuela.exceptions.ConflictoException;
import com.julian.escuela.exceptions.EntidadRelacionadaException;
import com.julian.escuela.exceptions.RecursoNoEncontradoException;
import com.julian.escuela.mappers.GrupoMapper;
import com.julian.escuela.repositories.AulaRepository;
import com.julian.escuela.repositories.CursoRepository;
import com.julian.escuela.repositories.GrupoRespository;
import com.julian.escuela.repositories.HorarioRepository;
import com.julian.escuela.repositories.InscripcionRepository;
import com.julian.escuela.repositories.MaestroRepository;
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
public class GrupoServiceImpl implements GrupoService {

    private final GrupoRespository grupoRepository;
    private final CursoRepository cursoRepository;
    private final MaestroRepository maestroRepository;
    private final AulaRepository aulaRepository;
    private final InscripcionRepository inscripcionRepository;
    private final HorarioRepository horarioRepository;
    private final GrupoMapper grupoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponse> listar() {
        log.info("Listando los grupos");
        return grupoRepository.findAll().stream().map(grupoMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GrupoResponse obtenerPorId(Long id) {
        return grupoMapper.entidadAResponse(obtenerGrupo(id));
    }

    @Override
    public GrupoResponse registrar(GrupoRequest request) {
        log.info("Registrando nuevo grupo...");

        Curso curso = ServiceUtils.obtenerEntidadOException(cursoRepository, request.idCurso(), Curso.class);
        Maestro maestro = ServiceUtils.obtenerEntidadOException(maestroRepository, request.idMaestro(), Maestro.class);
        Aula aula = ServiceUtils.obtenerEntidadOException(aulaRepository, request.idAula(), Aula.class);

        if (grupoRepository.existsByCursoAndMaestroAndAulaAndPeriodo(curso, maestro, aula, request.periodo()))
            throw new ConflictoException("Ya existe un grupo con el mismo curso, maestro, aula y periodo");

        Grupo grupo = grupoMapper.requestAEntidad(request, curso, maestro, aula);
        grupoRepository.saveAndFlush(grupo);

        log.info("Grupo {} registrado", grupo.getId());
        return grupoMapper.entidadAResponse(grupo);
    }

    @Override
    public GrupoResponse actualizar(GrupoRequest request, Long id) {
        log.info("Actualizando grupo con id: {}", id);

        Grupo grupo = obtenerGrupo(id);
        Curso curso = ServiceUtils.obtenerEntidadOException(cursoRepository, request.idCurso(), Curso.class);
        Maestro maestro = ServiceUtils.obtenerEntidadOException(maestroRepository, request.idMaestro(), Maestro.class);
        Aula aula = ServiceUtils.obtenerEntidadOException(aulaRepository, request.idAula(), Aula.class);

        if (grupoRepository.existsByCursoAndMaestroAndAulaAndPeriodoAndIdNot(
                curso, maestro, aula, request.periodo(), id))
            throw new ConflictoException("Ya existe un grupo con el mismo curso, maestro, aula y periodo");

        grupo.actualizar(curso, maestro, aula, request.periodo());
        grupoRepository.saveAndFlush(grupo);

        log.info("Grupo {} actualizado", id);
        return grupoMapper.entidadAResponse(grupo);
    }

    @Override
    public void eliminar(Long id){
        Grupo grupo = obtenerGrupo(id);

        if (inscripcionRepository.existsByGrupoId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el grupo porque tiene inscripciones asociadas"
            );

        if (horarioRepository.existsByGrupoId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el grupo ya que tiene horarios asociados"
            );

        log.info("Eliminando grupo con id: {}", id);
        grupoRepository.delete(grupo);
        grupoRepository.flush();
    }

    private Grupo obtenerGrupo(Long id) {
        return ServiceUtils.obtenerEntidadOException(grupoRepository, id, Grupo.class);
    }
}
