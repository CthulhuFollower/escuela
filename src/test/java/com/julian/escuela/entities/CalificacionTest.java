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

class CalificacionTest {
    @Test
    void crear_valorValido_retornaCalificacion() {
        // Arrange
        Inscripcion inscripcion = inscripcion();

        // Act
        Calificacion calificacion = Calificacion.crear(inscripcion, new BigDecimal("8.5"));

        // Assert
        assertThat(calificacion.getInscripcion()).isSameAs(inscripcion);
        assertThat(calificacion.getCalificacion()).isEqualByComparingTo("8.5");
        assertThat(calificacion.getFechaRegistro().toLocalDate()).isEqualTo(LocalDate.now());
    }
    @Test
    void crear_valorNulo_lanzaDatoInvalidoException() {
        // Arrange
        Inscripcion inscripcion = inscripcion();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> Calificacion.crear(inscripcion, null));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void actualizar_valorValido_actualizaCalificacion() {
        // Arrange
        Calificacion calificacion = calificacion();

        // Act
        calificacion.actualizar(inscripcion(), new BigDecimal("9"));

        // Assert
        assertThat(calificacion.getCalificacion()).isEqualByComparingTo("9");
    }
    @Test
    void actualizar_valorNulo_lanzaDatoInvalidoException() {
        // Arrange
        Calificacion calificacion = calificacion();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> calificacion.actualizar(inscripcion(), null));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void crear_calificacionCero_retornaCalificacion() {
        // Arrange
        Inscripcion inscripcion = inscripcion();

        // Act
        Calificacion calificacion = Calificacion.crear(inscripcion, BigDecimal.ZERO);

        // Assert
        assertThat(calificacion.getCalificacion()).isEqualByComparingTo(BigDecimal.ZERO);
    }
    @Test
    void crear_valorFueraDeRango_lanzaDatoInvalidoException() {
        // Arrange
        Inscripcion inscripcion = inscripcion();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> Calificacion.crear(inscripcion, new BigDecimal("11")));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
}
