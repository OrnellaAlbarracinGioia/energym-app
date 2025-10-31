package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.ClaseDTO;
import com.energym.energym_reservas.dto.ClasesCreateRequestDTO;
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

    @PostMapping
    @Operation(summary = "Crear clases asociadas a una Actividad", description = "Crea en el sistema clases")
    public ResponseEntity<List<ClaseDTO>> createClases(@Valid @RequestBody ClasesCreateRequestDTO clasesCreateRequestDTO) {
        List<ClaseDTO> clasesCreadas = claseService.createClases(clasesCreateRequestDTO);
        return ResponseEntity.ok().body(clasesCreadas);
    }
}
