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

class AulaTest {
    @Test
    void crear_datosValidos_retornaAula() {
        // Arrange
        String nombre = " Aula 101 ";

        // Act
        Aula aula = Aula.crear(nombre, 30);

        // Assert
        assertThat(aula.getNombre()).isEqualTo("Aula 101");
        assertThat(aula.getCapacidad()).isEqualTo(30);
        assertThat(aula.getGrupos()).isEmpty();
    }
    @Test
    void crear_capacidadNoPositiva_lanzaDatoInvalidoException() {
        // Arrange
        int capacidad = 0;

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> Aula.crear("Aula 101", capacidad));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void actualizar_datosValidos_actualizaCampos() {
        // Arrange
        Aula aula = aula();

        // Act
        aula.actualizar(" Aula 202 ", 40);

        // Assert
        assertThat(aula.getNombre()).isEqualTo("Aula 202");
        assertThat(aula.getCapacidad()).isEqualTo(40);
    }
    @Test
    void actualizar_nombreVacio_lanzaDatoInvalidoException() {
        // Arrange
        Aula aula = aula();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> aula.actualizar(" ", 40));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void crear_capacidadMayorAlMaximo_lanzaDatoInvalidoException() {
        // Arrange
        int capacidad = 10000;

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> Aula.crear("Aula 101", capacidad));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
}
