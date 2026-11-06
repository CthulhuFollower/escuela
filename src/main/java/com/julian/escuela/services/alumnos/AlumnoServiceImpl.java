package com.julian.escuela.services.alumnos;

import com.julian.escuela.dto.alumnos.AlumnoResponse;
import com.julian.escuela.dto.alumnos.AlumnosRequest;
import com.julian.escuela.entities.Alumno;
import com.julian.escuela.exceptions.EntidadRelacionadaException;
import com.julian.escuela.mappers.AlumnoMapper;
import com.julian.escuela.repositories.AlumnoRepository;
import com.julian.escuela.repositories.InscripcionRepository;
import com.julian.escuela.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AlumnoServiceImpl implements AlumnoService{

    private final AlumnoRepository alumnoRepository;
    private final AlumnoMapper alumnoMapper;
    private final InscripcionRepository inscripcionRepository;


    @Override
    @Transactional(readOnly = true)
    public List<AlumnoResponse> listar() {
        return alumnoRepository.findAll().stream().map(alumnoMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoResponse obtenerPorId(Long id) {

        return alumnoMapper.entidadAResponse(obtenerAlumno(id));
    }

    @Override
    public AlumnoResponse registrar(AlumnosRequest request) {
        log.info("Listando los alumnos");
        Alumno alumno = alumnoMapper.requestAEntidad(
                request,
                generarEmail(request),
                generarMatricula(request)
        );
        log.info("Alumnos encontrado");
        alumnoRepository.saveAndFlush(alumno);

        log.info("alumno guardado");

        return alumnoMapper.entidadAResponse(alumno);
    }

    @Override
    public AlumnoResponse actualizar(AlumnosRequest request, Long id) {

        Alumno alumno = obtenerAlumno(id);

        if (alumno.cambioEnDatos(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno()
        )){
            alumno.actualizar(
                    request.nombre(),
                    request.apellidoPaterno(),
                    request.apellidoMaterno(),
                    generarEmail(request),
                    generarMatricula(request)
            );

            alumnoRepository.saveAndFlush(alumno);
        }

        return alumnoMapper.entidadAResponse(alumno);
    }

    @Override
    public void eliminar(Long id) {

        Alumno alumno = obtenerAlumno(id);

        if (inscripcionRepository.existsByAlumnoId(id))
            throw new EntidadRelacionadaException("No se puede eliminar el alumno ya que tiene inscripciones asignadas");

        alumnoRepository.delete(alumno);
        alumnoRepository.flush();
    }

    private Alumno obtenerAlumno(Long id){
        return ServiceUtils.obtenerEntidadOException(alumnoRepository, id, Alumno.class);
    }

    private String generarEmail(AlumnosRequest request){
        return alumnoRepository.generarEmail(
                request.nombre().trim(),
                request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim()
        );
    }

    private String generarMatricula(AlumnosRequest request){
        return alumnoRepository.generarMatricula(
                request.nombre().trim(),
                request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim()
        );
    }
}
