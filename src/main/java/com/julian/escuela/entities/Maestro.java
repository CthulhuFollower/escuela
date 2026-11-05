package com.julian.escuela.entities;

import com.julian.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MAESTROS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Maestro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MAESTRO")
    private Long idMaestro;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "TELEFONO", nullable = false, unique = true, length = 10)
    private String telefono;

    @Builder.Default
    @OneToMany(mappedBy = "maestro", fetch = FetchType.LAZY)
    private List<Grupo> grupos = new ArrayList<>();

    public String obtenerNombreCompleto(){
        return String.join(" ",
                this.getNombre(),
                this.getApellidoPaterno(),
                this.getApellidoMaterno());
    }

    private static void validarDatos(String nombre, String apellidoPaterno,String apellidoMaterno, String email, String telefono){

        StringCustomUtils.validarTamanio(nombre, 1, 50, "El nombre es requerdio y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(apellidoPaterno, 1, 50, "El apellido paterno es requerdio y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(apellidoMaterno, 1, 50, "El apellido materno es requerdio y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(email, 8, 100, "El email es requerdio y debe tener entre 1 y 100 caracteres");

        StringCustomUtils.validarTamanio(telefono, 10, 10, "El telefono es requerdio y debe tener entre 1 y 10 caracteres");

    }

    public void actualizar(String nombre, String apellidoPaterno, String apellidoMaterno, String email, String telefono){
        validarDatos(nombre, apellidoPaterno, apellidoMaterno, email, telefono);

        this.nombre = nombre.trim();
        this.apellidoPaterno = apellidoPaterno.trim();
        this.apellidoMaterno = apellidoMaterno.trim();
        this.email = email.trim();
        this.telefono = telefono.trim();

    }

    public static Maestro crear(String nombre, String apellidoPaterno, String apellidoMaterno, String email, String telefono){

        validarDatos(nombre, apellidoPaterno, apellidoMaterno, email, telefono);

        return Maestro.builder()
                .nombre(nombre.trim())
                .apellidoMaterno(apellidoMaterno.trim())
                .apellidoPaterno(apellidoPaterno.trim())
                .email(email.trim().toLowerCase())
                .telefono(telefono.trim())
                .build();
    }
}
