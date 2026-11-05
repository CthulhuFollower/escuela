package com.julian.escuela.entities;

import com.julian.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlumnoTest {

    @Test
    void crear_debeCrearAlumno_cuandoLosDatosSonValidos() {
        // Arrange
        String nombre = " Juan ";
        String apellidoPaterno = " Perez ";
        String apellidoMaterno = " Lopez ";

        // Act
        Alumno alumno = Alumno.crear(nombre, apellidoPaterno, apellidoMaterno;

        // Assert
        assertThat(alumno.getId()).isNull();
        assertThat(alumno.getNombre()).isEqualTo("Juan");
        assertThat(alumno.getApellidoPaterno()).isEqualTo("Perez");
        assertThat(alumno.getApellidoMaterno()).isEqualTo("Lopez");
        assertThat(alumno.getFechaIngreso()).isNotNull();
        assertThat(alumno.getInscripciones()).isEmpty();
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoLaMatriculaEstaVacia() {
        // Arrange
        String matricula = " ";

        // Act + Assert
        assertThatThrownBy(() -> Alumno.crear("Juan", "Perez", "Lopez", "juan@alumnos.com", matricula))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessage("La matricula es requerida y debe tener como maximo 10 caracteres");
    }
}
