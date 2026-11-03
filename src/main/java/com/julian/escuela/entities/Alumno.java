package com.julian.escuela.entities;

import com.julian.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "ALUMNOS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alumno {

    // Hacer jalar el proyecto
    // Relaciones JPA
    // Terminar los entities

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALUMNO")
    private Long idAlumno;

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
    private LocalDateTime fechaIngreso;

    @Builder.Default
    @OneToMany(mappedBy = "alumno", fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    private static void validarDatos(String nombre, String apellidoPaterno, String apellidoMaterno,
                                     String email, String matricula) {
        StringCustomUtils.validarTamanio(nombre, 1, 50,
                "El nombre es requerido y debe tener como maximo 50 caracteres");
        StringCustomUtils.validarTamanio(apellidoPaterno, 1, 50,
                "El apellido paterno es requerido y debe tener como maximo 50 caracteres");
        StringCustomUtils.validarTamanio(apellidoMaterno, 1, 50,
                "El apellido materno es requerido y debe tener como maximo 50 caracteres");
        StringCustomUtils.validarTamanio(email, 1, 100,
                "El email es requerido y debe tener como maximo 100 caracteres");
        StringCustomUtils.validarTamanio(matricula, 1, 10,
                "La matricula es requerida y debe tener como maximo 10 caracteres");
    }

    public static Alumno crear(String nombre, String apellidoPaterno, String apellidoMaterno,
                               String email, String matricula) {
        validarDatos(nombre, apellidoPaterno, apellidoMaterno, email, matricula);

        return Alumno.builder()
                .nombre(nombre.trim())
                .apellidoPaterno(apellidoPaterno.trim())
                .apellidoMaterno(apellidoMaterno.trim())
                .email(email.trim().toLowerCase(Locale.ROOT))
                .matricula(matricula.trim())
                .fechaIngreso(LocalDateTime.now())
                .build();
    }
}
