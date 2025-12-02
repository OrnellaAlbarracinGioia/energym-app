package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.request.SocioRequestDTO;
import com.energym.energym_reservas.dto.response.ReservaResponseDTO;
import com.energym.energym_reservas.dto.response.SocioResponseDTO;
import com.energym.energym_reservas.service.SocioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socios")
@RequiredArgsConstructor
@Tag(name = "Socios", description = "API para gestión de socios del gimnasio")
public class SocioController {

    private final SocioService socioService;

    /**
     * Obtener todos los socios
     */
    @GetMapping
    @Operation(summary = "Obtener todos los socios", description = "Retorna una lista de todos los socios registrados")
    public ResponseEntity<List<SocioResponseDTO>> getAllSocios() {
        List<SocioResponseDTO> socios = socioService.getAllSocios();
        return ResponseEntity.ok(socios);
    }

    @GetMapping("/{id}/clases-personalizadas")
    @Operation(summary = "Obtener clases personalizadas", description = "Devuelve las clases personalizadas disponibles de un Socio")
    public ResponseEntity<Integer> obtenerClasesPersonalizadas(@PathVariable Integer id) {
        SocioResponseDTO socio = socioService.getSocioById(id);
        return ResponseEntity.ok(socio.getClasesPersonalizadas());
    }

    @GetMapping("/{id}/historial-asistencia")
    @Operation(summary = "Obtener historial de asistencia")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerHistorialAsistencia(@PathVariable Integer id) {
        List<ReservaResponseDTO> historial = socioService.obtenerHistorialAsistencia(id);
        return ResponseEntity.ok(historial);
    }

    /**
     * Obtener un socio por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener socio por ID", description = "Retorna los detalles de un socio específico")
    public ResponseEntity<SocioResponseDTO> getSocioById(@PathVariable Integer id) {
        SocioResponseDTO socio = socioService.getSocioById(id);
        return ResponseEntity.ok(socio);
    }

    /**
     * Crear un nuevo socio
     */
    @PostMapping
    @Operation(summary = "Crear nuevo socio", description = "Registra un nuevo socio en el sistema")
    public ResponseEntity<SocioResponseDTO> createSocio(@Valid @RequestBody SocioRequestDTO request) {
        SocioResponseDTO nuevoSocio = socioService.createSocio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoSocio);
    }

    /**
     * Actualizar un socio existente
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar socio", description = "Actualiza los datos de un socio existente")
    public ResponseEntity<SocioResponseDTO> updateSocio(@PathVariable Integer id, @Valid @RequestBody SocioRequestDTO request) {
        SocioResponseDTO socioActualizado = socioService.updateSocio(id, request);
        return ResponseEntity.ok(socioActualizado);
    }

    /**
     * Eliminar un socio (soft delete)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar socio", description = "Marca un socio como inactivo (soft delete)")
    public ResponseEntity<Void> deleteSocio(@PathVariable Integer id) {
        socioService.deleteSocio(id);
        return ResponseEntity.noContent().build();
    }

    /*
      Eliminar permanentemente un socio
     */
    @DeleteMapping("/{id}/permanente")
    @Operation(summary = "Eliminar socio permanentemente", description = "Elimina un socio de forma permanente (hard delete)")
    public ResponseEntity<Void> deleteSocioPermanente(@PathVariable Integer id) {
        socioService.deleteSocioPermanente(id);
        return ResponseEntity.noContent().build();
    }

}
