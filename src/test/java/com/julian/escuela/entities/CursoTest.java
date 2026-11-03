package com.julian.escuela.entities;

import com.julian.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CursoTest {

    @Test
    void crear_debeCrearCurso_cuandoLaDescripcionEsNulaYLosDemasDatosSonValidos() {
        // Arrange
        String nombre = " Matematicas I ";
        String descripcion = null;
        Integer creditos = 6;

        // Act
        Curso curso = Curso.crear(nombre, descripcion, creditos);

        // Assert
        assertThat(curso.getIdCurso()).isNull();
        assertThat(curso.getNombre()).isEqualTo("Matematicas I");
        assertThat(curso.getDescripcion()).isNull();
        assertThat(curso.getCreditos()).isEqualTo(6);
        assertThat(curso.getGrupos()).isEmpty();
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoLosCreditosSonCero() {
        // Arrange
        Integer creditos = 0;

        // Act + Assert
        assertThatThrownBy(() -> Curso.crear("Matematicas I", null, creditos))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessage("Los creditos son requeridos y deben ser mayores que 0");
    }
}
