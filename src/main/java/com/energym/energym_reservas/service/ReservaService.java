package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.ReservaDTO;
import com.energym.energym_reservas.entity.Clase;
import com.energym.energym_reservas.entity.Estado;
import com.energym.energym_reservas.entity.Reserva;
import com.energym.energym_reservas.entity.Socio;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.repository.ClaseRepository;
import com.energym.energym_reservas.repository.ReservaRepository;
import com.energym.energym_reservas.repository.SocioRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
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

        Integer cuposDisponibles = verificarDisponibilidad(clase.getId(), Estado.CONFIRMADA);

        if(cuposDisponibles <= 0){
            throw new IllegalStateException("La clase '" + clase.getActividad().getNombre() + "' del " + clase.getFecha() +
                    " a las " + clase.getHorario() + " está llena. ");
        }

        Reserva reserva = Reserva.builder()
                .socio(socio)
                .clase(clase)
                .build();

        return convertirADTO(reservaRepository.save(reserva));
    }


    @Transactional(readOnly = true)
    public Integer verificarDisponibilidad(Integer claseId, Estado estado) {
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new ResourceNotFoundException("Clase no encontrada"));

        Integer reservasConfirmadas = reservaRepository
                .countByClaseIdAndEstado(claseId, estado);

        Integer capacidadMaxima = clase.getCapacidadMaxima();

        return capacidadMaxima - reservasConfirmadas;
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
                .fecha(reserva.getFecha())
                .estado(reserva.getEstado().toString())
                .fechaCancelacion(reserva.getFechaCancelacion())
                .build();
    }
}
