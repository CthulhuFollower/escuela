package com.julian.escuela.controllers;

import com.julian.escuela.dto.grupos.GrupoRequest;
import com.julian.escuela.dto.grupos.GrupoResponse;
import com.julian.escuela.services.grupos.GrupoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grupos")
@Tag(name = "API Grupos", description = "Metodos para gestion de grupos")
public class GrupoController extends CrudController<GrupoRequest, GrupoResponse, GrupoService> {

    public GrupoController(GrupoService service) {
        super(service);
    }
}
