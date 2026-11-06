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

class AlumnoTest {
    @Test
    void asignarDatosAcademicos_matriculaNula_lanzaDatoInvalidoException() {
        // Arrange
        Alumno alumno = alumno();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class,
                () -> alumno.asignarDatosAcademicos("juan@escuela.com", null));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }

    @Test
    void asignarDatosAcademicos_matriculaVacia_lanzaDatoInvalidoException() {
        // Arrange
        Alumno alumno = alumno();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class,
                () -> alumno.asignarDatosAcademicos("juan@escuela.com", " "));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void crear_datosValidos_retornaAlumnoConDatosSinEspacios() {
        // Arrange
        String nombre = " Juan ";

        // Act
        Alumno alumno = Alumno.crear(nombre, " Perez ", " Lopez ");

        // Assert
        assertThat(alumno.getNombre()).isEqualTo("Juan");
        assertThat(alumno.getApellidoPaterno()).isEqualTo("Perez");
        assertThat(alumno.getApellidoMaterno()).isEqualTo("Lopez");
        assertThat(alumno.getFechaIngreso()).isEqualTo(LocalDate.now());
        assertThat(alumno.getInscripciones()).isEmpty();
    }
    @Test
    void crear_nombreVacio_lanzaDatoInvalidoException() {
        // Arrange
        String nombre = " ";

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> Alumno.crear(nombre, "Perez", "Lopez"));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void cambioEnDatos_nombreDistinto_retornaTrue() {
        // Arrange
        Alumno alumno = alumno();

        // Act
        boolean cambio = alumno.cambioEnDatos("Pedro", "Perez", "Lopez");

        // Assert
        assertThat(cambio).isTrue();
    }
    @Test
    void cambioEnDatos_mismosDatos_retornaFalse() {
        // Arrange
        Alumno alumno = alumno();

        // Act
        boolean cambio = alumno.cambioEnDatos("Juan", "Perez", "Lopez");

        // Assert
        assertThat(cambio).isFalse();
    }
    @Test
    void cambioEnDatos_apellidoPaternoVacio_lanzaDatoInvalidoException() {
        // Arrange
        Alumno alumno = alumno();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> alumno.cambioEnDatos("Juan", " ", "Lopez"));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void asignarDatosAcademicos_datosValidos_asignaEmailEnMinusculasYMatriculaSinEspacios() {
        // Arrange
        Alumno alumno = alumno();

        // Act
        alumno.asignarDatosAcademicos(" JUAN@ESCUELA.COM ", " PELOJU2601 ");

        // Assert
        assertThat(alumno.getEmail()).isEqualTo("juan@escuela.com");
        assertThat(alumno.getMatricula()).isEqualTo("PELOJU2601");
    }
    @Test
    void asignarDatosAcademicos_matriculaDeLongitudDistintaA10_lanzaDatoInvalidoException() {
        // Arrange
        Alumno alumno = alumno();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> alumno.asignarDatosAcademicos("juan@escuela.com", "ABC"));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void actualizar_datosValidos_actualizaDatosPersonalesYAcademicos() {
        // Arrange
        Alumno alumno = alumno();

        // Act
        alumno.actualizar(" Pedro ", " Garcia ", " Ramos ", " PEDRO@ESCUELA.COM ", "GARAPE2602");

        // Assert
        assertThat(alumno.getNombre()).isEqualTo("Pedro");
        assertThat(alumno.getApellidoPaterno()).isEqualTo("Garcia");
        assertThat(alumno.getApellidoMaterno()).isEqualTo("Ramos");
        assertThat(alumno.getEmail()).isEqualTo("pedro@escuela.com");
        assertThat(alumno.getMatricula()).isEqualTo("GARAPE2602");
    }
    @Test
    void actualizar_emailVacio_lanzaDatoInvalidoException() {
        // Arrange
        Alumno alumno = alumno();

        // Act
        DatoInvalidoException error = assertThrows(DatoInvalidoException.class, () -> alumno.actualizar("Pedro", "Garcia", "Ramos", " ", "GARAPE2602"));

        // Assert
        assertThat(error.getMessage()).isNotBlank();
    }
    @Test
    void calcularPromedio_conNulosYValores_ignoraNulos() {
        // Arrange
        Alumno alumno = alumno();
        Inscripcion primera = Inscripcion.crear(alumno, grupo());
        primera.setCalificacion(Calificacion.crear(primera, new BigDecimal("8")));
        Inscripcion segunda = Inscripcion.crear(alumno, grupo());
        segunda.setCalificacion(Calificacion.crear(segunda, new BigDecimal("10")));
        Inscripcion sinNota = Inscripcion.crear(alumno, grupo());
        alumno.setInscripciones(List.of(primera, segunda, sinNota));

        // Act
        BigDecimal promedio = alumno.calcularPromedio();

        // Assert
        assertThat(promedio).isEqualByComparingTo("9.00");
    }
    @Test
    void calcularPromedio_resultadoConDecimales_redondeaADosDecimalesHalfUp() {
        // Arrange
        Alumno alumno = alumno();
        Inscripcion primera = Inscripcion.crear(alumno, grupo());
        primera.setCalificacion(Calificacion.crear(primera, new BigDecimal("8.33")));
        Inscripcion segunda = Inscripcion.crear(alumno, grupo());
        segunda.setCalificacion(Calificacion.crear(segunda, new BigDecimal("8.34")));
        alumno.setInscripciones(List.of(primera, segunda));

        // Act
        BigDecimal promedio = alumno.calcularPromedio();

        // Assert
        assertThat(promedio).isEqualByComparingTo("8.34");
        assertThat(promedio.scale()).isEqualTo(2);
    }
    @Test
    void calcularPromedio_sinCalificaciones_retornaCero() {
        // Arrange
        Alumno alumno = alumno();

        // Act
        BigDecimal promedio = alumno.calcularPromedio();

        // Assert
        assertThat(promedio).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(promedio.scale()).isEqualTo(2);
    }
    @Test
    void obtenerNombreCompleto_datosValidos_retornaNombreYApellidos() {
        // Arrange
        Alumno alumno = alumno();

        // Act
        String nombre = alumno.obtenerNombreCompleto();

        // Assert
        assertThat(nombre).isEqualTo("Juan Perez Lopez");
    }
}
