package com.julian.escuela.repositories;

import com.julian.escuela.entities.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    @Query(value = "SELECT GENERAR_EMAIL(:nombre, :apellidoPaterno, :apellidoMaterno) FROM DUAL",
            nativeQuery = true)
    String generarEmail(@Param("nombre") String nombre,
                        @Param("apellidoPaterno") String apellidoPaterno,
                        @Param("apellidoMaterno") String apellidoMaterno);

    @Query(value = "SELECT GENERAR_MATRICULA(:nombre, :apellidoPaterno, :apellidoMaterno) FROM DUAL",
            nativeQuery = true)
    String generarMatricula(@Param("nombre") String nombre,
                            @Param("apellidoPaterno") String apellidoPaterno,
                            @Param("apellidoMaterno") String apellidoMaterno);
}
