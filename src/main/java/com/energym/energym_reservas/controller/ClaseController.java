package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.request.ClasesBatchCreateRequestDTO;
import com.energym.energym_reservas.dto.response.ClaseResponseDTO;
import com.energym.energym_reservas.service.ClaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    @Operation(summary = "Crear clases asociadas a una Actividad", description = "Crea en el sistema clases")
    public ResponseEntity<List<ClaseResponseDTO>> crearClases(@Valid @RequestBody ClasesBatchCreateRequestDTO request) {
        List<ClaseResponseDTO> clasesCreadas = claseService.crearClases(request);
        return ResponseEntity.ok().body(clasesCreadas);
    }
}
