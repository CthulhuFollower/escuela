package com.julian.escuela.entities;

import com.julian.escuela.exceptions.DatoInvalidoException;
import com.julian.escuela.utils.ValoresNumericosUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "CALIFICACIONES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CALIFICACION")
    private Long idCalificacion;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_INSCRIPCION", nullable = false, unique = true)
    private Inscripcion inscripcion;

    @Column(name = "CALIFICACION", nullable = false, precision = 3, scale = 1)
    private BigDecimal calificacion;

    @Column(name = "FECHA_REGISTRO", columnDefinition = "DATE")
    private LocalDateTime fechaRegistro;

    private static void validarDatos(Inscripcion inscripcion, BigDecimal calificacion) {
        if (inscripcion == null) {
            throw new DatoInvalidoException("La inscripcion es requerida");
        }
        ValoresNumericosUtils.validarNumeroRequerido(calificacion, "La calificacion es requerida");
        if (calificacion.compareTo(BigDecimal.ZERO) < 0 || calificacion.compareTo(BigDecimal.TEN) > 0) {
            throw new DatoInvalidoException("La calificacion debe estar entre 0 y 10");
        }
    }

    public static Calificacion crear(Inscripcion inscripcion, BigDecimal calificacion) {
        validarDatos(inscripcion, calificacion);

        Calificacion registro = new Calificacion();
        registro.inscripcion = inscripcion;
        registro.calificacion = calificacion;
        registro.fechaRegistro = LocalDateTime.now();
        return registro;
    }
}
