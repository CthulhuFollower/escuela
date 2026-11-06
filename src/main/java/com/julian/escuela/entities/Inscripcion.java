package com.julian.escuela.entities;

import com.julian.escuela.exceptions.DatoInvalidoException;
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

    private static void validarDatos(Alumno alumno, Grupo grupo) {
        if (alumno == null) {
            throw new DatoInvalidoException("El alumno es requerido");
        }
        if (grupo == null) {
            throw new DatoInvalidoException("El grupo es requerido");
        }
    }

    public void actualizar(Alumno alumno, Grupo grupo) {
        validarDatos(alumno, grupo);

        this.alumno = alumno;
        this.grupo = grupo;
    }

    public static Inscripcion crear(Alumno alumno, Grupo grupo) {
        validarDatos(alumno, grupo);

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.alumno = alumno;
        inscripcion.grupo = grupo;
        inscripcion.fechaInscripcion = LocalDateTime.now();
        return inscripcion;
    }
}
