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
import com.julian.escuela.services.alumnos.AlumnoServiceImpl;
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
class AlumnoServiceTest {
    @Mock AlumnoRepository alumnoRepository;
    @Mock InscripcionRepository inscripcionRepository;
    @Mock AlumnoMapper alumnoMapper;
    @InjectMocks AlumnoServiceImpl service;

    @Test
    void registrar_datosValidos_generaMatriculaYEmail() {
        // Arrange
        Alumno entidad = alumno();
        AlumnosRequest request = new AlumnosRequest("Juan", "Perez", "Lopez");
        when(alumnoRepository.generarEmail(anyString(), anyString(), anyString())).thenReturn("juan@escuela.com");
        when(alumnoRepository.generarMatricula(anyString(), anyString(), anyString())).thenReturn("PELOJU2601");
        when(alumnoMapper.requestAEntidad(request, "juan@escuela.com", "PELOJU2601")).thenReturn(entidad);
        AlumnoResponse esperado = respuesta();
        when(alumnoMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        AlumnoResponse resultado = service.registrar(request);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(alumnoRepository).saveAndFlush(entidad);
    }
    @Test
    void obtenerPorId_alumnoExistente_retornaAlumnoConPromedio() {
        // Arrange
        Alumno entidad = alumno();
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        AlumnoResponse esperado = respuesta();
        when(alumnoMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        AlumnoResponse resultado = service.obtenerPorId(1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
    }
    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        when(alumnoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(99L));

        // Assert
        assertThat(error.getMessage()).contains("99");
    }
    @Test
    void listar_conAlumnos_retornaLista() {
        // Arrange
        Alumno entidad = alumno();
        when(alumnoRepository.findAll()).thenReturn(List.of(entidad));
        AlumnoResponse esperado = respuesta();
        when(alumnoMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        List<AlumnoResponse> resultado = service.listar();

        // Assert
        assertThat(resultado).containsExactly(esperado);
    }
    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        // Arrange
        IllegalStateException causa = new IllegalStateException("fallo de repositorio");
        when(alumnoRepository.findAll()).thenThrow(causa);

        // Act
        IllegalStateException error = assertThrows(IllegalStateException.class, () -> service.listar());

        // Assert
        assertThat(error).isSameAs(causa);
    }
    @Test
    void actualizar_cambiaNombre_regeneraMatriculaYEmail() {
        // Arrange
        Alumno entidad = alumno();
        AlumnosRequest request = new AlumnosRequest("Pedro", "Perez", "Lopez");
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(alumnoRepository.generarEmail(anyString(), anyString(), anyString())).thenReturn("juan@escuela.com");
        when(alumnoRepository.generarMatricula(anyString(), anyString(), anyString())).thenReturn("PELOJU2601");
        AlumnoResponse esperado = respuesta();
        when(alumnoMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        AlumnoResponse resultado = service.actualizar(request, 1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(alumnoRepository).saveAndFlush(entidad);
        verify(alumnoRepository).generarEmail("Pedro", "Perez", "Lopez");
        verify(alumnoRepository).generarMatricula("Pedro", "Perez", "Lopez");
    }
    @Test
    void actualizar_idInexistente_lanzaRecursoNoEncontradoException() {
        // Arrange
        AlumnosRequest request = new AlumnosRequest("Pedro", "Perez", "Lopez");
        when(alumnoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        RecursoNoEncontradoException error = assertThrows(RecursoNoEncontradoException.class, () -> service.actualizar(request, 99L));

        // Assert
        assertThat(error.getMessage()).contains("99");
        verify(alumnoRepository, never()).saveAndFlush(any());
    }
    @Test
    void eliminar_sinInscripciones_eliminaAlumno() {
        // Arrange
        Alumno entidad = alumno();
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(entidad));

        // Act
        service.eliminar(1L);

        // Assert
        verify(alumnoRepository).delete(entidad);
        verify(alumnoRepository).flush();
    }
    @Test
    void eliminar_conInscripciones_lanzaEntidadRelacionadaException() {
        // Arrange
        Alumno entidad = alumno();
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(inscripcionRepository.existsByAlumnoId(1L)).thenReturn(true);

        // Act
        EntidadRelacionadaException error = assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(1L));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(alumnoRepository, never()).delete(any());
    }
    @Test
    void actualizar_sinCambiosEnDatos_noRegeneraNiGuarda() {
        // Arrange
        Alumno entidad = alumno();
        AlumnosRequest request = new AlumnosRequest("Juan", "Perez", "Lopez");
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        AlumnoResponse esperado = respuesta();
        when(alumnoMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        AlumnoResponse resultado = service.actualizar(request, 1L);

        // Assert
        assertThat(resultado).isSameAs(esperado);
        verify(alumnoRepository, never()).generarEmail(anyString(), anyString(), anyString());
        verify(alumnoRepository, never()).generarMatricula(anyString(), anyString(), anyString());
        verify(alumnoRepository, never()).saveAndFlush(any());
    }
    @Test
    void actualizar_cambiaSoloApellidoMaterno_regeneraMatriculaYEmail() {
        // Arrange
        Alumno entidad = alumno();
        AlumnosRequest request = new AlumnosRequest("Juan", "Perez", "Ramos");
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(alumnoRepository.generarEmail(anyString(), anyString(), anyString())).thenReturn("juan@escuela.com");
        when(alumnoRepository.generarMatricula(anyString(), anyString(), anyString())).thenReturn("PELOJU2601");
        AlumnoResponse esperado = respuesta();
        when(alumnoMapper.entidadAResponse(entidad)).thenReturn(esperado);

        // Act
        service.actualizar(request, 1L);

        // Assert
        assertThat(entidad.getApellidoMaterno()).isEqualTo("Ramos");
        verify(alumnoRepository).generarEmail("Juan", "Perez", "Ramos");
        verify(alumnoRepository).generarMatricula("Juan", "Perez", "Ramos");
    }
    @Test
    void registrar_nombreVacio_lanzaDatoInvalidoException() {
        // Arrange
        AlumnosRequest request = new AlumnosRequest(" ", "Perez", "Lopez");
        when(alumnoRepository.generarEmail(anyString(), anyString(), anyString())).thenReturn("juan@escuela.com");
        when(alumnoRepository.generarMatricula(anyString(), anyString(), anyString())).thenReturn("PELOJU2601");
        when(alumnoMapper.requestAEntidad(request, "juan@escuela.com", "PELOJU2601"))
                .thenAnswer(invocacion -> Alumno.crear(request.nombre(), request.apellidoPaterno(), request.apellidoMaterno()));

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> service.registrar(request));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
        verify(alumnoRepository, never()).saveAndFlush(any());
    }
    @Test
    void obtenerPorId_sinCalificaciones_retornaPromedioCero() {
        // Arrange
        Alumno entidad = alumno();
        
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(alumnoMapper.entidadAResponse(entidad)).thenAnswer(invocacion -> new AlumnoMapper().entidadAResponse(entidad));

        // Act
        AlumnoResponse resultado = service.obtenerPorId(1L);

        // Assert
        assertThat(resultado.promedio()).isEqualByComparingTo("0");
    }
    @Test
    void obtenerPorId_conCalificacionesNulas_ignoraNulosEnPromedio() {
        // Arrange
        Alumno entidad = alumno();
        Inscripcion conNota = Inscripcion.crear(entidad, grupo());
        conNota.setCalificacion(Calificacion.crear(conNota, new BigDecimal("8")));
        entidad.setInscripciones(List.of(conNota, Inscripcion.crear(entidad, grupo())));
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(alumnoMapper.entidadAResponse(entidad)).thenAnswer(invocacion -> new AlumnoMapper().entidadAResponse(entidad));

        // Act
        AlumnoResponse resultado = service.obtenerPorId(1L);

        // Assert
        assertThat(resultado.promedio()).isEqualByComparingTo("8");
    }

    private AlumnoResponse respuesta() {
        return new AlumnoResponse(1L, "Juan Perez Lopez", "juan@escuela.com", "PELOJU2601", "01/01/2026", List.of(), BigDecimal.ZERO);
    }
}
