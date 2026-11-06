package com.julian.escuela.support;

import java.util.Objects;

// Dobles de las funciones Oracle: solo se registran como aliases en H2.
public final class FuncionesOracleTest {
    private FuncionesOracleTest() {}

    public static String generarMatricula(String nombre, String paterno, String materno) {
        return String.format("%010d", Integer.toUnsignedLong(Objects.hash(nombre, paterno, materno)));
    }

    public static String generarEmail(String nombre, String paterno, String materno) {
        return "alumno." + generarMatricula(nombre, paterno, materno) + "@escuela.com";
    }
}
