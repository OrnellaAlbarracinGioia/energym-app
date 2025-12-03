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
        ReservaResponseDTO reserva = reservaService.crearReserva(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> obtenerTodasLasReservas() {
        List<ReservaResponseDTO> reservas = reservaService.obtenerTodasLasReservas();
        return ResponseEntity.ok(reservas);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> obtenerReservaPorId(@PathVariable Integer id) {
        ReservaResponseDTO reserva = reservaService.obtenerReservaPorId(id);
        return ResponseEntity.ok(reserva);
    }

    @GetMapping("/socio/{socioId}")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservasPorSocio(@PathVariable Integer socioId) {
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorSocio(socioId);
        return ResponseEntity.ok(reservas);
    }


    @GetMapping("/clase/{claseId}")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservasPorClase(@PathVariable Integer claseId) {
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorClase(claseId);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReservaResponseDTO>> obtenerReservasPorEstado(@PathVariable String estado) {
        List<ReservaResponseDTO> reservas = reservaService.obtenerReservasPorEstado(estado);
        return ResponseEntity.ok(reservas);
    }


    @PatchMapping("/{id}/marcar-asistencia")
    public ResponseEntity<ReservaResponseDTO> marcarAsistencia(@PathVariable Integer id) {
        ReservaResponseDTO reserva = reservaService.marcarAsistencia(id);
        return ResponseEntity.ok(reserva);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponseDTO> cancelarReserva(@PathVariable Integer id) {
        ReservaResponseDTO reserva = reservaService.cancelarReserva(id);
        return ResponseEntity.ok(reserva);
    }
}
