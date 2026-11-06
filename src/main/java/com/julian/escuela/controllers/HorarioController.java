package com.julian.escuela.controllers;

import com.julian.escuela.dto.horarios.HorariosRequest;
import com.julian.escuela.dto.horarios.HorariosResponse;
import com.julian.escuela.services.horarios.HorariosService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/horarios")
@Tag(name = "API Horarios", description = "Metodos para gestion de horarios")
public class HorarioController extends CrudController<HorariosRequest, HorariosResponse, HorariosService> {

    public HorarioController(HorariosService service) {
        super(service);
    }
}
