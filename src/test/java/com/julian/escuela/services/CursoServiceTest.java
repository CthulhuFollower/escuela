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
import com.julian.escuela.services.cursos.CursoServiceImpl;
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
class CursoServiceTest {
    @Mock CursoRepository cursoRepository;
    @Mock GrupoRespository grupoRepository;
    @Mock CursoMapper cursoMapper;
    @InjectMocks CursoServiceImpl service;

    @Test
    void registrar_datosValidos_registraCurso() {
        // Arrange
        Curso entidad = curso();
        CursoRequest request = new CursoRequest("Matematicas I", "Fundamentos", 6);
        when(cursoMapper.requestAEntidad(request)).thenReturn(entidad);
        CursoResponse esperado = respuesta();
        when(cursoMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        CursoResponse resultado = service.registrar(request);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(cursoRepository).saveAndFlush(entidad);
    }
    @Test
    void obtenerPorId_cursoExistente_retornaCurso() {
        // Arrange
        Curso entidad = curso();
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        CursoResponse esperado = respuesta();
        when(cursoMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        CursoResponse resultado = service.obtenerPorId(1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
    }
    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(99L));

        // Assert
        assertThat(error.getMessage()).contains("99");
    }
    @Test
    void listar_conCursos_retornaLista() {
        // Arrange
        Curso entidad = curso();
        when(cursoRepository.findAll()).thenReturn(List.of(entidad));
        CursoResponse esperado = respuesta();
        when(cursoMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        List<CursoResponse> resultado = service.listar();

        // Assert
        assertThat(resultado).containsExactly(esperado);
    }
    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        // Arrange
        IllegalStateException causa = new IllegalStateException("fallo de repositorio");
        when(cursoRepository.findAll()).thenThrow(causa);

        // Act
        IllegalStateException error = assertThrows(IllegalStateException.class, () -> service.listar());

        // Assert
        assertThat(error).isSameAs(causa);
    }
    @Test
    void actualizar_mismoNombrePropio_noLanzaConflicto() {
        // Arrange
        Curso entidad = curso();
        CursoRequest request = new CursoRequest("Matematicas I", null, 8);
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        CursoResponse esperado = respuesta();
        when(cursoMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        CursoResponse resultado = service.actualizar(request, 1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(cursoRepository).saveAndFlush(entidad);
    }
    @Test
    void actualizar_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        CursoRequest request = new CursoRequest("Matematicas I", null, 8);
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.actualizar(request, 99L));

        // Assert
        assertThat(error.getMessage()).contains("99");
        verify(cursoRepository, never()).saveAndFlush(any());
    }
    @Test
    void eliminar_sinGrupos_eliminaCurso() {
        // Arrange
        Curso entidad = curso();
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(entidad));

        // Act
        service.eliminar(1L);

        // Assert
        verify(cursoRepository).delete(entidad);
        verify(cursoRepository).flush();
    }
    @Test
    void registrar_nombreDuplicado_lanzaConflictoException() {
        // Arrange
        Curso entidad = curso();
        CursoRequest request = new CursoRequest("Matematicas I", "Fundamentos", 6);
        when(cursoRepository.existsByNombreIgnoreCase("Matematicas I")).thenReturn(true);

        // Act
        ConflictoException error = assertThrows(ConflictoException.class, () -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(cursoRepository, never()).saveAndFlush(any());
    }
    @Test
    void actualizar_nombreDeOtroCurso_lanzaConflictoException() {
        // Arrange
        Curso entidad = curso();
        CursoRequest request = new CursoRequest("Matematicas I", null, 8);
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(cursoRepository.existsByNombreIgnoreCaseAndIdCursoNot("Matematicas I", 1L)).thenReturn(true);

        // Act
        ConflictoException error = assertThrows(ConflictoException.class, () -> service.actualizar(request, 1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(cursoRepository, never()).saveAndFlush(any());
    }
    @Test
    void eliminar_conGrupos_lanzaEntidadRelacionadaException() {
        // Arrange
        Curso entidad = curso();
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(grupoRepository.existsByCurso_IdCurso(1L)).thenReturn(true);

        // Act
        EntidadRelacionadaException error = assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(cursoRepository, never()).delete(any());
    }

    private CursoResponse respuesta() {
        return new CursoResponse(1L, "Matematicas I", "Fundamentos", 6);
    }
}
