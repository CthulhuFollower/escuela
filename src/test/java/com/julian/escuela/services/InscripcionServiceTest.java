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
import com.julian.escuela.services.inscripciones.InscripcionServiceImpl;
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
class InscripcionServiceTest {
    @Mock InscripcionRepository inscripcionRepository;
    @Mock AlumnoRepository alumnoRepository;
    @Mock GrupoRespository grupoRepository;
    @Mock InscripcionMapper inscripcionMapper;
    @InjectMocks InscripcionServiceImpl service;

    @Test
    void registrar_datosValidos_registraInscripcion() {
        // Arrange
        Inscripcion entidad = inscripcion();
        InscripcionRequest request = new InscripcionRequest(1L, 1L);
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(entidad.getAlumno()));
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad.getGrupo()));
        when(inscripcionMapper.requestAEntidad(request, entidad.getAlumno(), entidad.getGrupo())).thenReturn(entidad);
        InscripcionResponse esperado = respuesta();
        when(inscripcionMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        InscripcionResponse resultado = service.registrar(request);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(inscripcionRepository).saveAndFlush(entidad);
    }
    @Test
    void obtenerPorId_inscripcionExistente_retornaInscripcion() {
        // Arrange
        Inscripcion entidad = inscripcion();
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(entidad));
        InscripcionResponse esperado = respuesta();
        when(inscripcionMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        InscripcionResponse resultado = service.obtenerPorId(1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
    }
    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        when(inscripcionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(99L));

        // Assert
        assertThat(error.getMessage()).contains("99");
    }
    @Test
    void listar_conInscripciones_retornaLista() {
        // Arrange
        Inscripcion entidad = inscripcion();
        when(inscripcionRepository.findAll()).thenReturn(List.of(entidad));
        InscripcionResponse esperado = respuesta();
        when(inscripcionMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        List<InscripcionResponse> resultado = service.listar();

        // Assert
        assertThat(resultado).containsExactly(esperado);
    }
    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        // Arrange
        IllegalStateException causa = new IllegalStateException("fallo de repositorio");
        when(inscripcionRepository.findAll()).thenThrow(causa);

        // Act
        IllegalStateException error = assertThrows(IllegalStateException.class, () -> service.listar());

        // Assert
        assertThat(error).isSameAs(causa);
    }
    @Test
    void actualizar_otroGrupoValido_actualizaInscripcion() {
        // Arrange
        Inscripcion entidad = inscripcion();
        Grupo nuevoGrupo = grupo();
        nuevoGrupo.setId(2L);
        InscripcionRequest request = new InscripcionRequest(1L, 2L);
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(entidad.getAlumno()));
        when(grupoRepository.findById(2L)).thenReturn(Optional.of(nuevoGrupo));
        InscripcionResponse esperado = respuesta();
        when(inscripcionMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        InscripcionResponse resultado = service.actualizar(request, 1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        assertThat(entidad.getGrupo()).isSameAs(nuevoGrupo);
        verify(inscripcionRepository).saveAndFlush(entidad);
    }
    @Test
    void actualizar_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        InscripcionRequest request = new InscripcionRequest(1L, 1L);
        when(inscripcionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.actualizar(request, 99L));

        // Assert
        assertThat(error.getMessage()).contains("99");
        verify(inscripcionRepository, never()).saveAndFlush(any());
    }
    @Test
    void eliminar_sinCalificacion_eliminaInscripcion() {
        // Arrange
        Inscripcion entidad = inscripcion();
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(entidad));

        // Act
        service.eliminar(1L);

        // Assert
        verify(inscripcionRepository).delete(entidad);
        verify(inscripcionRepository).flush();
    }
    @Test
    void registrar_alumnoYGrupoDuplicados_lanzaConflictoException() {
        // Arrange
        Inscripcion entidad = inscripcion();
        InscripcionRequest request = new InscripcionRequest(1L, 1L);
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(entidad.getAlumno()));
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad.getGrupo()));
        when(inscripcionRepository.existsByAlumnoIdAndGrupoId(1L, 1L)).thenReturn(true);

        // Act
        ConflictoException error = assertThrows(ConflictoException.class, () -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(inscripcionRepository, never()).saveAndFlush(any());
    }
    @Test
    void actualizar_combinacionYaExistente_lanzaConflictoException() {
        // Arrange
        Inscripcion entidad = inscripcion();
        InscripcionRequest request = new InscripcionRequest(1L, 1L);
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(entidad.getAlumno()));
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(entidad.getGrupo()));
        when(inscripcionRepository.existsByAlumnoIdAndGrupoIdAndIdInscripcionNot(1L, 1L, 1L)).thenReturn(true);

        // Act
        ConflictoException error = assertThrows(ConflictoException.class, () -> service.actualizar(request, 1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(inscripcionRepository, never()).saveAndFlush(any());
    }
    @Test
    void eliminar_conCalificacion_lanzaEntidadRelacionadaException() {
        // Arrange
        Inscripcion entidad = inscripcion();
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(entidad));
        entidad.setCalificacion(calificacion());

        // Act
        EntidadRelacionadaException error = assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(inscripcionRepository, never()).delete(any());
    }
    @Test
    void registrar_alumnoInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        InscripcionRequest request = new InscripcionRequest(1L, 1L);
        when(alumnoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(inscripcionRepository, never()).saveAndFlush(any());
    }

    private InscripcionResponse respuesta() {
        return new InscripcionResponse(1L, null, null, null, "01/01/2026");
    }
}
