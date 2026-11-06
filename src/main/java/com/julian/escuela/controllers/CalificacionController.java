package com.julian.escuela.controllers;

import com.julian.escuela.dto.calificaciones.CalificacionRequest;
import com.julian.escuela.dto.calificaciones.CalificacionResponse;
import com.julian.escuela.services.calificaciones.CalificacionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calificaciones")
@Tag(name = "API Calificaciones", description = "Metodos para gestion de calificaciones")
public class CalificacionController extends CrudController<CalificacionRequest, CalificacionResponse, CalificacionService> {

    public CalificacionController(CalificacionService service) {
        super(service);
    }
}
