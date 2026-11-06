package com.julian.escuela.controllers;

import com.julian.escuela.dto.cursos.CursoRequest;
import com.julian.escuela.dto.cursos.CursoResponse;
import com.julian.escuela.services.cursos.CursoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cursos")
@Tag(name = "API Cursos", description = "Metodos para gestion de cursos")
public class CursoController extends CrudController<CursoRequest, CursoResponse, CursoService> {

    public CursoController(CursoService service) {
        super(service);
    }
}
