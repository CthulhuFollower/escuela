package com.julian.escuela.services.calificaciones;

import com.julian.escuela.dto.calificaciones.CalificacionRequest;
import com.julian.escuela.dto.calificaciones.CalificacionResponse;
import com.julian.escuela.entities.Calificacion;
import com.julian.escuela.entities.Inscripcion;
import com.julian.escuela.exceptions.ConflictoException;
import com.julian.escuela.mappers.CalificacionMapper;
import com.julian.escuela.repositories.CalificacionRepository;
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
public class CalificacionServiceImpl implements CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final InscripcionRepository inscripcionRepository;
    private final CalificacionMapper calificacionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionResponse> listar() {
        log.info("Listando las calificaciones");
        return calificacionRepository.findAll().stream().map(calificacionMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CalificacionResponse obtenerPorId(Long id) {
        return calificacionMapper.entidadAResponse(obtenerCalificacion(id));
    }

    @Override
    public CalificacionResponse registrar(CalificacionRequest request) {
        log.info("Registrando nueva calificacion...");

        Inscripcion inscripcion = obtenerInscripcion(request.idInscripcion());
        validarDatosUnicos(request);

        Calificacion calificacion = calificacionMapper.requestAEntidad(request, inscripcion);
        calificacionRepository.saveAndFlush(calificacion);

        log.info("Calificacion {} registrada", calificacion.getIdCalificacion());
        return calificacionMapper.entidadAResponse(calificacion);
    }

    @Override
    public CalificacionResponse actualizar(CalificacionRequest request, Long id) {
        log.info("Actualizando calificacion con id: {}", id);

        Calificacion calificacion = obtenerCalificacion(id);
        Inscripcion inscripcion = obtenerInscripcion(request.idInscripcion());
        validarCambiosUnicos(request, id);

        calificacion.actualizar(inscripcion, request.calificacion());
        calificacionRepository.saveAndFlush(calificacion);

        log.info("Calificacion {} actualizada", id);
        return calificacionMapper.entidadAResponse(calificacion);
    }

    @Override
    public void eliminar(Long id) {
        Calificacion calificacion = obtenerCalificacion(id);

        log.info("Eliminando calificacion con id: {}", id);
        calificacionRepository.delete(calificacion);
        calificacionRepository.flush();
    }

    private Calificacion obtenerCalificacion(Long id) {
        return ServiceUtils.obtenerEntidadOException(calificacionRepository, id, Calificacion.class);
    }

    private Inscripcion obtenerInscripcion(Long id) {
        return ServiceUtils.obtenerEntidadOException(inscripcionRepository, id, Inscripcion.class);
    }

    public void validarDatosUnicos(CalificacionRequest request) {
        if (calificacionRepository.existsByInscripcion_IdInscripcion(request.idInscripcion()))
            throw new ConflictoException("Ya existe una calificacion para esta inscripcion");
    }

    public void validarCambiosUnicos(CalificacionRequest request, Long id) {
        if (calificacionRepository.existsByInscripcion_IdInscripcionAndIdCalificacionNot(
                request.idInscripcion(), id))
            throw new ConflictoException("Ya existe una calificacion para esta inscripcion");
    }
}
