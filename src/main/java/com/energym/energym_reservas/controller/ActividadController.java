package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.request.ActividadRequestDTO;
import com.energym.energym_reservas.dto.response.ActividadResponseDTO;
import com.energym.energym_reservas.service.ActividadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividades")
@RequiredArgsConstructor
@Tag(name = "Actividades", description = "API para gestión de actividades del gimnasio")
public class ActividadController {

    private final ActividadService actividadService;

    /**
     * Obtener todas las actividades
     */
    @GetMapping
    @Operation(summary = "Obtener todas las actividades", description = "Retorna una lista de todas las actividades disponibles")
    public ResponseEntity<List<ActividadResponseDTO>> obtenerTodasActividades() {
        List<ActividadResponseDTO> actividades = actividadService.obtenerTodasActividades();
        return ResponseEntity.ok(actividades);
    }

    /**
     * Obtener una actividad por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener actividad por ID", description = "Retorna los detalles de una actividad específica")
    public ResponseEntity<ActividadResponseDTO> obtenerActividadPorId(@PathVariable("id") Integer id) {
        ActividadResponseDTO actividad = actividadService.obtenerActividadPorId(id);
        return ResponseEntity.ok(actividad);
    }

    /**
     * Crear una nueva actividad
     */
    @PostMapping
    @Operation(summary = "Crear nueva actividad", description = "Crea una nueva actividad en el sistema")
    public ResponseEntity<ActividadResponseDTO> crearActividad( @Valid @RequestBody ActividadRequestDTO request) {
        ActividadResponseDTO nuevaActividad = actividadService.crearActividad(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaActividad);
    }

    /**
     * Actualizar una actividad existente
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar actividad", description = "Actualiza los datos de una actividad existente")
    public ResponseEntity<ActividadResponseDTO> actualizarActividad(@PathVariable("id") Integer id, @Valid @RequestBody ActividadRequestDTO request) {
        ActividadResponseDTO actividadActualizada = actividadService.actualizarActividad(id, request);
        return ResponseEntity.ok(actividadActualizada);
    }

    /**
     * Eliminar una actividad
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar actividad", description = "Elimina una actividad del sistema")
    public ResponseEntity<Void> eliminarActividad(@PathVariable("id") Integer id) {
        actividadService.eliminarActividad(id);
        return ResponseEntity.noContent().build();
    }


}
