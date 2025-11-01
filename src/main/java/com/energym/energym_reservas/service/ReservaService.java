package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.ReservaDTO;
import com.energym.energym_reservas.entity.Clase;
import com.energym.energym_reservas.entity.Estado;
import com.energym.energym_reservas.entity.Reserva;
import com.energym.energym_reservas.entity.Socio;
import com.energym.energym_reservas.exception.CapacidadLlenaException;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.repository.ClaseRepository;
import com.energym.energym_reservas.repository.ReservaRepository;
import com.energym.energym_reservas.repository.SocioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final SocioRepository socioRepository;
    private final ClaseRepository claseRepository;

    // CREATE valida
    public ReservaDTO crearReserva(ReservaDTO reservaDTO) throws CapacidadLlenaException {

        Socio socio = socioRepository.findById(reservaDTO.getSocioId())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con id: " + reservaDTO.getSocioId()));

        if(!socio.getActivo()) {
            throw new IllegalStateException("Socio no se encuentra activo");
        }

        Clase clase = claseRepository.findById(reservaDTO.getClaseId())
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con id: " + reservaDTO.getClaseId()));

        if (clase.getFecha().isBefore(LocalDate.now())) {
            throw new IllegalStateException("No se puede reservar una clase que ya pasó");
        }

        boolean yaReservado = reservaRepository.existsBySocioIdAndClaseIdAndEstado(
                reservaDTO.getSocioId(),
                reservaDTO.getClaseId(),
                Estado.CONFIRMADA
        );

        if (yaReservado) {
            throw new IllegalStateException("El socio ya tiene una reserva confirmada en esta clase");
        }

        Integer cuposDisponibles = verificarDisponibilidad(clase.getId());

        if(cuposDisponibles <= 0){
            throw new CapacidadLlenaException(
                    "La clase '" + clase.getActividad().getNombre() + "' está llena", clase.getActividad().getNombre(), cuposDisponibles);
        }

        Reserva reserva = Reserva.builder()
                .socio(socio)
                .clase(clase)
                .build();

        Reserva reservaGuardada = reservaRepository.save(reserva);
        log.info("Reserva creada. ID: {}", reservaGuardada.getId());

        return convertirADTO(reservaGuardada);
    }

    // READ - Obtener todas las reservas
    @Transactional(readOnly = true)
    public List<ReservaDTO> obtenerTodasLasReservas() {
        return reservaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // READ - Obtener reserva por ID
    @Transactional(readOnly = true)
    public ReservaDTO obtenerReservaPorId(Integer id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
        return convertirADTO(reserva);
    }

    // READ - Obtener reservas por socio
    @Transactional(readOnly = true)
    public List<ReservaDTO> obtenerReservasPorSocio(Integer socioId) {
        return reservaRepository.findBySocioId(socioId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // READ - Obtener reservas por clase
    @Transactional(readOnly = true)
    public List<ReservaDTO> obtenerReservasPorClase(Integer claseId) {
        return reservaRepository.findByClaseId(claseId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // READ - Obtener reservas por estado
    @Transactional(readOnly = true)
    public List<ReservaDTO> obtenerReservasPorEstado(String estado) {
        return reservaRepository.findByEstado(Estado.valueOf(estado)).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // UPDATE - Actualizar reserva
    public ReservaDTO actualizarReserva(Integer id, ReservaDTO reservaDTO) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));

        if (reservaDTO.getEstado() != null) {
            reserva.setEstado(Estado.valueOf(reservaDTO.getEstado().toUpperCase()));
        }

        Reserva reservaActualizada = reservaRepository.save(reserva);
        log.info("Reserva {} actualizada", id);

        return convertirADTO(reservaActualizada);
    }

    // UPDATE - Cancelar reserva
    public ReservaDTO cancelarReserva(Integer id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));

        if (!reserva.getEstado().equals(Estado.CONFIRMADA)) {
            throw new RuntimeException("Solo se pueden cancelar reservas confirmadas");
        }

        reserva.setEstado(Estado.CANCELADA);
        reserva.setFechaCancelacion(LocalDateTime.now());

        Reserva reservaActualizada = reservaRepository.save(reserva);
        log.info("Reserva {} cancelada", id);
        return convertirADTO(reservaActualizada);
    }

    // DELETE
    public void eliminarReserva(Integer id) {
        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException("Reserva no encontrada con id: " + id);
        }
        reservaRepository.deleteById(id);
        log.info("Reserva {} eliminada", id);
    }

    public ReservaDTO marcarAsistencia(Integer reservaId, Boolean asistio) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        if (reserva.getClase().getFecha().isAfter(LocalDate.now())) {
            throw new IllegalStateException("No se puede marcar asistencia antes de que la clase ocurra");
        }

        if (asistio) {
            reserva.setEstado(Estado.COMPLETADA);
        }

        reservaRepository.save(reserva);
        return convertirADTO(reserva);
    }


    private Integer verificarDisponibilidad(Integer claseId) {
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new ResourceNotFoundException("Clase no encontrada"));

        Integer reservasConfirmadas = reservaRepository
                .countByClaseIdAndEstado(claseId, Estado.CONFIRMADA);

        Integer capacidadMaxima = clase.getCapacidadMaxima();

        return capacidadMaxima - reservasConfirmadas;
    }

    // Metodo auxiliar para convertir entidad a DTO
    private ReservaDTO convertirADTO(Reserva reserva) {
        return ReservaDTO.builder()
                .id(reserva.getId())
                .socioId(reserva.getSocio().getId())
                .socioNombre(reserva.getSocio().getNombre())
                .claseId(reserva.getClase().getId())
                .fecha(reserva.getFecha())
                .estado(reserva.getEstado().toString())
                .fechaCancelacion(reserva.getFechaCancelacion())
                .build();
    }
}
