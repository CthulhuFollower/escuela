package com.julian.escuela.services.aula;

import com.julian.escuela.dto.aula.AulaRequest;
import com.julian.escuela.dto.aula.AulaResponse;
import com.julian.escuela.entities.Aula;
import com.julian.escuela.exceptions.ConflictoException;
import com.julian.escuela.exceptions.EntidadRelacionadaException;
import com.julian.escuela.mappers.AulaMapper;
import com.julian.escuela.repositories.AulaRepository;
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
public class AulaServiceImpl implements AulaService{

    private final AulaRepository aulaRepository;
    private final AulaMapper aulaMapper;
    private final GrupoRespository grupoRepository;

    @Override
    @Transactional(readOnly = true)
    public AulaResponse obtenerPorId(Long id) {
        log.info("Obteniendo aula por id {}", id);
        return aulaMapper.entidadAResponse(obtenerAula(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AulaResponse> listar() {
        log.info("Listando las aulas");
        return aulaRepository.findAll().stream().map(aulaMapper::entidadAResponse).toList();
    }

    @Override
    public AulaResponse registrar(AulaRequest aulaRequest) {

        log.info("Registrando nuevo aula...");

        validarDatosUnicos(aulaRequest);

        Aula aula = aulaMapper.requestAEntidad(aulaRequest);

        aulaRepository.saveAndFlush(aula);

        log.info("Nueva aula {} registrada", aula.getNombre());

        return aulaMapper.entidadAResponse(aula);
    }

    @Override
    public AulaResponse actualizar(AulaRequest request, Long id) {

        log.info("Actualizando aula con id: {}", id);

        Aula aula = obtenerAula(id);

        validarCambiosUnicos(request, id);

        aula.actualizar(
                request.nombre(),
                request.capacidad()
        );

        aulaRepository.saveAndFlush(aula);

        log.info("Aula {} actualizada", aula.getNombre());

        return aulaMapper.entidadAResponse(aula);
    }

    @Override
    public void eliminar(Long id) {
        Aula aula = obtenerAula(id);

        if (grupoRepository.existsByAulaId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el aula ya que tiene grupos asignados"
            );

        log.info("Eliminando aula con id: {}", id);

        aulaRepository.delete(aula);
        aulaRepository.flush();
    }

    private Aula obtenerAula(Long id){
        return ServiceUtils.obtenerEntidadOException(aulaRepository, id, Aula.class);
    }

    public void validarDatosUnicos(AulaRequest request){
        log.info("Validando nombre unico...");
        if (aulaRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new ConflictoException("Ya existe un aula registrada con el nombre: " + request.nombre());
    }

    public void validarCambiosUnicos(AulaRequest request, Long id){
        log.info("Validando cambio en nombre unico...");
        if (aulaRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(), id))
            throw new ConflictoException("Ya existe un aula registrada con el nombre: " + request.nombre());

    }
}
