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
@Table(name = "CURSOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CURSO")
    private Long idCurso;

    @Column(name = "NOMBRE", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;

    @Column(name = "CREDITOS", nullable = false, precision = 2)
    private Integer creditos;

    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    private List<Grupo> grupos = new ArrayList<>();

    private static void validarDatos(String nombre, String descripcion, Integer creditos) {
        StringCustomUtils.validarTamanio(nombre, 1, 100,
                "El nombre del curso es requerido y debe tener como maximo 100 caracteres");
        if (descripcion != null && descripcion.length() > 200) {
            throw new DatoInvalidoException("La descripcion debe tener como maximo 200 caracteres");
        }
        ValoresNumericosUtils.validarEnteroPositivo(creditos,
                "Los creditos son requeridos y deben ser mayores que 0");
        if (creditos > 99) {
            throw new DatoInvalidoException("Los creditos no pueden exceder 99");
        }
    }

    public static Curso crear(String nombre, String descripcion, Integer creditos) {
        validarDatos(nombre, descripcion, creditos);

        Curso curso = new Curso();
        curso.nombre = nombre.trim();
        curso.descripcion = descripcion == null ? null : descripcion.trim();
        curso.creditos = creditos;
        return curso;
    }
}
