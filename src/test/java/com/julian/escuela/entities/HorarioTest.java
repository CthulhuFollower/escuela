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

class HorarioTest {
    @Test
    void crear_horaInicioMenorQueHoraFin_retornaHorario() {
        // Arrange
        Grupo grupo = grupo();

        // Act
        Horario horario = Horario.crear(grupo, DiaSemana.LUNES, "08:00", "10:00");

        // Assert
        assertThat(horario.getGrupo()).isSameAs(grupo);
        assertThat(horario.getHoraInicio()).isEqualTo("08:00");
        assertThat(horario.getHoraFin()).isEqualTo("10:00");
    }
    @Test
    void crear_horaInicioMayorQueHoraFin_lanzaDatoInvalidoException() {
        // Arrange
        Grupo grupo = grupo();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> Horario.crear(grupo, DiaSemana.LUNES, "10:00", "08:00"));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void crear_horaInicioIgualAHoraFin_lanzaDatoInvalidoException() {
        // Arrange
        Grupo grupo = grupo();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> Horario.crear(grupo, DiaSemana.LUNES, "08:00", "08:00"));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void crear_formatoInvalido_lanzaDatoInvalidoException() {
        // Arrange
        Grupo grupo = grupo();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> Horario.crear(grupo, DiaSemana.LUNES, "8:00", "10:00"));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void actualizar_rangoValido_actualizaHorario() {
        // Arrange
        Horario horario = horario();

        // Act
        horario.actualizar(grupo(), DiaSemana.MARTES, "10:00", "12:00");

        // Assert
        assertThat(horario.getDia()).isEqualTo(DiaSemana.MARTES);
        assertThat(horario.getHoraFin()).isEqualTo("12:00");
    }
    @Test
    void actualizar_horaInicioMayorAHoraFin_lanzaDatoInvalidoException() {
        // Arrange
        Horario horario = horario();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> horario.actualizar(grupo(), DiaSemana.LUNES, "10:00", "08:00"));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
}
