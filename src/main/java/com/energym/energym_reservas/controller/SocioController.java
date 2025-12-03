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

    @GetMapping
    @Operation(summary = "Obtener todos los socios", description = "Retorna una lista de todos los socios registrados")
    public ResponseEntity<List<SocioResponseDTO>> obtenerTodosLosSocios() {
        List<SocioResponseDTO> socios = socioService.obtenerTodosLosSocios();
        return ResponseEntity.ok(socios);
    }

    @GetMapping("/{id}/clases-personalizadas")
    @Operation(summary = "Obtener clases personalizadas", description = "Devuelve las clases personalizadas disponibles de un Socio")
    public ResponseEntity<Integer> obtenerClasesPersonalizadas(@PathVariable Integer id) {
        SocioResponseDTO socio = socioService.obtenerSocioPorId(id);
        return ResponseEntity.ok(socio.getClasesPersonalizadas());
    }

    @GetMapping("/{id}/historial-asistencia")
    @Operation(summary = "Obtener historial de asistencia")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerHistorialAsistencia(@PathVariable Integer id) {
        List<ReservaResponseDTO> historial = socioService.obtenerHistorialAsistencia(id);
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener socio por ID", description = "Retorna los detalles de un socio específico")
    public ResponseEntity<SocioResponseDTO> obtenerSocioPorId(@PathVariable Integer id) {
        SocioResponseDTO socio = socioService.obtenerSocioPorId(id);
        return ResponseEntity.ok(socio);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo socio", description = "Registra un nuevo socio en el sistema")
    public ResponseEntity<SocioResponseDTO> crearSocio(@Valid @RequestBody SocioRequestDTO request) {
        SocioResponseDTO nuevoSocio = socioService.crearSocio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoSocio);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizar socio", description = "Actualiza los datos de un socio existente")
    public ResponseEntity<SocioResponseDTO> updateSocio(@PathVariable Integer id, @Valid @RequestBody SocioRequestDTO request) {
        SocioResponseDTO socioActualizado = socioService.actualizarSocio(id, request);
        return ResponseEntity.ok(socioActualizado);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar socio", description = "Marca un socio como inactivo (soft delete)")
    public ResponseEntity<Void> eliminarSocio(@PathVariable Integer id) {
        socioService.eliminarSocio(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/permanente")
    @Operation(summary = "Eliminar socio permanentemente", description = "Elimina un socio de forma permanente (hard delete)")
    public ResponseEntity<Void> eliminarSocioPermanente(@PathVariable Integer id) {
        socioService.eliminarSocioPermanente(id);
        return ResponseEntity.noContent().build();
    }
}
