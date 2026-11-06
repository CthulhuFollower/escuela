package com.julian.escuela.entities;

import com.julian.escuela.exceptions.DatoInvalidoException;
import com.julian.escuela.utils.StringCustomUtils;
import com.julian.escuela.utils.ValoresNumericosUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "AULAS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AULA")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, unique = true, length = 30)
    private String nombre;

    @Column(name = "CAPACIDAD", nullable = false, precision = 4)
    private Integer capacidad;

    @OneToMany(mappedBy = "aula", fetch = FetchType.LAZY)

    private List<Grupo> grupos = new ArrayList<>();

    private static void validarDatos(String nombre, Integer capacidad) {
        StringCustomUtils.validarTamanio(nombre, 1, 30,
                "El nombre del aula es requerido y debe tener como maximo 30 caracteres");
        ValoresNumericosUtils.validarEnteroPositivo(capacidad,
                "La capacidad es requerida y debe ser mayor que 0");
        if (capacidad > 9999) {
            throw new DatoInvalidoException("La capacidad no puede exceder 9999");
        }
    }

    public void actualizar(String nombre, Integer capacidad) {
        validarDatos(nombre, capacidad);

        this.nombre = nombre.trim();
        this.capacidad = capacidad;
    }

    public static Aula crear(String nombre, Integer capacidad) {
        validarDatos(nombre, capacidad);

        Aula aula = new Aula();
        aula.nombre = nombre.trim();
        aula.capacidad = capacidad;
        return aula;
    }
}
