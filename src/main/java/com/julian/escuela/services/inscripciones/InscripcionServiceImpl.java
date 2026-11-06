package com.julian.escuela.services.inscripciones;

import com.julian.escuela.dto.inscripciones.InscripcionRequest;
import com.julian.escuela.dto.inscripciones.InscripcionResponse;
import com.julian.escuela.entities.Alumno;
import com.julian.escuela.entities.Grupo;
import com.julian.escuela.entities.Inscripcion;
import com.julian.escuela.exceptions.ConflictoException;
import com.julian.escuela.exceptions.EntidadRelacionadaException;
import com.julian.escuela.mappers.InscripcionMapper;
import com.julian.escuela.repositories.AlumnoRepository;
import com.julian.escuela.repositories.GrupoRespository;
import com.julian.escuela.repositories.InscripcionRepository;
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
public class InscripcionServiceImpl implements InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final AlumnoRepository alumnoRepository;
    private final GrupoRespository grupoRepository;
    private final InscripcionMapper inscripcionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionResponse> listar() {
        log.info("Listando las inscripciones");
        return inscripcionRepository.findAll().stream().map(inscripcionMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InscripcionResponse obtenerPorId(Long id) {
        return inscripcionMapper.entidadAResponse(obtenerInscripcion(id));
    }

    @Override
    public InscripcionResponse registrar(InscripcionRequest request) {
        log.info("Registrando nueva inscripcion...");

        Alumno alumno = ServiceUtils.obtenerEntidadOException(alumnoRepository, request.idAlumno(), Alumno.class);
        Grupo grupo = ServiceUtils.obtenerEntidadOException(grupoRepository, request.idGrupo(), Grupo.class);
        validarDatosUnicos(request);

        Inscripcion inscripcion = inscripcionMapper.requestAEntidad(request, alumno, grupo);
        inscripcionRepository.saveAndFlush(inscripcion);

        log.info("Inscripcion {} registrada", inscripcion.getIdInscripcion());
        return inscripcionMapper.entidadAResponse(inscripcion);
    }

    @Override
    public InscripcionResponse actualizar(InscripcionRequest request, Long id) {
        log.info("Actualizando inscripcion con id: {}", id);

        Inscripcion inscripcion = obtenerInscripcion(id);
        Alumno alumno = ServiceUtils.obtenerEntidadOException(alumnoRepository, request.idAlumno(), Alumno.class);
        Grupo grupo = ServiceUtils.obtenerEntidadOException(grupoRepository, request.idGrupo(), Grupo.class);
        validarCambiosUnicos(request, id);

        inscripcion.actualizar(alumno, grupo);
        inscripcionRepository.saveAndFlush(inscripcion);

        log.info("Inscripcion {} actualizada", id);
        return inscripcionMapper.entidadAResponse(inscripcion);
    }

    @Override
    public void eliminar(Long id) {
        Inscripcion inscripcion = obtenerInscripcion(id);

        if (inscripcion.getCalificacion() != null)
            throw new EntidadRelacionadaException(
                    "No se puede eliminar la inscripcion ya que tiene una calificacion asociada"
            );

        log.info("Eliminando inscripcion con id: {}", id);
        inscripcionRepository.delete(inscripcion);
        inscripcionRepository.flush();
    }

    private Inscripcion obtenerInscripcion(Long id) {
        return ServiceUtils.obtenerEntidadOException(inscripcionRepository, id, Inscripcion.class);
    }

    public void validarDatosUnicos(InscripcionRequest request) {
        if (inscripcionRepository.existsByAlumnoIdAndGrupoId(request.idAlumno(), request.idGrupo()))
            throw new ConflictoException("El alumno ya esta inscrito en este grupo");
    }

    public void validarCambiosUnicos(InscripcionRequest request, Long id) {
        if (inscripcionRepository.existsByAlumnoIdAndGrupoIdAndIdInscripcionNot(
                request.idAlumno(), request.idGrupo(), id))
            throw new ConflictoException("El alumno ya esta inscrito en este grupo");
    }
}
