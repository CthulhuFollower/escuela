package com.julian.escuela.controllers;

import com.julian.escuela.dto.alumnos.AlumnoResponse;
import com.julian.escuela.dto.alumnos.AlumnosRequest;
import com.julian.escuela.services.alumnos.AlumnoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/alumnos")
@Tag(name = "API Alumnos", description = "Metodo para gestion de alumnos")
public class AlumnoController extends CrudController<AlumnosRequest, AlumnoResponse, AlumnoService>{

    public AlumnoController(AlumnoService service){
        super(service);
    }
}
