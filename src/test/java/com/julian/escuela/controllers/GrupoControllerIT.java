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

class GrupoControllerIT extends ControllerIntegrationSupport {
    @Test
    void actualizar_mismaCombinacionPropia_noLanzaConflicto() throws Exception {
        // Arrange
        String json = "{\"idCurso\":%d,\"idMaestro\":%d,\"idAula\":%d,\"periodo\":\"2026-01\"}"
                .formatted(curso.getIdCurso(), maestro.getIdMaestro(), aula.getId());

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", grupo.getId())
                .contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.periodo").value("2026-01"));
    }
    private static final String RUTA = "/api/grupos";

    @Test
    void registrar_datosValidos_registraGrupo() throws Exception {
        // Arrange
        String json = requestValido();

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().isCreated()).andExpect(jsonPath("$.periodo").value("2026-02"));
    }
    @Test
    void obtenerPorId_grupoExistente_retornaGrupoConHorarios() throws Exception {
        // Arrange
        Long id = prepararEntidad();

        // Act
        ResultActions resultado = mvc.perform(get(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id)).andExpect(jsonPath("$.horarios[0]").value("Lunes 08:00 - 10:00"));
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
    void listar_conGrupos_retornaLista() throws Exception {
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
        doThrow(new IllegalStateException("fallo simulado")).when(grupoService).listar();

        // Act
        ResultActions resultado = mvc.perform(get(RUTA));

        // Assert
        resultado.andExpect(status().is(500))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.detail").value("Error interno del servidor. Contacte al administrador."));
    }
    @Test
    void actualizar_datosValidos_actualizaGrupo() throws Exception {
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
    void eliminar_sinInscripcionesNiHorarios_eliminaGrupo() throws Exception {
        // Arrange
        Long id = grupo.getId();

        // Act
        ResultActions resultado = mvc.perform(delete(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().isNoContent());
        assertThat(grupoRepository.existsById(id)).isFalse();
    }
    @Test
    void eliminar_conInscripciones_lanzaEntidadRelacionadaException() throws Exception {
        // Arrange
        inscribir();
        Long id = grupo.getId();

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
        String json = "{\"idCurso\":null,\"idMaestro\":1,\"idAula\":1,\"periodo\":\"2026-01\"}";

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
        String json = "{\"idCurso\":0,\"idMaestro\":1,\"idAula\":1,\"periodo\":\"2026-01\"}";

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void registrar_combinacionDuplicada_lanzaConflictoException() throws Exception {
        // Arrange
        String json = "{\"idCurso\":%d,\"idMaestro\":%d,\"idAula\":%d,\"periodo\":\"2026-01\"}".formatted(curso.getIdCurso(), maestro.getIdMaestro(), aula.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(409))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void actualizar_combinacionDeOtroGrupo_lanzaConflictoException() throws Exception {
        // Arrange
        Long id = grupoOtroPeriodo().getId();
        String json = "{\"idCurso\":%d,\"idMaestro\":%d,\"idAula\":%d,\"periodo\":\"2026-01\"}".formatted(curso.getIdCurso(), maestro.getIdMaestro(), aula.getId());

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(409))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void registrar_cursoInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        // Arrange
        String json = "{\"idCurso\":%d,\"idMaestro\":%d,\"idAula\":%d,\"periodo\":\"2026-02\"}".formatted(999999L, maestro.getIdMaestro(), aula.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void registrar_periodoFormatoInvalido_retorna400() throws Exception {
        // Arrange
        String json = "{\"idCurso\":%d,\"idMaestro\":%d,\"idAula\":%d,\"periodo\":\"2026-1\"}".formatted(curso.getIdCurso(), maestro.getIdMaestro(), aula.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void registrar_periodoFormatoInvalidoMesInexistente_retorna400() throws Exception {
        // Arrange
        String json = "{\"idCurso\":%d,\"idMaestro\":%d,\"idAula\":%d,\"periodo\":\"2026-13\"}".formatted(curso.getIdCurso(), maestro.getIdMaestro(), aula.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void registrar_periodoFormatoInvalidoMesCero_retorna400() throws Exception {
        // Arrange
        String json = "{\"idCurso\":%d,\"idMaestro\":%d,\"idAula\":%d,\"periodo\":\"2026-00\"}".formatted(curso.getIdCurso(), maestro.getIdMaestro(), aula.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void registrar_periodoFormatoInvalidoAnioCorto_retorna400() throws Exception {
        // Arrange
        String json = "{\"idCurso\":%d,\"idMaestro\":%d,\"idAula\":%d,\"periodo\":\"26-01\"}".formatted(curso.getIdCurso(), maestro.getIdMaestro(), aula.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void registrar_periodoFormatoInvalidoSeparadorInvalido_retorna400() throws Exception {
        // Arrange
        String json = "{\"idCurso\":%d,\"idMaestro\":%d,\"idAula\":%d,\"periodo\":\"2026/01\"}".formatted(curso.getIdCurso(), maestro.getIdMaestro(), aula.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void registrar_periodoFormatoInvalidoVacio_retorna400() throws Exception {
        // Arrange
        String json = "{\"idCurso\":%d,\"idMaestro\":%d,\"idAula\":%d,\"periodo\":\"\"}".formatted(curso.getIdCurso(), maestro.getIdMaestro(), aula.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void registrar_periodoFormatoValido_registraGrupo() throws Exception {
        // Arrange
        String json = requestValido();

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().isCreated()).andExpect(jsonPath("$.periodo").value("2026-02"));
    }
    @Test
    void actualizar_periodoFormatoInvalido_retorna400() throws Exception {
        // Arrange
        Long id = grupo.getId();
        String json = "{\"idCurso\":%d,\"idMaestro\":%d,\"idAula\":%d,\"periodo\":\"2026-13\"}".formatted(curso.getIdCurso(), maestro.getIdMaestro(), aula.getId());

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void eliminar_conHorarios_lanzaEntidadRelacionadaException() throws Exception {
        // Arrange
        guardarHorario(grupo, "08:00", "10:00");
        Long id = grupo.getId();

        // Act
        ResultActions resultado = mvc.perform(delete(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().is(409))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }

    private String requestValido() {
        return "{\"idCurso\":%d,\"idMaestro\":%d,\"idAula\":%d,\"periodo\":\"2026-02\"}".formatted(curso.getIdCurso(), maestro.getIdMaestro(), aula.getId());
    }

    private Long prepararEntidad() {
        guardarHorario(grupo, "08:00", "10:00");
        return grupo.getId();
    }
}
