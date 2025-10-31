package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.ClaseDTO;
import com.energym.energym_reservas.service.ClaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clases")
@RequiredArgsConstructor
@Tag(name = "Clases", description = "API para gestión de clases del gimnasio")
public class ClaseController {

    private final ClaseService claseService;

    /**
     * Obtener todas las clases
     */
    @GetMapping
    @Operation(summary = "Obtener todas las clases", description = "Retorna una lista de todas las clases disponibles")
    public ResponseEntity<List<ClaseDTO>> getAllClases() {
        List<ClaseDTO> clases = claseService.getAllClases();
        return ResponseEntity.ok(clases);
    }

    /**
     * Obtener una clase por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener clase por ID", description = "Retorna los detalles de una clase específica")
    public ResponseEntity<ClaseDTO> getClaseById(@PathVariable Integer id) {
        ClaseDTO clase = claseService.getClaseById(id);
        return ResponseEntity.ok(clase);
    }

    /**
     * Crear una nueva clase
     */
    @PostMapping
    @Operation(summary = "Crear nueva clase", description = "Crea una nueva clase en el sistema")
    public ResponseEntity<ClaseDTO> createClase(@Valid @RequestBody ClaseDTO claseDTO) {
        ClaseDTO nuevaClase = claseService.createClase(claseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaClase);
    }

    /**
     * Actualizar una clase existente
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar clase", description = "Actualiza los datos de una clase existente")
    public ResponseEntity<ClaseDTO> updateClase(@PathVariable Integer id, 
                                                 @Valid @RequestBody ClaseDTO claseDTO) {
        ClaseDTO claseActualizada = claseService.updateClase(id, claseDTO);
        return ResponseEntity.ok(claseActualizada);
    }

    /**
     * Eliminar una clase
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar clase", description = "Elimina una clase del sistema")
    public ResponseEntity<Void> deleteClase(@PathVariable Integer id) {
        claseService.deleteClase(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtener clases por sucursal
     */
    @GetMapping("/sucursal/{sucursalId}")
    @Operation(summary = "Obtener clases por sucursal", description = "Retorna todas las clases de una sucursal específica")
    public ResponseEntity<List<ClaseDTO>> getClasesBySucursal(@PathVariable Integer sucursalId) {
        List<ClaseDTO> clases = claseService.getClasesBySucursal(sucursalId);
        return ResponseEntity.ok(clases);
    }
}
