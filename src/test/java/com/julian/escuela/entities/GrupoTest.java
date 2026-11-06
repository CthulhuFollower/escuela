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

class GrupoTest {
    @Test
    void crear_cursoNulo_lanzaDatoInvalidoException() {
        // Arrange
        Maestro maestro = maestro();
        Aula aula = aula();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class,
                () -> Grupo.crear(null, maestro, aula, "2026-01"));

        // Assert
        assertThat(error.getMessage()).contains("curso");
    }
    @Test
    void crear_datosValidos_retornaGrupo() {
        // Arrange
        Curso curso = curso();
        Maestro maestro = maestro();
        Aula aula = aula();

        // Act
        Grupo grupo = Grupo.crear(curso, maestro, aula, "2026-01");

        // Assert
        assertThat(grupo.getCurso()).isSameAs(curso);
        assertThat(grupo.getMaestro()).isSameAs(maestro);
        assertThat(grupo.getAula()).isSameAs(aula);
        assertThat(grupo.getPeriodo()).isEqualTo("2026-01");
    }
    @Test
    void crear_periodoNulo_lanzaDatoInvalidoException() {
        // Arrange
        Curso curso = curso();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> Grupo.crear(curso, maestro(), aula(), null));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void actualizar_datosValidos_actualizaCampos() {
        // Arrange
        Grupo grupo = grupo();
        Aula nueva = Aula.crear("Aula 202", 40);

        // Act
        grupo.actualizar(curso(), maestro(), nueva, "2026-02");

        // Assert
        assertThat(grupo.getAula()).isSameAs(nueva);
        assertThat(grupo.getPeriodo()).isEqualTo("2026-02");
    }
    @Test
    void actualizar_periodoVacio_lanzaDatoInvalidoException() {
        // Arrange
        Grupo grupo = grupo();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> grupo.actualizar(curso(), maestro(), aula(), " "));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
}
