package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.ReservaDTO;
import com.energym.energym_reservas.service.ReservaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@Tag(name = "Reservas", description = "API para reservas del gimnasio")
@RequiredArgsConstructor
public class ReservaController {
    
    private final ReservaService reservaService;
    
    // CREATE - Crear nueva reserva
    @PostMapping
    public ResponseEntity<ReservaDTO> crearReserva(@Valid @RequestBody ReservaDTO reservaDTO) {
        try {
            ReservaDTO reserva = reservaService.crearReserva(reservaDTO);
            return new ResponseEntity<>(reserva, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    // READ - Obtener todas las reservas
    @GetMapping
    public ResponseEntity<List<ReservaDTO>> obtenerTodasLasReservas() {
        List<ReservaDTO> reservas = reservaService.obtenerTodasLasReservas();
        return ResponseEntity.ok(reservas);
    }
    
    // READ - Obtener reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> obtenerReservaPorId(@PathVariable Integer id) {
        try {
            ReservaDTO reserva = reservaService.obtenerReservaPorId(id);
            return ResponseEntity.ok(reserva);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    // READ - Obtener reservas por socio
    @GetMapping("/socio/{socioId}")
    public ResponseEntity<List<ReservaDTO>> obtenerReservasPorSocio(@PathVariable Integer socioId) {
        List<ReservaDTO> reservas = reservaService.obtenerReservasPorSocio(socioId);
        return ResponseEntity.ok(reservas);
    }
    
    // READ - Obtener reservas por clase
    @GetMapping("/clase/{claseId}")
    public ResponseEntity<List<ReservaDTO>> obtenerReservasPorClase(@PathVariable Integer claseId) {
        List<ReservaDTO> reservas = reservaService.obtenerReservasPorClase(claseId);
        return ResponseEntity.ok(reservas);
    }
    
    // READ - Obtener reservas por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReservaDTO>> obtenerReservasPorEstado(@PathVariable String estado) {
        List<ReservaDTO> reservas = reservaService.obtenerReservasPorEstado(estado);
        return ResponseEntity.ok(reservas);
    }
    
    // UPDATE - Actualizar reserva
    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO> actualizarReserva(
            @PathVariable Integer id, 
            @Valid @RequestBody ReservaDTO reservaDTO) {
        try {
            ReservaDTO reserva = reservaService.actualizarReserva(id, reservaDTO);
            return ResponseEntity.ok(reserva);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    // UPDATE - Cancelar reserva
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaDTO> cancelarReserva(@PathVariable Integer id) {
        try {
            ReservaDTO reserva = reservaService.cancelarReserva(id);
            return ResponseEntity.ok(reserva);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    // DELETE - Eliminar reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Integer id) {
        try {
            reservaService.eliminarReserva(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
