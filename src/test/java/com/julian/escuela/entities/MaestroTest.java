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

class MaestroTest {
    @Test
    void crear_telefonoMuyCorto_lanzaDatoInvalidoException() {
        // Arrange
        String telefono = "123";

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class,
                () -> Maestro.crear("Laura", "Martinez", "Lopez", "laura@escuela.com", telefono));

        // Assert
        assertThat(error.getMessage()).contains("telefono");
    }
    @Test
    void crear_datosValidos_retornaMaestroConDatosSinEspacios() {
        // Arrange
        String email = " LAURA@ESCUELA.COM ";

        // Act
        Maestro maestro = Maestro.crear(" Laura ", " Martinez ", " Lopez ", email, "5512345678");

        // Assert
        assertThat(maestro.getNombre()).isEqualTo("Laura");
        assertThat(maestro.getEmail()).isEqualTo("laura@escuela.com");
        assertThat(maestro.getGrupos()).isEmpty();
    }
    @Test
    void crear_emailVacio_lanzaDatoInvalidoException() {
        // Arrange
        String email = " ";

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> Maestro.crear("Laura", "Martinez", "Lopez", email, "5512345678"));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void actualizar_datosValidos_actualizaCampos() {
        // Arrange
        Maestro maestro = maestro();

        // Act
        maestro.actualizar(" Ana ", " Garcia ", " Ramos ", "ana@escuela.com", "5598765432");

        // Assert
        assertThat(maestro.getNombre()).isEqualTo("Ana");
        assertThat(maestro.getEmail()).isEqualTo("ana@escuela.com");
        assertThat(maestro.getTelefono()).isEqualTo("5598765432");
    }
    @Test
    void actualizar_telefonoInvalido_lanzaDatoInvalidoException() {
        // Arrange
        Maestro maestro = maestro();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> maestro.actualizar("Ana", "Garcia", "Ramos", "ana@escuela.com", "123"));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void obtenerNombreCompleto_datosValidos_retornaNombreYApellidos() {
        // Arrange
        Maestro maestro = maestro();

        // Act
        String nombre = maestro.obtenerNombreCompleto();

        // Assert
        assertThat(nombre).isEqualTo("Laura Martinez Lopez");
    }
}
