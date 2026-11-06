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

class HorarioControllerIT extends ControllerIntegrationSupport {
    @Test
    void actualizar_cambiaHoras_excluyeElPropioHorario() throws Exception {
        // Arrange
        Long id = prepararEntidad();
        String json = "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"09:00\",\"horaFin\":\"11:00\"}"
                .formatted(grupo.getId());

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id)
                .contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.horario").value("Lunes 09:00 11:00"));
    }
    private static final String RUTA = "/api/horarios";

    @Test
    void registrar_datosValidos_registraHorario() throws Exception {
        // Arrange
        String json = requestValido();

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().isCreated()).andExpect(jsonPath("$.horario").value("Lunes 08:00 10:00"));
    }
    @Test
    void obtenerPorId_horarioExistente_retornaHorario() throws Exception {
        // Arrange
        Long id = prepararEntidad();

        // Act
        ResultActions resultado = mvc.perform(get(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id)).andExpect(jsonPath("$.horario").value("Lunes 08:00 10:00"));
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
    void listar_conHorarios_retornaLista() throws Exception {
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
        doThrow(new IllegalStateException("fallo simulado")).when(horariosService).listar();

        // Act
        ResultActions resultado = mvc.perform(get(RUTA));

        // Assert
        resultado.andExpect(status().is(500))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.detail").value("Error interno del servidor. Contacte al administrador."));
    }
    @Test
    void actualizar_datosValidos_actualizaHorario() throws Exception {
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
    void eliminar_horarioExistente_eliminaHorario() throws Exception {
        // Arrange
        Long id = prepararEntidad();

        // Act
        ResultActions resultado = mvc.perform(delete(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().isNoContent());
        assertThat(horarioRepository.existsById(id)).isFalse();
    }
    @Test
    void eliminar_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        // Arrange
        long id = 999999L;

        // Act
        ResultActions resultado = mvc.perform(delete(RUTA + "/{id}", id));

        // Assert
        resultado.andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void registrar_idNulo_retorna400() throws Exception {
        // Arrange
        String json = "{\"idGrupo\":null,\"dia\":\"Lunes\",\"horaInicio\":\"08:00\",\"horaFin\":\"10:00\"}";

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
        String json = "{\"idGrupo\":0,\"dia\":\"Lunes\",\"horaInicio\":\"08:00\",\"horaFin\":\"10:00\"}";

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void registrar_grupoInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        // Arrange
        String json = "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"08:00\",\"horaFin\":\"10:00\"}".formatted(999999L);

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void registrar_horaInicioMayorAHoraFin_lanzaDatoInvalidoException() throws Exception {
        // Arrange
        String json = "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"10:00\",\"horaFin\":\"08:00\"}".formatted(grupo.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void registrar_horaInicioIgualAHoraFin_lanzaDatoInvalidoException() throws Exception {
        // Arrange
        String json = "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"08:00\",\"horaFin\":\"08:00\"}".formatted(grupo.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void registrar_formatoHoraInvalido_retorna400() throws Exception {
        // Arrange
        String json = "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"8:00\",\"horaFin\":\"10:00\"}".formatted(grupo.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void registrar_formatoHoraFueraDeRango_retorna400() throws Exception {
        // Arrange
        String json = "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"25:00\",\"horaFin\":\"10:00\"}".formatted(grupo.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void registrar_mismoGrupoDatosIgualesExceptoPeriodo_registraExitosamente() throws Exception {
        // Arrange
        guardarHorario(grupo, "08:00", "10:00");
        Grupo otroPeriodo = grupoOtroPeriodo();
        String json = "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"08:00\",\"horaFin\":\"10:00\"}".formatted(otroPeriodo.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().isCreated()).andExpect(jsonPath("$.grupo.periodo").value("2026-02"));
    }
    @Test
    void registrar_aulaOcupadaPorOtroGrupo_lanzaConflictoException() throws Exception {
        // Arrange
        guardarHorario(grupo, "08:00", "10:00");
        Grupo otro = otroGrupoMismoPeriodo();
        String json = "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"09:00\",\"horaFin\":\"11:00\"}".formatted(otro.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(409))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void actualizar_horaInicioMayorAHoraFin_lanzaDatoInvalidoException() throws Exception {
        // Arrange
        Long id = prepararEntidad();
        String json = "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"10:00\",\"horaFin\":\"08:00\"}".formatted(grupo.getId());

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void actualizar_horaInicioIgualAHoraFin_lanzaDatoInvalidoException() throws Exception {
        // Arrange
        Long id = prepararEntidad();
        String json = "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"08:00\",\"horaFin\":\"08:00\"}".formatted(grupo.getId());

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void actualizar_formatoHoraSinCero_retorna400() throws Exception {
        // Arrange
        Long id = prepararEntidad();
        String json = "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"8:00\",\"horaFin\":\"10:00\"}".formatted(grupo.getId());

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void actualizar_formatoHoraFueraDeRango_retorna400() throws Exception {
        // Arrange
        Long id = prepararEntidad();
        String json = "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"25:00\",\"horaFin\":\"10:00\"}".formatted(grupo.getId());

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(400))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.errores").isArray());
    }
    @Test
    void actualizar_mismoGrupoDatosIgualesExceptoPeriodo_actualizaExitosamente() throws Exception {
        // Arrange
        guardarHorario(grupo, "08:00", "10:00");
        Grupo otroPeriodo = grupoOtroPeriodo();
        Long id = guardarHorario(otroPeriodo, "12:00", "13:00").getId();
        String json = "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"08:00\",\"horaFin\":\"10:00\"}".formatted(otroPeriodo.getId());

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().isOk()).andExpect(jsonPath("$.grupo.periodo").value("2026-02"));
    }
    @Test
    void actualizar_aulaOcupadaPorOtroGrupo_lanzaConflictoException() throws Exception {
        // Arrange
        guardarHorario(grupo, "08:00", "10:00");
        Grupo otro = otroGrupoMismoPeriodo();
        Long id = guardarHorario(otro, "12:00", "13:00").getId();
        String json = "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"09:00\",\"horaFin\":\"11:00\"}".formatted(otro.getId());

        // Act
        ResultActions resultado = mvc.perform(put(RUTA + "/{id}", id).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(409))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void registrar_traslapeEnElMismoGrupo_lanzaConflictoException() throws Exception {
        // Arrange
        guardarHorario(grupo, "08:00", "10:00");
        String json = "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"09:00\",\"horaFin\":\"11:00\"}".formatted(grupo.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().is(409))
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.detail").isNotEmpty());
    }
    @Test
    void registrar_horariosConsecutivos_registraHorario() throws Exception {
        // Arrange
        guardarHorario(grupo, "08:00", "10:00");
        String json = "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"10:00\",\"horaFin\":\"12:00\"}".formatted(grupo.getId());

        // Act
        ResultActions resultado = mvc.perform(post(RUTA).contentType("application/json").content(json));

        // Assert
        resultado.andExpect(status().isCreated());
    }

    private String requestValido() {
        return "{\"idGrupo\":%d,\"dia\":\"Lunes\",\"horaInicio\":\"08:00\",\"horaFin\":\"10:00\"}".formatted(grupo.getId());
    }

    private Long prepararEntidad() {
        return guardarHorario(grupo, "08:00", "10:00").getId();
    }
}
