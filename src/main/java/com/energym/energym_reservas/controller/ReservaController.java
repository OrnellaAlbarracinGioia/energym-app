package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.ReservaDTO;
import com.energym.energym_reservas.service.ReservaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@Tag(name = "Reservas", description = "API para reservas del gimnasio")
@RequiredArgsConstructor
@Slf4j
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaDTO> crearReserva(@Valid @RequestBody ReservaDTO reservaDTO) {
        try {
            ReservaDTO reserva = reservaService.crearReserva(reservaDTO);
            return new ResponseEntity<>(reserva, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error al crear reserva: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> obtenerTodasLasReservas() {
        try {
            List<ReservaDTO> reservas = reservaService.obtenerTodasLasReservas();
            return ResponseEntity.ok(reservas);
        } catch (Exception e) {
            log.error("Error al obtener reservas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> obtenerReservaPorId(@PathVariable Integer id) {
        try {
            ReservaDTO reserva = reservaService.obtenerReservaPorId(id);
            return ResponseEntity.ok(reserva);
        } catch (Exception e) {
            log.error("Error al obtener reserva: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/socio/{socioId}")
    public ResponseEntity<List<ReservaDTO>> obtenerReservasPorSocio(@PathVariable Integer socioId) {
        try {
            List<ReservaDTO> reservas = reservaService.obtenerReservasPorSocio(socioId);
            return ResponseEntity.ok(reservas);
        } catch (Exception e) {
            log.error("Error al obtener reservas por socio: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/clase/{claseId}")
    public ResponseEntity<List<ReservaDTO>> obtenerReservasPorClase(@PathVariable Integer claseId) {
        try {
            List<ReservaDTO> reservas = reservaService.obtenerReservasPorClase(claseId);
            return ResponseEntity.ok(reservas);
        } catch (Exception e) {
            log.error("Error al obtener reservas por clase: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReservaDTO>> obtenerReservasPorEstado(@PathVariable String estado) {
        try {
            List<ReservaDTO> reservas = reservaService.obtenerReservasPorEstado(estado);
            return ResponseEntity.ok(reservas);
        } catch (Exception e) {
            log.error("Error al obtener reservas por estado: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO> actualizarReserva(
            @PathVariable Integer id,
            @Valid @RequestBody ReservaDTO reservaDTO) {
        try {
            ReservaDTO reserva = reservaService.actualizarReserva(id, reservaDTO);
            return ResponseEntity.ok(reserva);
        } catch (Exception e) {
            log.error("Error al actualizar reserva: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaDTO> cancelarReserva(@PathVariable Integer id) {
        try {
            ReservaDTO reserva = reservaService.cancelarReserva(id);
            return ResponseEntity.ok(reserva);
        } catch (Exception e) {
            log.error("Error al cancelar reserva: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Integer id) {
        try {
            reservaService.eliminarReserva(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al eliminar reserva: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
