package com.julian.escuela.entities;

import com.julian.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalificacionTest {

    @Test
    void crear_debeCrearCalificacion_cuandoLaCalificacionEsCero() {
        // Arrange
        Inscripcion inscripcion = crearInscripcion();
        BigDecimal valor = new BigDecimal("0.0");

        // Act
        Calificacion calificacion = Calificacion.crear(inscripcion, valor);

        // Assert
        assertThat(calificacion.getIdCalificacion()).isNull();
        assertThat(calificacion.getInscripcion()).isSameAs(inscripcion);
        assertThat(calificacion.getCalificacion()).isEqualByComparingTo("0.0");
        assertThat(calificacion.getFechaRegistro()).isNotNull();
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoLaCalificacionEsMayorQueDiez() {
        // Arrange
        Inscripcion inscripcion = crearInscripcion();
        BigDecimal valor = new BigDecimal("11.0");

        // Act + Assert
        assertThatThrownBy(() -> Calificacion.crear(inscripcion, valor))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessage("La calificacion debe estar entre 0 y 10");
    }

    private Inscripcion crearInscripcion() {
        Alumno alumno = Alumno.crear("Juan", "Perez", "Lopez", "juan@alumnos.com", "A2026001");
        Curso curso = Curso.crear("Matematicas I", null, 6);
        Maestro maestro = Maestro.crear("Laura", "Martinez", "Lopez", "laura@escuela.com", "5551010789");
        Aula aula = Aula.crear("Aula 101", 30);
        Grupo grupo = Grupo.crear(curso, maestro, aula, "2026-2");
        return Inscripcion.crear(alumno, grupo);
    }
}
