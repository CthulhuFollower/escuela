package com.julian.escuela.controllers;

import com.julian.escuela.docs.ProblemaDoc;
import com.julian.escuela.services.CrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Validated
// Errores que pueden ocurrir en cualquier endpoint:
@ApiResponse(responseCode = "400", description = "Datos o parámetros inválidos",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
public class CrudController <REQUEST, RESPONSE, SERVICE extends CrudService<REQUEST, RESPONSE>> {

    protected final SERVICE service;

    @GetMapping
    @Operation(summary = "Listar registos")
    @ApiResponse(responseCode = "200", description = "Listado obtenido")
    public ResponseEntity<List<RESPONSE>> listar(){
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener registro por ID")
    @ApiResponse(responseCode = "200", description = "Registro encontrado")
    @ApiResponse(responseCode = "404", description = "El registro no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<RESPONSE> obtenerPorId(
            @Parameter(description = "Id del registro", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo registro")
    @ApiResponse(responseCode = "201", description = "Registro creado")
    @ApiResponse(responseCode = "409", description = "Conflicto con datos existentes",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<RESPONSE> registrar(@Valid @RequestBody REQUEST request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.registrar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un registro existente")
    @ApiResponse(responseCode = "200", description = "Registro actualizado")
    @ApiResponse(responseCode = "404", description = "El registro no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<RESPONSE> actualizar(
            @Parameter(description = "Id del producto", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,
            @Valid @RequestBody REQUEST request
    ) {
        return ResponseEntity.ok(service.actualizar(request, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un registro")
    @ApiResponse(responseCode = "204", description = "Registro eliminado")
    @ApiResponse(responseCode = "404", description = "El registro no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "El registro está en uso y no puede eliminarse",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Id del registro", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }


}
