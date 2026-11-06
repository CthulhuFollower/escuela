package com.julian.escuela.support;

import com.julian.escuela.entities.*;
import com.julian.escuela.enums.DiaSemana;
import java.math.BigDecimal;

public final class TestData {
    private TestData() {}

    public static Alumno alumno() {
        Alumno alumno = Alumno.crear("Juan", "Perez", "Lopez");
        alumno.asignarDatosAcademicos("juan@escuela.com", "PELOJU2601");
        alumno.setId(1L);
        return alumno;
    }

    public static Maestro maestro() {
        Maestro maestro = Maestro.crear("Laura", "Martinez", "Lopez", "laura@escuela.com", "5512345678");
        maestro.setIdMaestro(1L);
        return maestro;
    }

    public static Curso curso() {
        Curso curso = Curso.crear("Matematicas I", "Fundamentos", 6);
        curso.setIdCurso(1L);
        return curso;
    }

    public static Aula aula() {
        Aula aula = Aula.crear("Aula 101", 30);
        aula.setId(1L);
        return aula;
    }

    public static Grupo grupo() {
        Grupo grupo = Grupo.crear(curso(), maestro(), aula(), "2026-01");
        grupo.setId(1L);
        return grupo;
    }

    public static Horario horario() {
        Horario horario = Horario.crear(grupo(), DiaSemana.LUNES, "08:00", "10:00");
        horario.setId(1L);
        return horario;
    }

    public static Inscripcion inscripcion() {
        Inscripcion inscripcion = Inscripcion.crear(alumno(), grupo());
        inscripcion.setIdInscripcion(1L);
        return inscripcion;
    }

    public static Calificacion calificacion() {
        Calificacion calificacion = Calificacion.crear(inscripcion(), new BigDecimal("8.5"));
        calificacion.setIdCalificacion(1L);
        return calificacion;
    }
}
