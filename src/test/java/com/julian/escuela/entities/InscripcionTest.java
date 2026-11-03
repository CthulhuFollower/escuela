package com.julian.escuela.entities;

import com.julian.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InscripcionTest {

    @Test
    void crear_debeCrearInscripcion_cuandoLosDatosSonValidos() {
        // Arrange
        Alumno alumno = Alumno.crear("Juan", "Perez", "Lopez", "juan@alumnos.com", "A2026001");
        Grupo grupo = crearGrupo();

        // Act
        Inscripcion inscripcion = Inscripcion.crear(alumno, grupo);

        // Assert
        assertThat(inscripcion.getIdInscripcion()).isNull();
        assertThat(inscripcion.getAlumno()).isSameAs(alumno);
        assertThat(inscripcion.getGrupo()).isSameAs(grupo);
        assertThat(inscripcion.getFechaInscripcion()).isNotNull();
        assertThat(inscripcion.getCalificacion()).isNull();
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoElAlumnoEsNulo() {
        // Arrange
        Alumno alumno = null;
        Grupo grupo = crearGrupo();

        // Act + Assert
        assertThatThrownBy(() -> Inscripcion.crear(alumno, grupo))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessage("El alumno es requerido");
    }

    private Grupo crearGrupo() {
        Curso curso = Curso.crear("Matematicas I", null, 6);
        Maestro maestro = Maestro.crear("Laura", "Martinez", "Lopez", "laura@escuela.com", "5551010789");
        Aula aula = Aula.crear("Aula 101", 30);
        return Grupo.crear(curso, maestro, aula, "2026-2");
    }
}
