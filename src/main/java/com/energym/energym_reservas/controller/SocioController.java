package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.request.SocioRequestDTO;
import com.energym.energym_reservas.dto.response.ReservaResponseDTO;
import com.energym.energym_reservas.dto.response.SocioResponseDTO;
import com.energym.energym_reservas.service.SocioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", originPatterns = "*")
@RestController
@RequestMapping("/api/socios")
@RequiredArgsConstructor
@Tag(name = "Socios", description = "API para gestión de socios del gimnasio")
public class SocioController {

    private final SocioService socioService;

    @PreAuthorize("hasAnyRole('ADMIN','ENTRENADOR')")
    @GetMapping
    @Operation(summary = "Obtener todos los socios", description = "Retorna una lista de todos los socios registrados")
    public ResponseEntity<List<SocioResponseDTO>> obtenerTodosLosSocios() {
        List<SocioResponseDTO> socios = socioService.obtenerTodosLosSocios();
        return ResponseEntity.ok(socios);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ENTRENADOR')")
    @GetMapping("/{id}/clases-personalizadas")
    @Operation(summary = "Obtener clases personalizadas", description = "Devuelve las clases personalizadas disponibles de un Socio")
    public ResponseEntity<Integer> obtenerClasesPersonalizadas(@PathVariable("id") Integer id) {
        SocioResponseDTO socio = socioService.obtenerSocioPorId(id);
        return ResponseEntity.ok(socio.getClasesPersonalizadas());
    }

    @PreAuthorize("hasAnyRole('ADMIN','ENTRENADOR')")
    @GetMapping("/{id}/historial-asistencia")
    @Operation(summary = "Obtener historial de asistencia")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerHistorialAsistencia(@PathVariable("id") Integer id) {
        List<ReservaResponseDTO> historial = socioService.obtenerHistorialAsistencia(id);
        return ResponseEntity.ok(historial);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ENTRENADOR')")
    @GetMapping("/{id}")
    @Operation(summary = "Obtener socio por ID", description = "Retorna los detalles de un socio específico")
    public ResponseEntity<SocioResponseDTO> obtenerSocioPorId(@PathVariable("id") Integer id) {
        SocioResponseDTO socio = socioService.obtenerSocioPorId(id);
        return ResponseEntity.ok(socio);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SOCIO')")
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar socio", description = "Actualiza los datos de un socio existente")
    public ResponseEntity<SocioResponseDTO> actualizarSocio(@PathVariable("id") Integer id, @Valid @RequestBody SocioRequestDTO request) {
        SocioResponseDTO socioActualizado = socioService.actualizarSocio(id, request);
        return ResponseEntity.ok(socioActualizado);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SOCIO')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar socio", description = "Marca un socio como inactivo (soft delete)")
    public ResponseEntity<Void> eliminarSocio(@PathVariable("id") Integer id) {
        socioService.eliminarSocio(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/permanente")
    @Operation(summary = "Eliminar socio permanentemente", description = "Elimina un socio de forma permanente (hard delete)")
    public ResponseEntity<Void> eliminarSocioPermanente(@PathVariable("id") Integer id) {
        socioService.eliminarSocioPermanente(id);
        return ResponseEntity.noContent().build();
    }
}
