package com.julian.escuela.entities;

import com.julian.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ALUMNOS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALUMNO")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "MATRICULA", nullable = false, unique = true, length = 10)
    private String matricula;

    @Column(name = "FECHA_INGRESO", columnDefinition = "DATE")
    private LocalDate fechaIngreso;

    @Builder.Default
    @OneToMany(mappedBy = "alumno", fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public String obtenerNombreCompleto(){
        return String.join(" ",
                this.getNombre(),
                this.getApellidoPaterno(),
                this.getApellidoMaterno());
    }

    //cambioEnDatos
    public void asignarDatosAcademicos(String email, String matricula){
        matricula = matricula == null ? null : matricula.trim();
        validarDatosAcademicos(email, matricula);

        this.email = email.toLowerCase().trim();
        this.matricula = matricula;
    }

    public void actualizar(String nombre, String apellidoPaterno, String apellidoMaterno,
                           String email, String matricula){

        validarDatos(nombre, apellidoPaterno, apellidoMaterno);
        asignarDatosAcademicos(email, matricula);

        this.nombre = nombre.trim();
        this.apellidoMaterno = apellidoMaterno.trim();
        this.apellidoPaterno = apellidoPaterno.trim();

    }

    public boolean cambioEnDatos(String nombre, String apellidoPaterno, String apellidoMaterno){

        validarDatos(nombre, apellidoPaterno, apellidoMaterno);

        return !this.nombre.equals(nombre) ||
                !this.apellidoPaterno.equals(apellidoPaterno) ||
                !this.apellidoMaterno.equals(apellidoMaterno);
    }

    private static void validarDatos(String nombre, String apellidoPaterno, String apellidoMaterno) {
        StringCustomUtils.validarTamanio(nombre, 1, 50,
                "El nombre es requerido y debe tener como maximo 50 caracteres");
        StringCustomUtils.validarTamanio(apellidoPaterno, 1, 50,
                "El apellido paterno es requerido y debe tener como maximo 50 caracteres");
        StringCustomUtils.validarTamanio(apellidoMaterno, 1, 50,
                "El apellido materno es requerido y debe tener como maximo 50 caracteres");
    }

    private static void validarDatosAcademicos(String email, String matricula) {
        StringCustomUtils.validarTamanio(email, 1, 100,
                "El email es requerido y debe tener 1 y 100 caracteres");
        StringCustomUtils.validarTamanio(matricula, 10, 10,
                "La matricula es requerida y debe tener 1 y 100 caracteres");
    }

    public static Alumno crear(String nombre, String apellidoPaterno, String apellidoMaterno) {

        validarDatos(nombre, apellidoPaterno, apellidoMaterno);

        return Alumno.builder()
                .nombre(nombre.trim())
                .apellidoPaterno(apellidoPaterno.trim())
                .apellidoMaterno(apellidoMaterno.trim())
                .fechaIngreso(LocalDate.now())
                .build();
    }

    public BigDecimal calcularPromedio(){

        List<BigDecimal> calificaciones = inscripciones.stream()
                .map(Inscripcion::getCalificacion)
                .filter(Objects::nonNull)
                .map(Calificacion::getCalificacion)
                .filter(Objects::nonNull).toList();

        if (calificaciones.isEmpty())
            return BigDecimal.ZERO.setScale(2);

        BigDecimal suma = calificaciones.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return suma.divide(
                BigDecimal.valueOf(calificaciones.size()),
                2,
                RoundingMode.HALF_UP
        );
    }
}
