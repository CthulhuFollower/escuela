package com.julian.escuela.entities;

import com.julian.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MaestroTest {

    @Test
    void crear_debeCrearMaestro_cuandoLosDatosSonValidos() {
        // Arrange
        String nombre = " Laura ";
        String apellidoPaterno = " Martinez ";
        String apellidoMaterno = " Lopez ";
        String email = " LAURA@ESCUELA.COM ";
        String telefono = "5551010789";

        // Act
        Maestro maestro = Maestro.crear(nombre, apellidoPaterno, apellidoMaterno, email, telefono);

        // Assert
        assertThat(maestro.getIdMaestro()).isNull();
        assertThat(maestro.getNombre()).isEqualTo("Laura");
        assertThat(maestro.getApellidoPaterno()).isEqualTo("Martinez");
        assertThat(maestro.getApellidoMaterno()).isEqualTo("Lopez");
        assertThat(maestro.getEmail()).isEqualTo("laura@escuela.com");
        assertThat(maestro.getTelefono()).isEqualTo("5551010789");
        assertThat(maestro.getGrupos()).isEmpty();
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoElTelefonoEsMuyCorto() {
        // Arrange
        String telefono = "555101078";

        // Act + Assert
        assertThatThrownBy(() -> Maestro.crear("Laura", "Martinez", "Lopez", "laura@escuela.com", telefono))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessageContaining("telefono");
    }
}
