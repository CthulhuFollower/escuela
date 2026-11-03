package com.julian.escuela.entities;

import com.julian.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AulaTest {

    @Test
    void crear_debeCrearAula_cuandoLosDatosSonValidos() {
        // Arrange
        String nombre = " Aula 101 ";
        Integer capacidad = 30;

        // Act
        Aula aula = Aula.crear(nombre, capacidad);

        // Assert
        assertThat(aula.getIdAula()).isNull();
        assertThat(aula.getNombre()).isEqualTo("Aula 101");
        assertThat(aula.getCapacidad()).isEqualTo(30);
        assertThat(aula.getGrupos()).isEmpty();
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoLaCapacidadEsCero() {
        // Arrange
        Integer capacidad = 0;

        // Act + Assert
        assertThatThrownBy(() -> Aula.crear("Aula 101", capacidad))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessage("La capacidad es requerida y debe ser mayor que 0");
    }
}
