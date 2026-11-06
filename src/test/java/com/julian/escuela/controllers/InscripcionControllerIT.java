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

class InscripcionControllerIT extends ControllerIntegrationSupport {
    @Test
    void actualizar_mismaCombinacionPropia_noLanzaConflicto() throws Exception {
        // Arrange
        Long id = prepararEntidad();
        String json = requestValido();

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id)
                .contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id));
    }
    private static final String RUTA = "/api/inscripciones";

    @Test
    void registrar_datosValidos_registraInscripcion() throws Exception {
        // Arrange
        String json = requestValido();

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().isCreated()).andExpect(jsonPath("$.calificacion").value(nullValue()));
    }
    @Test
    void obtenerPorId_inscripcionExistente_retornaInscripcion() throws Exception {
        // Arrange
        Long id = prepararEntidad();

        // Act
        ResultActions resultado = mvc.perform(get(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id)).andExpect(jsonPath("$.calificacion").value(nullValue()));
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
    void listar_conInscripciones_retornaLista() throws Exception {
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
        doThrow(new IllegalStateException("fallo simulado")).when(inscripcionService).listar();

        // Act
        ResultActions resultado = mvc.perform(get(RUTA));

        // Assert
        resultado.andExpect(status().is(500))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.detail").value("Error interno del servidor. Contacte al administrador."));
    }
    @Test
    void actualizar_otroGrupoValido_actualizaInscripcion() throws Exception {
        // Arrange
        Long id = prepararEntidad();
        Grupo nuevoGrupo = grupoOtroPeriodo();
        String json = "{\"idAlumno\":%d,\"idGrupo\":%d}".formatted(alumno.getId(), nuevoGrupo.getId());

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.grupo.periodo").value("2026-02"));
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
    void eliminar_sinCalificacion_eliminaInscripcion() throws Exception {
        // Arrange
        Long id = prepararEntidad();

        // Act
        ResultActions resultado = mvc.perform(delete(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().isNoContent());
        assertThat(inscripcionRepository.existsById(id)).isFalse();
    }
    @Test
    void eliminar_conCalificacion_lanzaEntidadRelacionadaException() throws Exception {
        // Arrange
        Inscripcion registro = inscribir();
        calificar(registro);
        Long id = registro.getIdInscripcion();

        // Act
        ResultActions resultado = mvc.perform(delete(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().is(409))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void registrar_idNulo_retorna400() throws Exception {
        // Arrange
        String json = "{\"idAlumno\":null,\"idGrupo\":1}";

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void registrar_idNoPositivo_retorna400() throws Exception {
        // Arrange
        String json = "{\"idAlumno\":0,\"idGrupo\":1}";

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void registrar_alumnoYGrupoDuplicados_lanzaConflictoException() throws Exception {
        // Arrange
        prepararEntidad();
        String json = "{\"idAlumno\":%d,\"idGrupo\":%d}".formatted(alumno.getId(), grupo.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(409))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void actualizar_combinacionYaExistente_lanzaConflictoException() throws Exception {
        // Arrange
        inscribir();
        Long id = inscripcionRepository.saveAndFlush(Inscripcion.crear(alumno, grupoOtroPeriodo())).getIdInscripcion();
        String json = "{\"idAlumno\":%d,\"idGrupo\":%d}".formatted(alumno.getId(), grupo.getId());

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(409))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void registrar_alumnoInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        // Arrange
        String json = "{\"idAlumno\":%d,\"idGrupo\":%d}".formatted(999999L, grupo.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }

    private String requestValido() {
        return "{\"idAlumno\":%d,\"idGrupo\":%d}".formatted(alumno.getId(), grupo.getId());
    }

    private Long prepararEntidad() {
        return inscribir().getIdInscripcion();
    }
}
