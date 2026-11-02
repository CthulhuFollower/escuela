package com.julian.escuela.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
}
