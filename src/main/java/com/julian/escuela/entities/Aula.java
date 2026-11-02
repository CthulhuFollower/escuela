package com.julian.escuela.entities;

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
    private Long idAula;

    @Column(name = "NOMBRE", nullable = false, unique = true, length = 30)
    private String nombre;

    @Column(name = "CAPACIDAD", nullable = false, precision = 4)
    private Integer capacidad;

    @OneToMany(mappedBy = "aula", fetch = FetchType.LAZY)
    private List<Grupo> grupos = new ArrayList<>();
}
