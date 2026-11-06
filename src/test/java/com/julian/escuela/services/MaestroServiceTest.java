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
import com.julian.escuela.services.maestros.MaestroServiceImpl;
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
class MaestroServiceTest {
    @Mock MaestroRepository maestroRepository;
    @Mock GrupoRespository grupoRepository;
    @Mock MaestroMapper maestroMapper;
    @InjectMocks MaestroServiceImpl service;

    @Test
    void registrar_datosValidos_registraMaestro() {
        // Arrange
        Maestro entidad = maestro();
        MaestroRequest request = new MaestroRequest("Laura", "Martinez", "Lopez", "laura@escuela.com", "5512345678");
        when(maestroMapper.requestAEntidad(request)).thenReturn(entidad);
        MaestroResponse esperado = respuesta();
        when(maestroMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        MaestroResponse resultado = service.registrar(request);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(maestroRepository).saveAndFlush(entidad);
    }
    @Test
    void obtenerPorId_maestroExistente_retornaMaestroConCursos() {
        // Arrange
        Maestro entidad = maestro();
        when(maestroRepository.findById(1L)).thenReturn(Optional.of(entidad));
        MaestroResponse esperado = respuesta();
        when(maestroMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        MaestroResponse resultado = service.obtenerPorId(1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
    }
    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        when(maestroRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(99L));

        // Assert
        assertThat(error.getMessage()).contains("99");
    }
    @Test
    void listar_conMaestros_retornaLista() {
        // Arrange
        Maestro entidad = maestro();
        when(maestroRepository.findAll()).thenReturn(List.of(entidad));
        MaestroResponse esperado = respuesta();
        when(maestroMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        List<MaestroResponse> resultado = service.listar();

        // Assert
        assertThat(resultado).containsExactly(esperado);
    }
    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        // Arrange
        IllegalStateException causa = new IllegalStateException("fallo de repositorio");
        when(maestroRepository.findAll()).thenThrow(causa);

        // Act
        IllegalStateException error = assertThrows(IllegalStateException.class, () -> service.listar());

        // Assert
        assertThat(error).isSameAs(causa);
    }
    @Test
    void actualizar_mismoEmailYTelefonoPropios_noLanzaConflicto() {
        // Arrange
        Maestro entidad = maestro();
        MaestroRequest request = new MaestroRequest("Laura", "Martinez", "Lopez", "laura@escuela.com", "5512345678");
        when(maestroRepository.findById(1L)).thenReturn(Optional.of(entidad));
        MaestroResponse esperado = respuesta();
        when(maestroMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        MaestroResponse resultado = service.actualizar(request, 1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(maestroRepository).saveAndFlush(entidad);
    }
    @Test
    void actualizar_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        MaestroRequest request = new MaestroRequest("Laura", "Martinez", "Lopez", "laura@escuela.com", "5512345678");
        when(maestroRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.actualizar(request, 99L));

        // Assert
        assertThat(error.getMessage()).contains("99");
        verify(maestroRepository, never()).saveAndFlush(any());
    }
    @Test
    void eliminar_sinGrupos_eliminaMaestro() {
        // Arrange
        Maestro entidad = maestro();
        when(maestroRepository.findById(1L)).thenReturn(Optional.of(entidad));

        // Act
        service.eliminar(1L);

        // Assert
        verify(maestroRepository).delete(entidad);
        verify(maestroRepository).flush();
    }
    @Test
    void registrar_emailDuplicado_lanzaConflictoException() {
        // Arrange
        Maestro entidad = maestro();
        MaestroRequest request = new MaestroRequest("Laura", "Martinez", "Lopez", "laura@escuela.com", "5512345678");
        when(maestroRepository.existsByEmailIgnoreCase("laura@escuela.com")).thenReturn(true);

        // Act
        ConflictoException error = assertThrows(ConflictoException.class, () -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(maestroRepository, never()).saveAndFlush(any());
    }
    @Test
    void actualizar_emailDeOtroMaestro_lanzaConflictoException() {
        // Arrange
        Maestro entidad = maestro();
        MaestroRequest request = new MaestroRequest("Laura", "Martinez", "Lopez", "laura@escuela.com", "5512345678");
        when(maestroRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(maestroRepository.existsByEmailIgnoreCaseAndIdMaestroNot("laura@escuela.com", 1L)).thenReturn(true);

        // Act
        ConflictoException error = assertThrows(ConflictoException.class, () -> service.actualizar(request, 1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(maestroRepository, never()).saveAndFlush(any());
    }
    @Test
    void eliminar_conGrupos_lanzaEntidadRelacionadaException() {
        // Arrange
        Maestro entidad = maestro();
        when(maestroRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(grupoRepository.existsByMaestro_IdMaestro(1L)).thenReturn(true);

        // Act
        EntidadRelacionadaException error = assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(maestroRepository, never()).delete(any());
    }
    @Test
    void registrar_telefonoDuplicado_lanzaConflictoException() {
        // Arrange
        MaestroRequest request = new MaestroRequest("Laura", "Martinez", "Lopez", "laura@escuela.com", "5512345678");
        when(maestroRepository.existsByTelefono("5512345678")).thenReturn(true);

        // Act
        ConflictoException error = assertThrows(ConflictoException.class, () -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).contains("telefono");
        verify(maestroRepository, never()).saveAndFlush(any());
    }

    private MaestroResponse respuesta() {
        return new MaestroResponse(1L, "Laura Martinez Lopez", "laura@escuela.com", "5512345678", List.of());
    }
}
