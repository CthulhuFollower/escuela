package com.julian.escuela.entities;

import com.julian.escuela.enums.DiaSemana;
import com.julian.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HorarioTest {

    @Test
    void crear_debeCrearHorario_cuandoLosDatosSonValidos() {
        // Arrange
        Grupo grupo = crearGrupo();
        DiaSemana dia = DiaSemana.LUNES;
        String horaInicio = "08:00";
        String horaFin = "09:00";

        // Act
        Horario horario = Horario.crear(grupo, dia, horaInicio, horaFin);

        // Assert
        assertThat(horario.getIdHorario()).isNull();
        assertThat(horario.getGrupo()).isSameAs(grupo);
        assertThat(horario.getDia()).isEqualTo(DiaSemana.LUNES);
        assertThat(horario.getHoraInicio()).isEqualTo("08:00");
        assertThat(horario.getHoraFin()).isEqualTo("09:00");
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoLaHoraDeInicioEsInvalida() {
        // Arrange
        Grupo grupo = crearGrupo();
        String horaInicio = "25:00";

        // Act + Assert
        assertThatThrownBy(() -> Horario.crear(grupo, DiaSemana.LUNES, horaInicio, "09:00"))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessage("La hora de inicio debe tener el formato HH:mm y estar entre 00:00 y 23:59");
    }

    private Grupo crearGrupo() {
        Curso curso = Curso.crear("Matematicas I", null, 6);
        Maestro maestro = Maestro.crear("Laura", "Martinez", "Lopez", "laura@escuela.com", "5551010789");
        Aula aula = Aula.crear("Aula 101", 30);
        return Grupo.crear(curso, maestro, aula, "2026-2");
    }
}
