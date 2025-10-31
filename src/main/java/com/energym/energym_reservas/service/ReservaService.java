package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.ReservaDTO;
import com.energym.energym_reservas.entity.Clase;
import com.energym.energym_reservas.entity.Reserva;
import com.energym.energym_reservas.entity.Socio;
import com.energym.energym_reservas.repository.ClaseRepository;
import com.energym.energym_reservas.repository.ReservaRepository;
import com.energym.energym_reservas.repository.SocioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final SocioRepository socioRepository;
    private final ClaseRepository claseRepository;

    // CREATE valida
    public ReservaDTO crearReserva(ReservaDTO reservaDTO) {
        Socio socio = socioRepository.findById(reservaDTO.getSocioId())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con id: " + reservaDTO.getSocioId()));

        if(!socio.getActivo())
            throw new IllegalStateException("Socio no se encuentra activo");


        Clase clase = claseRepository.findById(reservaDTO.getClaseId())
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con id: " + reservaDTO.getClaseId()));

        Integer reservasConfirmadas = reservaRepository.countReservasConfirmadasByClaseId(clase.getId());

        if(reservasConfirmadas >= clase.getCapacidadMaxima()){
            throw new IllegalStateException("La clase está llena. No se cuenta con cupos disponibles");
        }

        // Validar capacidad disponible
        if (clase.getCuposDisponibles() <= 0) {
            throw new RuntimeException("No hay cupos disponibles para esta clase");
        }

        Reserva reserva = Reserva.builder()
                .socio(socio)
                .clase(clase)
                .fechaClase(reservaDTO.getFechaClase())
                .estado("CONFIRMADA")
                .build();

        Reserva reservaGuardada = reservaRepository.save(reserva);
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
        return reservaRepository.findByEstado(estado).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // UPDATE - Actualizar reserva
    public ReservaDTO actualizarReserva(Integer id, ReservaDTO reservaDTO) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));

        if (reservaDTO.getFechaClase() != null) {
            reserva.setFechaClase(reservaDTO.getFechaClase());
        }

        if (reservaDTO.getEstado() != null) {
            reserva.setEstado(reservaDTO.getEstado());
        }

        Reserva reservaActualizada = reservaRepository.save(reserva);
        return convertirADTO(reservaActualizada);
    }

    // UPDATE - Cancelar reserva
    public ReservaDTO cancelarReserva(Integer id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));

        if (!"CONFIRMADA".equals(reserva.getEstado())) {
            throw new RuntimeException("Solo se pueden cancelar reservas confirmadas");
        }

        reserva.setEstado("CANCELADA");
        reserva.setFechaCancelacion(LocalDateTime.now());

        Reserva reservaActualizada = reservaRepository.save(reserva);
        return convertirADTO(reservaActualizada);
    }

    // DELETE
    public void eliminarReserva(Integer id) {
        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException("Reserva no encontrada con id: " + id);
        }
        reservaRepository.deleteById(id);
    }

    // Metodo auxiliar para convertir entidad a DTO
    private ReservaDTO convertirADTO(Reserva reserva) {
        return ReservaDTO.builder()
                .id(reserva.getId())
                .socioId(reserva.getSocio().getId())
                .socioNombre(reserva.getSocio().getNombre())
                .claseId(reserva.getClase().getId())
                .claseNombre(reserva.getClase().getNombre())
                .fechaReserva(reserva.getFechaReserva())
                .fechaClase(reserva.getFechaClase())
                .estado(reserva.getEstado())
                .fechaCancelacion(reserva.getFechaCancelacion())
                .build();
    }
}
