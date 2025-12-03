package com.energym.energym_reservas.controller;

import com.energym.energym_reservas.dto.request.ReservaRequestDTO;
import com.energym.energym_reservas.dto.response.ReservaResponseDTO;
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
    public ResponseEntity<ReservaResponseDTO> crearReserva(@Valid @RequestBody ReservaRequestDTO request) {
        try {
            ReservaResponseDTO reserva = reservaService.crearReserva(request);
            log.info("Reserva creada");
            return new ResponseEntity<>(reserva, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(" Error al crear reserva: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> obtenerTodasLasReservas() {
        try {
            List<ReservaResponseDTO> reservas = reservaService.obtenerTodasLasReservas();
            return ResponseEntity.ok(reservas);
        } catch (Exception e) {
            log.error(" Error al obtener reservas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> obtenerReservaPorId(@PathVariable Integer id) {
        try {
            ReservaResponseDTO reserva = reservaService.obtenerReservaPorId(id);
            return ResponseEntity.ok(reserva);
        } catch (Exception e) {
            log.error(" Error al obtener reserva: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/socio/{socioId}")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservasPorSocio(@PathVariable Integer socioId) {
        try {
            List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorSocio(socioId);
            return ResponseEntity.ok(reservas);
        } catch (Exception e) {
            log.error(" Error al obtener reservas por socio: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/clase/{claseId}")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservasPorClase(@PathVariable Integer claseId) {
        try {
            List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorClase(claseId);
            return ResponseEntity.ok(reservas);
        } catch (Exception e) {
            log.error(" Error al obtener reservas por clase: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservasPorEstado(@PathVariable String estado) {
        try {
            List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorEstado(estado);
            return ResponseEntity.ok(reservas);
        } catch (Exception e) {
            log.error(" Error al obtener reservas por estado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PatchMapping("/{id}/marcar-asistencia")
    public ResponseEntity<ReservaResponseDTO> marcarAsistencia(@PathVariable Integer id) {
        ReservaResponseDTO reserva = reservaService.marcarAsistencia(id);
        return ResponseEntity.ok(reserva);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponseDTO> cancelarReserva(@PathVariable Integer id) {
        try {
            ReservaResponseDTO reserva = reservaService.cancelarReserva(id);
            log.info(" Reserva {} cancelada", id);
            return ResponseEntity.ok(reserva);
        } catch (Exception e) {
            log.error(" Error al cancelar reserva: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
