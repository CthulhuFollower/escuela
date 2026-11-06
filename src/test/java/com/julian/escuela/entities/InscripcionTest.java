package com.julian.escuela.entities;

import com.julian.escuela.exceptions.DatoInvalidoException;
import com.julian.escuela.enums.DiaSemana;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import static com.julian.escuela.support.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InscripcionTest {
    @Test
    void crear_alumnoYGrupoValidos_retornaInscripcionConFechaDeHoy() {
        // Arrange
        Alumno alumno = alumno();
        Grupo grupo = grupo();

        // Act
        Inscripcion inscripcion = Inscripcion.crear(alumno, grupo);

        // Assert
        assertThat(inscripcion.getAlumno()).isSameAs(alumno);
        assertThat(inscripcion.getGrupo()).isSameAs(grupo);
        assertThat(inscripcion.getFechaInscripcion().toLocalDate()).isEqualTo(LocalDate.now());
        assertThat(inscripcion.getCalificacion()).isNull();
    }
    @Test
    void crear_alumnoNulo_lanzaDatoInvalidoException() {
        // Arrange
        Grupo grupo = grupo();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> Inscripcion.crear(null, grupo));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void actualizar_datosValidos_actualizaAlumnoYGrupo() {
        // Arrange
        Inscripcion inscripcion = inscripcion();
        Grupo nuevo = grupo();
        nuevo.setId(2L);

        // Act
        inscripcion.actualizar(alumno(), nuevo);

        // Assert
        assertThat(inscripcion.getGrupo()).isSameAs(nuevo);
    }
    @Test
    void actualizar_grupoNulo_lanzaDatoInvalidoException() {
        // Arrange
        Inscripcion inscripcion = inscripcion();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> inscripcion.actualizar(alumno(), null));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
}
