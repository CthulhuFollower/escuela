package com.julian.escuela.support;

import com.julian.escuela.entities.*;
import com.julian.escuela.enums.DiaSemana;
import com.julian.escuela.repositories.*;
import com.julian.escuela.services.alumnos.AlumnoService;
import com.julian.escuela.services.maestros.MaestroService;
import com.julian.escuela.services.aula.AulaService;
import com.julian.escuela.services.cursos.CursoService;
import com.julian.escuela.services.grupos.GrupoService;
import com.julian.escuela.services.horarios.HorariosService;
import com.julian.escuela.services.inscripciones.InscripcionService;
import com.julian.escuela.services.calificaciones.CalificacionService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public abstract class ControllerIntegrationSupport {
    @Autowired protected MockMvc mvc;
    @Autowired protected EntityManager entityManager;
    @Autowired protected JdbcTemplate jdbc;
    @Autowired protected AlumnoRepository alumnoRepository;
    @Autowired protected MaestroRepository maestroRepository;
    @Autowired protected AulaRepository aulaRepository;
    @Autowired protected CursoRepository cursoRepository;
    @Autowired protected GrupoRespository grupoRepository;
    @Autowired protected HorarioRepository horarioRepository;
    @Autowired protected InscripcionRepository inscripcionRepository;
    @Autowired protected CalificacionRepository calificacionRepository;

    // Solo se simula listar() en los casos de error 500; el resto usa el servicio real.
    @MockitoSpyBean protected AlumnoService alumnoService;
    @MockitoSpyBean protected MaestroService maestroService;
    @MockitoSpyBean protected AulaService aulaService;
    @MockitoSpyBean protected CursoService cursoService;
    @MockitoSpyBean protected GrupoService grupoService;
    @MockitoSpyBean protected HorariosService horariosService;
    @MockitoSpyBean protected InscripcionService inscripcionService;
    @MockitoSpyBean protected CalificacionService calificacionService;

    protected Alumno alumno;
    protected Maestro maestro;
    protected Aula aula;
    protected Curso curso;
    protected Grupo grupo;
    protected Inscripcion inscripcion;

    @BeforeEach
    void prepararDatos() {
        jdbc.execute("CREATE ALIAS IF NOT EXISTS GENERAR_EMAIL FOR \"com.julian.escuela.support.FuncionesOracleTest.generarEmail\"");
        jdbc.execute("CREATE ALIAS IF NOT EXISTS GENERAR_MATRICULA FOR \"com.julian.escuela.support.FuncionesOracleTest.generarMatricula\"");
        alumno = Alumno.crear("Juan", "Perez", "Lopez");
        alumno.asignarDatosAcademicos("juan@escuela.com", "PELOJU2601");
        alumnoRepository.saveAndFlush(alumno);
        maestro = maestroRepository.saveAndFlush(
                Maestro.crear("Laura", "Martinez", "Lopez", "laura@escuela.com", "5512345678"));
        aula = aulaRepository.saveAndFlush(Aula.crear("Aula 101", 30));
        curso = cursoRepository.saveAndFlush(Curso.crear("Matematicas I", "Fundamentos", 6));
        grupo = grupoRepository.saveAndFlush(Grupo.crear(curso, maestro, aula, "2026-01"));
        entityManager.clear();
    }

    protected Inscripcion inscribir() {
        return inscripcionRepository.saveAndFlush(Inscripcion.crear(alumno, grupo));
    }

    protected Calificacion calificar(Inscripcion registro) {
        Calificacion calificacion = calificacionRepository.saveAndFlush(
                Calificacion.crear(registro, new BigDecimal("8.5")));
        registro.setCalificacion(calificacion);
        return calificacion;
    }

    protected Horario guardarHorario(Grupo destino, String inicio, String fin) {
        return horarioRepository.saveAndFlush(Horario.crear(destino, DiaSemana.LUNES, inicio, fin));
    }

    protected Grupo grupoOtroPeriodo() {
        return grupoRepository.saveAndFlush(Grupo.crear(curso, maestro, aula, "2026-02"));
    }

    protected Grupo otroGrupoMismoPeriodo() {
        Curso otroCurso = cursoRepository.saveAndFlush(Curso.crear("Fisica", null, 6));
        return grupoRepository.saveAndFlush(Grupo.crear(otroCurso, maestro, aula, "2026-01"));
    }
}
