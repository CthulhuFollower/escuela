package com.julian.escuela.services.horarios;

import com.julian.escuela.dto.horarios.HorariosRequest;
import com.julian.escuela.dto.horarios.HorariosResponse;
import com.julian.escuela.entities.Grupo;
import com.julian.escuela.entities.Horario;
import com.julian.escuela.exceptions.ConflictoException;
import com.julian.escuela.mappers.HorariosMapper;
import com.julian.escuela.repositories.GrupoRespository;
import com.julian.escuela.repositories.HorarioRepository;
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
public class HorarioServiceImpl implements HorariosService {

    private final HorarioRepository horarioRepository;
    private final GrupoRespository grupoRepository;
    private final HorariosMapper horariosMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HorariosResponse> listar() {
        log.info("Listando los horarios");
        return horarioRepository.findAll().stream().map(horariosMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HorariosResponse obtenerPorId(Long id) {
        return horariosMapper.entidadAResponse(obtenerHorario(id));
    }

    @Override
    public HorariosResponse registrar(HorariosRequest request) {
        log.info("Registrando nuevo horario...");

        Grupo grupo = obtenerGrupo(request.idGrupo());
        Horario horario = horariosMapper.requestAEntidad(request, grupo);
        validarTraslapes(horario, null);

        horarioRepository.saveAndFlush(horario);

        log.info("Horario {} registrado", horario.getId());
        return horariosMapper.entidadAResponse(horario);
    }

    @Override
    public HorariosResponse actualizar(HorariosRequest request, Long id) {
        log.info("Actualizando horario con id: {}", id);

        Horario horario = obtenerHorario(id);
        Grupo grupo = obtenerGrupo(request.idGrupo());
        Horario cambios = horariosMapper.requestAEntidad(request, grupo);
        validarTraslapes(cambios, id);

        horario.actualizar(grupo, cambios.getDia(), cambios.getHoraInicio(), cambios.getHoraFin());
        horarioRepository.saveAndFlush(horario);

        log.info("Horario {} actualizado", id);
        return horariosMapper.entidadAResponse(horario);
    }

    @Override
    public void eliminar(Long id) {
        Horario horario = obtenerHorario(id);

        log.info("Eliminando horario con id: {}", id);
        horarioRepository.delete(horario);
        horarioRepository.flush();
    }

    private Horario obtenerHorario(Long id) {
        return ServiceUtils.obtenerEntidadOException(horarioRepository, id, Horario.class);
    }

    private Grupo obtenerGrupo(Long id) {
        return ServiceUtils.obtenerEntidadOException(grupoRepository, id, Grupo.class);
    }

    private void validarTraslapes(Horario horario, Long idExcluido) {
        Grupo grupo = horario.getGrupo();

        if (horarioRepository.existeTraslapePorGrupo(
                grupo.getId(), horario.getDia(), horario.getHoraInicio(), horario.getHoraFin(), idExcluido))
            throw new ConflictoException("El horario se traslapa con otro horario del mismo grupo");

        if (horarioRepository.existeTraslapePorAula(
                grupo.getAula().getId(), grupo.getPeriodo(), grupo.getId(),
                horario.getDia(), horario.getHoraInicio(), horario.getHoraFin(), idExcluido))
            throw new ConflictoException("El horario se traslapa con otro grupo de la misma aula y periodo");
    }
}
