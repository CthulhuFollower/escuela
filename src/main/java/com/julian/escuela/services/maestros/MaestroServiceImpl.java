package com.julian.escuela.services.maestros;

import com.julian.escuela.dto.maestros.MaestroRequest;
import com.julian.escuela.dto.maestros.MaestroResponse;
import com.julian.escuela.entities.Maestro;
import com.julian.escuela.exceptions.ConflictoException;
import com.julian.escuela.exceptions.EntidadRelacionadaException;
import com.julian.escuela.exceptions.RecursoNoEncontradoException;
import com.julian.escuela.mappers.MaestroMapper;
import com.julian.escuela.repositories.GrupoRespository;
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
public class MaestroServiceImpl implements MaestroService{

    private final MaestroMapper maestroMapper;
    private final MaestroRepository maestroRepository;
    private final GrupoRespository grupoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listar() {
        log.info("Listando los maestros");
        return maestroRepository.findAll().stream().map(maestroMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MaestroResponse obtenerPorId(Long id) {
        return maestroMapper.entidadAResponse(obtenerMaestro(id));
    }

    @Override
    @Transactional
    public MaestroResponse registrar(MaestroRequest request) {

        log.info("Registrando nuevo maestro...");

        validarDatosUnicos(request);

        Maestro maestro = maestroMapper.requestAEntidad(request);

        maestroRepository.saveAndFlush(maestro);

        log.info("Nuevo maestro {} registrado", maestro.obtenerNombreCompleto());

        return maestroMapper.entidadAResponse(maestro);
    }

    @Override
    @Transactional
    public MaestroResponse actualizar(MaestroRequest request, Long id) {

        validarCambiosUnicos(request, id);

        log.info("Actualizando maestro con id: {}", id);

        Maestro maestro = obtenerMaestro(id);

        maestro.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.email(),
                request.telefono()
        );

        maestroRepository.saveAndFlush(maestro);

        log.info("Maestro {} actualizado", maestro.obtenerNombreCompleto());

        return maestroMapper.entidadAResponse(maestro);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Maestro maestro = obtenerMaestro(id);

        if (grupoRepository.existByMaestroId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el maestro ya que tiene grupos asignados"
            );


        log.info("Eliminando maestro con id: {}", id);

        maestroRepository.delete(maestro);
        maestroRepository.flush();

    }

    private Maestro obtenerMaestro(Long id){
        return ServiceUtils.obtenerEntidadOException(maestroRepository, id, Maestro.class);
    }

    public void validarDatosUnicos(MaestroRequest request){
        log.info("Validando correo unico...");
        if (maestroRepository.existByEmailIgnoreCase(request.email().trim()))
            throw new ConflictoException("Ya existe un maestro registrado con el email: " + request.email());

        log.info("Validando numero unico...");
        if (maestroRepository.existByTelefono(request.telefono().trim()))
            throw new ConflictoException("Ya existe un maestro registrado con el telefono: " + request.telefono());
    }

    public void validarCambiosUnicos(MaestroRequest request, Long id){
        log.info("Validando cambio en correo unico...");
        if (maestroRepository.existsByEmailIgnoreCaseAndIdNot(request.email().trim(), id))
            throw new ConflictoException("Ya existe un maestro registrado con el email: " + request.email());

        log.info("Validando cambio en numero unico...");
        if (maestroRepository.existsByTelefonoAndIdNot(request.telefono().trim(), id))
            throw new ConflictoException("Ya existe un maestro registrado con el telefono: " + request.telefono());
    }
}
