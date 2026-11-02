package com.julian.escuela.repositories;

public interface MaestroRepository {

    boolean existByEmailIgnoreCase(String email);

    boolean existByTelefono(String telefono);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByTelefonoAndIdNot(String telefono, Long id);

}
