package com.julian.escuela.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "INSCRIPCIONES", uniqueConstraints = @UniqueConstraint(
        name = "INSCRIPCION_ALU_GRU_UK",
        columnNames = {"ID_ALUMNO", "ID_GRUPO"}
))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INSCRIPCION")
    private Long idInscripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ALUMNO", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    @Column(name = "FECHA_INSCRIPCION", columnDefinition = "DATE")
    private LocalDateTime fechaInscripcion;

    @OneToOne(mappedBy = "inscripcion", fetch = FetchType.LAZY)
    private Calificacion calificacion;
}
