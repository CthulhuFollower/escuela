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

class CursoTest {
    @Test
    void crear_descripcionNula_retornaCurso() {
        // Arrange
        String nombre = "Matematicas I";

        // Act
        Curso curso = Curso.crear(nombre, null, 6);

        // Assert
        assertThat(curso.getDescripcion()).isNull();
    }
    @Test
    void crear_datosValidos_retornaCurso() {
        // Arrange
        String nombre = " Matematicas I ";

        // Act
        Curso curso = Curso.crear(nombre, " Fundamentos ", 6);

        // Assert
        assertThat(curso.getNombre()).isEqualTo("Matematicas I");
        assertThat(curso.getDescripcion()).isEqualTo("Fundamentos");
        assertThat(curso.getCreditos()).isEqualTo(6);
    }
    @Test
    void crear_creditosNoPositivos_lanzaDatoInvalidoException() {
        // Arrange
        int creditos = 0;

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> Curso.crear("Matematicas I", null, creditos));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void actualizar_datosValidos_actualizaCampos() {
        // Arrange
        Curso curso = curso();

        // Act
        curso.actualizar(" Bases de Datos ", null, 8);

        // Assert
        assertThat(curso.getNombre()).isEqualTo("Bases de Datos");
        assertThat(curso.getDescripcion()).isNull();
        assertThat(curso.getCreditos()).isEqualTo(8);
    }
    @Test
    void actualizar_nombreVacio_lanzaDatoInvalidoException() {
        // Arrange
        Curso curso = curso();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> curso.actualizar(" ", null, 8));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
}
