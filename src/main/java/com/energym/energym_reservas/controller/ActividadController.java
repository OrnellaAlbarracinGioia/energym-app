package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.ActividadDTO;
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
    public ResponseEntity<List<ActividadDTO>> getAllActividades() {
        List<ActividadDTO> actividades = actividadService.getAllActividades();
        return ResponseEntity.ok(actividades);
    }

    /**
     * Obtener una actividad por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener actividad por ID", description = "Retorna los detalles de una actividad específica")
    public ResponseEntity<ActividadDTO> getActividadById(@PathVariable Integer id) {
        ActividadDTO actividad = actividadService.getActividadById(id);
        return ResponseEntity.ok(actividad);
    }

    /**
     * Crear una nueva actividad
     */
    @PostMapping
    @Operation(summary = "Crear nueva actividad", description = "Crea una nueva actividad en el sistema")
    public ResponseEntity<ActividadDTO> createActividad(@Valid @RequestBody ActividadDTO actividadDTO) {
        ActividadDTO nuevaActividad = actividadService.createActividad(actividadDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaActividad);
    }

    /**
     * Actualizar una actividad existente
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar actividad", description = "Actualiza los datos de una actividad existente")
    public ResponseEntity<ActividadDTO> updateActividad(@PathVariable Integer id,
                                                    @Valid @RequestBody ActividadDTO actividadDTO) {
        ActividadDTO actividadActualizada = actividadService.updateActividad(id, actividadDTO);
        return ResponseEntity.ok(actividadActualizada);
    }

    /**
     * Eliminar una actividad
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar actividad", description = "Elimina una actividad del sistema")
    public ResponseEntity<Void> deleteActividad(@PathVariable Integer id) {
        actividadService.deleteActividad(id);
        return ResponseEntity.noContent().build();
    }


}
