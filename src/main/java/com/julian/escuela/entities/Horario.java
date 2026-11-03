package com.julian.escuela.entities;

import com.julian.escuela.enums.DiaSemana;
import com.julian.escuela.exceptions.DatoInvalidoException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "HORARIOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HORARIO")
    private Long idHorario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    @Enumerated(EnumType.STRING)
    @Column(name = "DIA", nullable = false, length = 15)
    private DiaSemana dia;

    @Column(name = "HORA_INICIO", nullable = false, length = 5)
    private String horaInicio;

    @Column(name = "HORA_FIN", nullable = false, length = 5)
    private String horaFin;

    private static void validarDatos(Grupo grupo, DiaSemana dia, String horaInicio, String horaFin) {
        if (grupo == null) {
            throw new DatoInvalidoException("El grupo es requerido");
        }
        if (dia == null) {
            throw new DatoInvalidoException("El dia es requerido");
        }
        validarHora(horaInicio, "La hora de inicio debe tener el formato HH:mm y estar entre 00:00 y 23:59");
        validarHora(horaFin, "La hora de fin debe tener el formato HH:mm y estar entre 00:00 y 23:59");
    }

    public static Horario crear(Grupo grupo, DiaSemana dia, String horaInicio, String horaFin) {
        validarDatos(grupo, dia, horaInicio, horaFin);

        Horario horario = new Horario();
        horario.grupo = grupo;
        horario.dia = dia;
        horario.horaInicio = horaInicio;
        horario.horaFin = horaFin;
        return horario;
    }

    private static void validarHora(String hora, String mensaje) {
        if (hora == null || !hora.matches("([01][0-9]|2[0-3]):[0-5][0-9]")) {
            throw new DatoInvalidoException(mensaje);
        }
    }
}
