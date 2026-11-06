package com.julian.escuela.services;

import com.julian.escuela.entities.*;
import com.julian.escuela.enums.DiaSemana;
import com.julian.escuela.dto.alumnos.*;
import com.julian.escuela.dto.maestros.*;
import com.julian.escuela.dto.aula.*;
import com.julian.escuela.dto.cursos.*;
import com.julian.escuela.dto.grupos.*;
import com.julian.escuela.dto.horarios.*;
import com.julian.escuela.dto.inscripciones.*;
import com.julian.escuela.dto.calificaciones.*;
import com.julian.escuela.exceptions.*;
import com.julian.escuela.mappers.*;
import com.julian.escuela.repositories.*;
import com.julian.escuela.services.horarios.HorarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static com.julian.escuela.support.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HorarioServiceTest {
    @Mock HorarioRepository horarioRepository;
    @Mock GrupoRespository grupoRepository;
    @Mock HorariosMapper horariosMapper;
    @InjectMocks HorarioServiceImpl service;

    @Test
    void registrar_datosValidos_registraHorario() {
        // Arrange
        Horario entidad = horario();
        HorariosRequest request = new HorariosRequest(1L, "Lunes", "08:00", "10:00");
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad.getGrupo()));
        when(horariosMapper.requestAEntidad(request, entidad.getGrupo())).thenReturn(entidad);
        HorariosResponse esperado = respuesta();
        when(horariosMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        HorariosResponse resultado = service.registrar(request);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(horarioRepository).saveAndFlush(entidad);
    }
    @Test
    void obtenerPorId_horarioExistente_retornaHorario() {
        // Arrange
        Horario entidad = horario();
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(entidad));
        HorariosResponse esperado = respuesta();
        when(horariosMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        HorariosResponse resultado = service.obtenerPorId(1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
    }
    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        when(horarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(99L));

        // Assert
        assertThat(error.getMessage()).contains("99");
    }
    @Test
    void listar_conHorarios_retornaLista() {
        // Arrange
        Horario entidad = horario();
        when(horarioRepository.findAll()).thenReturn(List.of(entidad));
        HorariosResponse esperado = respuesta();
        when(horariosMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        List<HorariosResponse> resultado = service.listar();

        // Assert
        assertThat(resultado).containsExactly(esperado);
    }
    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        // Arrange
        IllegalStateException causa = new IllegalStateException("fallo de repositorio");
        when(horarioRepository.findAll()).thenThrow(causa);

        // Act
        IllegalStateException error = assertThrows(IllegalStateException.class, () -> service.listar());

        // Assert
        assertThat(error).isSameAs(causa);
    }
    @Test
    void actualizar_datosValidos_actualizaHorario() {
        // Arrange
        Horario entidad = horario();
        HorariosRequest request = new HorariosRequest(1L, "Lunes", "10:00", "12:00");
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad.getGrupo()));
        when(horariosMapper.requestAEntidad(request, entidad.getGrupo())).thenAnswer(invocacion ->
                Horario.crear(entidad.getGrupo(), DiaSemana.LUNES, request.horaInicio(), request.horaFin()));
        HorariosResponse esperado = respuesta();
        when(horariosMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        HorariosResponse resultado = service.actualizar(request, 1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(horarioRepository).saveAndFlush(entidad);
        verify(horarioRepository).existeTraslapePorGrupo(1L, DiaSemana.LUNES, "10:00", "12:00", 1L);
    }
    @Test
    void actualizar_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        HorariosRequest request = new HorariosRequest(1L, "Lunes", "10:00", "12:00");
        when(horarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.actualizar(request, 99L));

        // Assert
        assertThat(error.getMessage()).contains("99");
        verify(horarioRepository, never()).saveAndFlush(any());
    }
    @Test
    void eliminar_horarioExistente_eliminaHorario() {
        // Arrange
        Horario entidad = horario();
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(entidad));

        // Act
        service.eliminar(1L);

        // Assert
        verify(horarioRepository).delete(entidad);
        verify(horarioRepository).flush();
    }
    @Test
    void registrar_traslapeEnElMismoGrupo_lanzaConflictoException() {
        // Arrange
        Horario entidad = horario();
        HorariosRequest request = new HorariosRequest(1L, "Lunes", "08:00", "10:00");
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad.getGrupo()));
        when(horariosMapper.requestAEntidad(request, entidad.getGrupo())).thenReturn(entidad);
        when(horarioRepository.existeTraslapePorGrupo(1L, DiaSemana.LUNES, "08:00", "10:00", null)).thenReturn(true);

        // Act
        ConflictoException error = assertThrows(ConflictoException.class, () -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(horarioRepository, never()).saveAndFlush(any());
    }
    @Test
    void actualizar_aulaOcupadaPorOtroGrupo_lanzaConflictoException() {
        // Arrange
        Horario entidad = horario();
        HorariosRequest request = new HorariosRequest(1L, "Lunes", "10:00", "12:00");
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad.getGrupo()));
        when(horariosMapper.requestAEntidad(request, entidad.getGrupo())).thenAnswer(invocacion ->
                Horario.crear(entidad.getGrupo(), DiaSemana.LUNES, request.horaInicio(), request.horaFin()));
        when(horarioRepository.existeTraslapePorAula(1L, "2026-01", 1L, DiaSemana.LUNES, "10:00", "12:00", 1L)).thenReturn(true);

        // Act
        ConflictoException error = assertThrows(ConflictoException.class, () -> service.actualizar(request, 1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(horarioRepository, never()).saveAndFlush(any());
    }
    @Test
    void eliminar_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        when(horarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(99L));

        // Assert
        assertThat(error.getMessage()).contains("99");
        verify(horarioRepository, never()).delete(any());
    }
    @Test
    void registrar_grupoInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        HorariosRequest request = new HorariosRequest(1L, "Lunes", "08:00", "10:00");
        when(grupoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(horarioRepository, never()).saveAndFlush(any());
    }
    @Test
    void registrar_horaInicioMayorAHoraFin_lanzaDatoInvalidoException() {
        // Arrange
        Horario entidad = horario();
        HorariosRequest request = new HorariosRequest(1L, "Lunes", "10:00", "08:00");
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad.getGrupo()));
        when(horariosMapper.requestAEntidad(request, entidad.getGrupo())).thenAnswer(invocacion ->
                Horario.crear(entidad.getGrupo(), DiaSemana.LUNES, request.horaInicio(), request.horaFin()));

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).contains("posterior");
        verify(horarioRepository, never()).saveAndFlush(any());
    }
    @Test
    void registrar_horaInicioIgualAHoraFin_lanzaDatoInvalidoException() {
        // Arrange
        Horario entidad = horario();
        HorariosRequest request = new HorariosRequest(1L, "Lunes", "10:00", "10:00");
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad.getGrupo()));
        when(horariosMapper.requestAEntidad(request, entidad.getGrupo())).thenAnswer(invocacion ->
                Horario.crear(entidad.getGrupo(), DiaSemana.LUNES, request.horaInicio(), request.horaFin()));

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).contains("posterior");
        verify(horarioRepository, never()).saveAndFlush(any());
    }
    @Test
    void registrar_mismoGrupoDatosIgualesExceptoPeriodo_registraExitosamente() {
        // Arrange
        Horario entidad = horario();
        entidad.getGrupo().setPeriodo("2026-02");
        HorariosRequest request = new HorariosRequest(1L, "Lunes", "08:00", "10:00");
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad.getGrupo()));
        when(horariosMapper.requestAEntidad(request, entidad.getGrupo())).thenReturn(entidad);
        HorariosResponse esperado = respuesta();
        when(horariosMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        HorariosResponse resultado = service.registrar(request);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(horarioRepository).existeTraslapePorAula(1L, "2026-02", 1L, DiaSemana.LUNES, "08:00", "10:00", null);
        verify(horarioRepository).saveAndFlush(entidad);
    }
    @Test
    void actualizar_horaInicioMayorAHoraFin_lanzaDatoInvalidoException() {
        // Arrange
        Horario entidad = horario();
        HorariosRequest request = new HorariosRequest(1L, "Lunes", "10:00", "08:00");
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad.getGrupo()));
        when(horariosMapper.requestAEntidad(request, entidad.getGrupo())).thenAnswer(invocacion ->
                Horario.crear(entidad.getGrupo(), DiaSemana.LUNES, request.horaInicio(), request.horaFin()));

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> service.actualizar(request, 1L));

        // Assert
        assertThat(error.getMessage()).contains("posterior");
        verify(horarioRepository, never()).saveAndFlush(any());
    }
    @Test
    void actualizar_horaInicioIgualAHoraFin_lanzaDatoInvalidoException() {
        // Arrange
        Horario entidad = horario();
        HorariosRequest request = new HorariosRequest(1L, "Lunes", "10:00", "10:00");
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad.getGrupo()));
        when(horariosMapper.requestAEntidad(request, entidad.getGrupo())).thenAnswer(invocacion ->
                Horario.crear(entidad.getGrupo(), DiaSemana.LUNES, request.horaInicio(), request.horaFin()));

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> service.actualizar(request, 1L));

        // Assert
        assertThat(error.getMessage()).contains("posterior");
        verify(horarioRepository, never()).saveAndFlush(any());
    }
    @Test
    void actualizar_mismoGrupoDatosIgualesExceptoPeriodo_actualizaExitosamente() {
        // Arrange
        Horario entidad = horario();
        entidad.getGrupo().setPeriodo("2026-02");
        HorariosRequest request = new HorariosRequest(1L, "Lunes", "08:00", "10:00");
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad.getGrupo()));
        when(horariosMapper.requestAEntidad(request, entidad.getGrupo())).thenReturn(entidad);
        HorariosResponse esperado = respuesta();
        when(horariosMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        HorariosResponse resultado = service.actualizar(request, 1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(horarioRepository).existeTraslapePorAula(1L, "2026-02", 1L, DiaSemana.LUNES, "08:00", "10:00", 1L);
        verify(horarioRepository).saveAndFlush(entidad);
    }
    @Test
    void registrar_aulaOcupadaPorOtroGrupo_lanzaConflictoException() {
        // Arrange
        Horario entidad = horario();
        HorariosRequest request = new HorariosRequest(1L, "Lunes", "08:00", "10:00");
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad.getGrupo()));
        when(horariosMapper.requestAEntidad(request, entidad.getGrupo())).thenReturn(entidad);
        when(horarioRepository.existeTraslapePorAula(1L, "2026-01", 1L, DiaSemana.LUNES, "08:00", "10:00", null)).thenReturn(true);

        // Act
        ConflictoException error = assertThrows(ConflictoException.class, () -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).contains("aula");
        verify(horarioRepository, never()).saveAndFlush(any());
    }

    private HorariosResponse respuesta() {
        return new HorariosResponse(1L, null, "Lunes 08:00 10:00");
    }
}
