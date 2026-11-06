package com.julian.escuela.controllers;

import com.julian.escuela.support.ControllerIntegrationSupport;
import com.julian.escuela.entities.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.ResultActions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AlumnoControllerIT extends ControllerIntegrationSupport {
    private static final String RUTA = "/api/alumnos";

    @Test
    void registrar_datosValidos_generaMatriculaYEmail() throws Exception {
        // Arrange
        String json = requestValido();

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().isCreated()).andExpect(jsonPath("$.matricula").value(matchesPattern("[0-9]{10}")));
    }
    @Test
    void obtenerPorId_alumnoExistente_retornaAlumnoConPromedio() throws Exception {
        // Arrange
        Long id = prepararEntidad();

        // Act
        ResultActions resultado = mvc.perform(get(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id)).andExpect(jsonPath("$.promedio").value(0));
    }
    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        // Arrange
        long id = 999999L;

        // Act
        ResultActions resultado = mvc.perform(get(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void listar_conAlumnos_retornaLista() throws Exception {
        // Arrange
        prepararEntidad();

        // Act
        ResultActions resultado = mvc.perform(get(RUTA));

        // Assert
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$").isNotEmpty());
    }
    @Test
    void listar_errorDeRepositorio_propagaExcepcion() throws Exception {
        // Arrange
        doThrow(new IllegalStateException("fallo simulado")).when(alumnoService).listar();

        // Act
        ResultActions resultado = mvc.perform(get(RUTA));

        // Assert
        resultado.andExpect(status().is(500))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.detail").value("Error interno del servidor. Contacte al administrador."));
    }
    @Test
    void actualizar_cambiaNombre_regeneraMatriculaYEmail() throws Exception {
        // Arrange
        Long id = prepararEntidad();

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(requestValido()));

        // Assert
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id));
    }
    @Test
    void actualizar_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        // Arrange
        long id = 999999L;

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(requestValido()));

        // Assert
        resultado.andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void obtenerPorId_idCero_retorna400() throws Exception {
        // Arrange
        long id = 0L;

        // Act
        ResultActions resultado = mvc.perform(get(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void obtenerPorId_idNegativo_retorna400() throws Exception {
        // Arrange
        long id = -1L;

        // Act
        ResultActions resultado = mvc.perform(get(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void actualizar_idNegativo_retorna400() throws Exception {
        // Arrange
        long id = -1L;

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(requestValido()));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void eliminar_idCero_retorna400() throws Exception {
        // Arrange
        long id = 0L;

        // Act
        ResultActions resultado = mvc.perform(delete(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void registrar_bodyVacio_retorna400() throws Exception {
        // Arrange
        String json = "";

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void registrar_camposObligatoriosVacios_retorna400() throws Exception {
        // Arrange
        String json = "{}";

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void eliminar_sinInscripciones_eliminaAlumno() throws Exception {
        // Arrange
        Long id = alumno.getId();

        // Act
        ResultActions resultado = mvc.perform(delete(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().isNoContent());
        assertThat(alumnoRepository.existsById(id)).isFalse();
    }
    @Test
    void eliminar_conInscripciones_lanzaEntidadRelacionadaException() throws Exception {
        // Arrange
        inscribir();
        Long id = alumno.getId();

        // Act
        ResultActions resultado = mvc.perform(delete(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().is(409))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void registrar_nombreVacio_lanzaDatoInvalidoException() throws Exception {
        // Arrange
        String json = "{\"nombre\":\" \",\"apellidoPaterno\":\"Perez\",\"apellidoMaterno\":\"Lopez\"}";

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void obtenerPorId_sinCalificaciones_retornaPromedioCero() throws Exception {
        // Arrange
        Long id = alumno.getId();

        // Act
        ResultActions resultado = mvc.perform(get(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.promedio").value(0));
    }
    @Test
    void obtenerPorId_conCalificacionesNulas_ignoraNulosEnPromedio() throws Exception {
        // Arrange
        Inscripcion primera = inscribir();
        calificar(primera);
        inscripcionRepository.saveAndFlush(Inscripcion.crear(alumno, grupoOtroPeriodo()));
        entityManager.clear();
        Long id = alumno.getId();

        // Act
        ResultActions resultado = mvc.perform(get(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.promedio").value(8.5));
    }
    @Test
    void actualizar_cambiaSoloApellidoMaterno_regeneraMatriculaYEmail() throws Exception {
        // Arrange
        String matriculaOriginal = alumno.getMatricula();
        String emailOriginal = alumno.getEmail();
        Long id = alumno.getId();
        String json = "{\"nombre\":\"Juan\",\"apellidoPaterno\":\"Perez\",\"apellidoMaterno\":\"Ramos\"}";

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().isOk())
                .andExpect(jsonPath("$.matricula").value(not(matriculaOriginal)))
                .andExpect(jsonPath("$.email").value(not(emailOriginal)));
    }
    @Test
    void actualizar_sinCambiosEnDatos_noRegeneraNiGuarda() throws Exception {
        // Arrange
        Long id = alumno.getId();
        String matriculaOriginal = alumno.getMatricula();
        String json = "{\"nombre\":\"Juan\",\"apellidoPaterno\":\"Perez\",\"apellidoMaterno\":\"Lopez\"}";

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.matricula").value(matriculaOriginal));
    }

    private String requestValido() {
        return "{\"nombre\":\"Maria\",\"apellidoPaterno\":\"Gomez\",\"apellidoMaterno\":\"Ramos\"}";
    }

    private Long prepararEntidad() {
        return alumno.getId();
    }
}
