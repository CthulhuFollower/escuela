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
import com.julian.escuela.services.calificaciones.CalificacionServiceImpl;
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
class CalificacionServiceTest {
    @Mock CalificacionRepository calificacionRepository;
    @Mock InscripcionRepository inscripcionRepository;
    @Mock CalificacionMapper calificacionMapper;
    @InjectMocks CalificacionServiceImpl service;

    @Test
    void registrar_datosValidos_registraCalificacion() {
        // Arrange
        Calificacion entidad = calificacion();
        CalificacionRequest request = new CalificacionRequest(1L, new BigDecimal("8.5"));
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(entidad.getInscripcion()));
        when(calificacionMapper.requestAEntidad(request, entidad.getInscripcion())).thenReturn(entidad);
        CalificacionResponse esperado = respuesta();
        when(calificacionMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        CalificacionResponse resultado = service.registrar(request);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(calificacionRepository).saveAndFlush(entidad);
    }
    @Test
    void obtenerPorId_calificacionExistente_retornaCalificacion() {
        // Arrange
        Calificacion entidad = calificacion();
        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(entidad));
        CalificacionResponse esperado = respuesta();
        when(calificacionMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        CalificacionResponse resultado = service.obtenerPorId(1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
    }
    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        when(calificacionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(99L));

        // Assert
        assertThat(error.getMessage()).contains("99");
    }
    @Test
    void listar_conCalificaciones_retornaLista() {
        // Arrange
        Calificacion entidad = calificacion();
        when(calificacionRepository.findAll()).thenReturn(List.of(entidad));
        CalificacionResponse esperado = respuesta();
        when(calificacionMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        List<CalificacionResponse> resultado = service.listar();

        // Assert
        assertThat(resultado).containsExactly(esperado);
    }
    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        // Arrange
        IllegalStateException causa = new IllegalStateException("fallo de repositorio");
        when(calificacionRepository.findAll()).thenThrow(causa);

        // Act
        IllegalStateException error = assertThrows(IllegalStateException.class, () -> service.listar());

        // Assert
        assertThat(error).isSameAs(causa);
    }
    @Test
    void actualizar_valorValido_actualizaCalificacion() {
        // Arrange
        Calificacion entidad = calificacion();
        CalificacionRequest request = new CalificacionRequest(1L, new BigDecimal("9"));
        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(entidad.getInscripcion()));
        CalificacionResponse esperado = respuesta();
        when(calificacionMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        CalificacionResponse resultado = service.actualizar(request, 1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(calificacionRepository).saveAndFlush(entidad);
    }
    @Test
    void actualizar_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        CalificacionRequest request = new CalificacionRequest(1L, new BigDecimal("9"));
        when(calificacionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.actualizar(request, 99L));

        // Assert
        assertThat(error.getMessage()).contains("99");
        verify(calificacionRepository, never()).saveAndFlush(any());
    }
    @Test
    void eliminar_calificacionExistente_eliminaCalificacion() {
        // Arrange
        Calificacion entidad = calificacion();
        when(calificacionRepository.findById(1L)).thenReturn(Optional.of(entidad));

        // Act
        service.eliminar(1L);

        // Assert
        verify(calificacionRepository).delete(entidad);
        verify(calificacionRepository).flush();
    }
    @Test
    void registrar_inscripcionYaCalificada_lanzaConflictoException() {
        // Arrange
        Calificacion entidad = calificacion();
        CalificacionRequest request = new CalificacionRequest(1L, new BigDecimal("8.5"));
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(entidad.getInscripcion()));
        when(calificacionRepository.existsByInscripcion_IdInscripcion(1L)).thenReturn(true);

        // Act
        ConflictoException error = assertThrows(ConflictoException.class, () -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(calificacionRepository, never()).saveAndFlush(any());
    }
    @Test
    void eliminar_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        when(calificacionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(99L));

        // Assert
        assertThat(error.getMessage()).contains("99");
        verify(calificacionRepository, never()).delete(any());
    }
    @Test
    void registrar_inscripcionInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        CalificacionRequest request = new CalificacionRequest(1L, new BigDecimal("8.5"));
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(calificacionRepository, never()).saveAndFlush(any());
    }

    private CalificacionResponse respuesta() {
        return new CalificacionResponse(1L, null, new BigDecimal("8.5"), "01/01/2026");
    }
}
