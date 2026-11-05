package com.julian.escuela.controllers;

import com.julian.escuela.dto.aula.AulaRequest;
import com.julian.escuela.dto.aula.AulaResponse;
import com.julian.escuela.services.aula.AulaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aulas")
@Tag(name = "API Aulas", description = "Metodos para gestion de aulas")
public class AulaController extends CrudController<AulaRequest, AulaResponse, AulaService>{

    public AulaController(AulaService service) {
        super(service);
    }
}
