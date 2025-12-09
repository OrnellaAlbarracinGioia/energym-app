package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.request.ClasesBatchCreateRequestDTO;
import com.energym.energym_reservas.dto.response.ClaseResponseDTO;
import com.energym.energym_reservas.service.ClaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", originPatterns = "*")
@RestController
@RequestMapping("/api/clases")
@RequiredArgsConstructor
@Tag(name = "Clases", description = "API para gestión de clases del gimnasio")
public class ClaseController {

    private final ClaseService claseService;

    @GetMapping
    @Operation(summary = "Obtener las clases existentes en sistema", description = "Lista las clases")
    public ResponseEntity<List<ClaseResponseDTO>> obtenerClases() {
        List<ClaseResponseDTO> clases = claseService.obtenerClases();
        return ResponseEntity.ok(clases);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener la Clase según el ID")
    public ResponseEntity<ClaseResponseDTO> obtenerClasePorId(@PathVariable("id") Integer id) {
        ClaseResponseDTO clase = claseService.obtenerClasePorId(id);
        return ResponseEntity.ok(clase);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Crear clases asociadas a una Actividad", description = "Crea en el sistema clases")
    public ResponseEntity<List<ClaseResponseDTO>> crearClases(@Valid @RequestBody ClasesBatchCreateRequestDTO request) {
        List<ClaseResponseDTO> clasesCreadas = claseService.crearClases(request);
        return ResponseEntity.ok().body(clasesCreadas);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar la clase")
    public ResponseEntity<Void> eliminarClase(@PathVariable("id") Integer id) {
        claseService.eliminarClase(id);
        return ResponseEntity.noContent().build();
    }

}
