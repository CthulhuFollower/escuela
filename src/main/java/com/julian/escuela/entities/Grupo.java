package com.julian.escuela.entities;

import com.julian.escuela.exceptions.DatoInvalidoException;
import com.julian.escuela.utils.StringCustomUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GRUPOS", uniqueConstraints = @UniqueConstraint(
        name = "GRUPO_CU_MA_AU_PE_UK",
        columnNames = {"ID_CURSO", "ID_MAESTRO", "ID_AULA", "PERIODO"}
))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GRUPO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CURSO", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_MAESTRO", nullable = false)
    private Maestro maestro;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_AULA", nullable = false)
    private Aula aula;

    @Column(name = "PERIODO", nullable = false, length = 20)
    private String periodo;

    @OneToMany(mappedBy = "grupo", fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    @OneToMany(mappedBy = "grupo", fetch = FetchType.LAZY)
    private List<Horario> horarios = new ArrayList<>();

    private static void validarDatos(Curso curso, Maestro maestro, Aula aula, String periodo) {
        if (curso == null) {
            throw new DatoInvalidoException("El curso es requerido");
        }
        if (maestro == null) {
            throw new DatoInvalidoException("El maestro es requerido");
        }
        if (aula == null) {
            throw new DatoInvalidoException("El aula es requerida");
        }
        StringCustomUtils.validarTamanio(periodo, 1, 20,
                "El periodo es requerido y debe tener como maximo 20 caracteres");
    }

    public void actualizar(Curso curso, Maestro maestro, Aula aula, String periodo) {
        validarDatos(curso, maestro, aula, periodo);

        this.curso = curso;
        this.maestro = maestro;
        this.aula = aula;
        this.periodo = periodo.trim();
    }

    public static Grupo crear(Curso curso, Maestro maestro, Aula aula, String periodo) {

        validarDatos(curso, maestro, aula, periodo);

        Grupo grupo = new Grupo();
        grupo.curso = curso;
        grupo.maestro = maestro;
        grupo.aula = aula;
        grupo.periodo = periodo.trim();
        return grupo;
    }
}
