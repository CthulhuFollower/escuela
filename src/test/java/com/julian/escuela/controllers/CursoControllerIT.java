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

class CursoControllerIT extends ControllerIntegrationSupport {
    private static final String RUTA = "/api/cursos";

    @Test
    void registrar_datosValidos_registraCurso() throws Exception {
        // Arrange
        String json = requestValido();

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().isCreated()).andExpect(jsonPath("$.nombre").value("Bases de Datos"));
    }
    @Test
    void obtenerPorId_cursoExistente_retornaCurso() throws Exception {
        // Arrange
        Long id = prepararEntidad();

        // Act
        ResultActions resultado = mvc.perform(get(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id)).andExpect(jsonPath("$.creditos").value(6));
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
    void listar_conCursos_retornaLista() throws Exception {
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
        doThrow(new IllegalStateException("fallo simulado")).when(cursoService).listar();

        // Act
        ResultActions resultado = mvc.perform(get(RUTA));

        // Assert
        resultado.andExpect(status().is(500))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.detail").value("Error interno del servidor. Contacte al administrador."));
    }
    @Test
    void actualizar_mismoNombrePropio_noLanzaConflicto() throws Exception {
        // Arrange
        Long id = prepararEntidad();
        String json = "{\"nombre\":\"Matematicas I\",\"creditos\":8}";

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(json));

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
    void eliminar_sinGrupos_eliminaCurso() throws Exception {
        // Arrange
        Long id = cursoRepository.saveAndFlush(Curso.crear("Libre", null, 6)).getIdCurso();

        // Act
        ResultActions resultado = mvc.perform(delete(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().isNoContent());
        assertThat(cursoRepository.existsById(id)).isFalse();
    }
    @Test
    void eliminar_conGrupos_lanzaEntidadRelacionadaException() throws Exception {
        // Arrange
        Long id = curso.getIdCurso();

        // Act
        ResultActions resultado = mvc.perform(delete(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().is(409))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void registrar_nombreDuplicado_lanzaConflictoException() throws Exception {
        // Arrange
        String json = "{\"nombre\":\"Matematicas I\",\"creditos\":6}";

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(409))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void actualizar_nombreDeOtroCurso_lanzaConflictoException() throws Exception {
        // Arrange
        Long id = cursoRepository.saveAndFlush(Curso.crear("Libre", null, 6)).getIdCurso();
        String json = "{\"nombre\":\"Matematicas I\",\"creditos\":6}";

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(409))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }

    private String requestValido() {
        return "{\"nombre\":\"Bases de Datos\",\"descripcion\":\"SQL\",\"creditos\":8}";
    }

    private Long prepararEntidad() {
        return curso.getIdCurso();
    }
}
