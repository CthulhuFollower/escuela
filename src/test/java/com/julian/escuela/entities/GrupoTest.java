package com.julian.escuela.entities;

import com.julian.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GrupoTest {

    @Test
    void crear_debeCrearGrupo_cuandoLosDatosSonValidos() {
        // Arrange
        Curso curso = Curso.crear("Matematicas I", null, 6);
        Maestro maestro = Maestro.crear("Laura", "Martinez", "Lopez", "laura@escuela.com", "5551010789");
        Aula aula = Aula.crear("Aula 101", 30);
        String periodo = " 2026-2 ";

        // Act
        Grupo grupo = Grupo.crear(curso, maestro, aula, periodo);

        // Assert
        assertThat(grupo.getIdGrupo()).isNull();
        assertThat(grupo.getCurso()).isSameAs(curso);
        assertThat(grupo.getMaestro()).isSameAs(maestro);
        assertThat(grupo.getAula()).isSameAs(aula);
        assertThat(grupo.getPeriodo()).isEqualTo("2026-2");
        assertThat(grupo.getInscripciones()).isEmpty();
        assertThat(grupo.getHorarios()).isEmpty();
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoElCursoEsNulo() {
        // Arrange
        Curso curso = null;
        Maestro maestro = Maestro.crear("Laura", "Martinez", "Lopez", "laura@escuela.com", "5551010789");
        Aula aula = Aula.crear("Aula 101", 30);

        // Act + Assert
        assertThatThrownBy(() -> Grupo.crear(curso, maestro, aula, "2026-2"))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessage("El curso es requerido");
    }
}
