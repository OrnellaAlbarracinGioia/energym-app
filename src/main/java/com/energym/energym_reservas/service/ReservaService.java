package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.request.ReservaRequestDTO;
import com.energym.energym_reservas.dto.response.ReservaResponseDTO;
import com.energym.energym_reservas.entity.Clase;
import com.energym.energym_reservas.entity.Estado;
import com.energym.energym_reservas.entity.Reserva;
import com.energym.energym_reservas.entity.Socio;
import com.energym.energym_reservas.event.ReservaCompletadaEvent;
import com.energym.energym_reservas.exception.BusinessRuleException;
import com.energym.energym_reservas.exception.CapacidadLlenaException;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.mapper.ReservaMapper;
import com.energym.energym_reservas.repository.ClaseRepository;
import com.energym.energym_reservas.repository.ReservaRepository;
import com.energym.energym_reservas.repository.SocioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final SocioRepository socioRepository;
    private final ClaseRepository claseRepository;
    private final ReservaMapper reservaMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    /*
     * Crear una nueva Reserva
     */
    public ReservaResponseDTO crearReserva(ReservaRequestDTO request) {

        Socio socio = socioRepository.findById(request.getSocioId())
                .orElseThrow(() -> new ResourceNotFoundException("Socio", "id", request.getSocioId()));

        if(!socio.getActivo()) {
            throw new BusinessRuleException("Socio no se encuentra activo");
        }

        Clase clase = claseRepository.findById(request.getClaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Clase", "id", request.getClaseId()));

        LocalDateTime inicioClase = LocalDateTime.of(clase.getFecha(), clase.getHorario());
        if (inicioClase.isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("No se puede reservar una clase que ya pasó");
        }

        boolean yaReservado = reservaRepository.existsBySocioIdAndClaseIdAndEstado(
                socio.getId(),
                clase.getId(),
                Estado.CONFIRMADA);

        if (yaReservado) {
            throw new BusinessRuleException("El socio ya tiene una reserva confirmada en esta clase");
        }

        int disponibilidadActualizada = claseRepository.incrementarOcupados(request.getClaseId());

        if(disponibilidadActualizada == 0) {
            throw new CapacidadLlenaException("No quedan cupos disponibles para la clase. ");
        }

        Clase claseCompleta = claseRepository.findById(request.getClaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Clase", "id", request.getClaseId()));

        Reserva reserva = Reserva.builder()
                .socio(socio)
                .clase(claseCompleta)
                .estado(Estado.CONFIRMADA)
                .build();

        Reserva savedReserva = reservaRepository.save(reserva);
        return reservaMapper.toResponseDTO(savedReserva);
    }

    /*
     * Obtener todas las reservas
     */
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> obtenerTodasLasReservas() {
        List<Reserva> reservas = reservaRepository.findAll();
        return reservaMapper.toResponseList(reservas);
    }

    /*
     * Obtener reserva por ID
     */
    @Transactional(readOnly = true)
    public ReservaResponseDTO obtenerReservaPorId(Integer id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "id",id));
        return reservaMapper.toResponseDTO(reserva);
    }

    /*
     * Obtener reservas por socio
     */
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> obtenerReservasPorSocio(Integer socioId) {
        List<Reserva> reservas = reservaRepository.findBySocioId(socioId);
        return  reservaMapper.toResponseList(reservas);
    }

    /*
     * Obtener reservas por clase
     */
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> obtenerReservasPorClase(Integer claseId) {
        List<Reserva> reservas = reservaRepository.findByClaseId(claseId);
        return reservaMapper.toResponseList(reservas);
    }

    /*
     * Obtener reservas por estado
     */
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> obtenerReservasPorEstado(Estado estado) {
        List<Reserva> reservas = reservaRepository.findByEstado(estado);
        return reservaMapper.toResponseList(reservas);
    }

    /*
     * Cancelar reserva
     */
    public ReservaResponseDTO cancelarReserva(Integer id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "id",id));

        if (!reserva.getEstado().equals(Estado.CONFIRMADA)) {
            throw new BusinessRuleException("Solo se pueden cancelar reservas confirmadas");
        }

        LocalDateTime inicioClase = LocalDateTime.of(reserva.getClase().getFecha(), reserva.getClase().getHorario());

        if(inicioClase.isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("No se pueden cancelar reservas que ya pasaron");
        }

        if (Boolean.TRUE.equals(reserva.getClase().getPersonalizada())) {

            Socio socio = reserva.getSocio();
            socio.setClasesPersonalizadas(socio.getClasesPersonalizadas() + 1);
        }

        reserva.setEstado(Estado.CANCELADA);
        reserva.setFechaCancelacion(LocalDateTime.now());

        return reservaMapper.toResponseDTO(reserva);
    }

    /*
     * Marcar una reserva como Asistida/Completada
     */
    public ReservaResponseDTO marcarAsistencia(Integer reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "id",reservaId));

        LocalDateTime inicioClase = LocalDateTime.of(reserva.getClase().getFecha(), reserva.getClase().getHorario());

        if(inicioClase.isAfter(LocalDateTime.now())) {
            throw new BusinessRuleException("No se puede marcar asistencia antes de que la clase ocurra");
        }

        if(!reserva.getEstado().equals(Estado.CONFIRMADA)) {
            throw new BusinessRuleException("La reserva no se encuentra confirmada o ya fue utilizada");
        }

        reserva.setEstado(Estado.COMPLETADA);

        ReservaCompletadaEvent event = new ReservaCompletadaEvent(this, reservaId);
        applicationEventPublisher.publishEvent(event);

        return reservaMapper.toResponseDTO(reserva);
    }

    /*
     * Contar cuantas clases completadas tiene un Socio en el Mes Actual
     */
    public Integer contarClasesCompletadasEnMes(Integer socioId) {
        // Obtener primer y último día del mes actual
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        LocalDate finMes = hoy.withDayOfMonth(hoy.lengthOfMonth());

        // Contar las clases COMPLETADAS en ese rango
        Integer clasesCompletadas = reservaRepository
                .countBySocioIdAndEstadoAndClaseFechaBetween(
                        socioId,
                        Estado.COMPLETADA,
                        inicioMes,
                        finMes
                );

        return clasesCompletadas != null ? clasesCompletadas : 0;
    }

    private Integer verificarDisponibilidad(Integer claseId) {
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new ResourceNotFoundException("Clase","id",claseId));
        return clase.getCapacidadMaxima() - clase.getCuposOcupados();
    }
}