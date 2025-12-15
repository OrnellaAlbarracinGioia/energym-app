package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.request.ReservaRequestDTO;
import com.energym.energym_reservas.dto.response.ReservaResponseDTO;
import com.energym.energym_reservas.entity.*;
import com.energym.energym_reservas.event.ReservaCompletadaEvent;
import com.energym.energym_reservas.exception.BusinessRuleException;
import com.energym.energym_reservas.exception.CapacidadLlenaException;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.mapper.ReservaMapperImpl;
import com.energym.energym_reservas.repository.ClaseRepository;
import com.energym.energym_reservas.repository.ReservaRepository;
import com.energym.energym_reservas.repository.SocioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    ReservaRepository reservaRepository;

    @Mock
    SocioRepository socioRepository;

    @Mock
    ClaseRepository claseRepository;

    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    @Spy
    ReservaMapperImpl reservaMapper = new ReservaMapperImpl();

    @InjectMocks
    ReservaService reservaService;

    Actividad actividad;
    Integer claseId;
    Clase clase;
    Integer socioId;
    Socio socio;

    Reserva reserva;
    Reserva reserva2;

    @BeforeEach
    void setUp() {
        this.actividad = new Actividad(1, "Funcional", null, 40);
        this.clase = new Clase();
        this.claseId = 1;
        clase.setId(claseId);
        clase.setActividad(actividad);
        clase.setHorario(LocalTime.of(15,30));
        clase.setFecha(LocalDate.now().plusDays(1));
        clase.setCapacidadMaxima(5);

        this.socio = new Socio();
        this.socioId = 2;
        socio.setId(socioId);
        socio.setNombre("Fernando");
        socio.setTelefono("1144566355");
        socio.setUser(new User());

        this.reserva = new Reserva();
        reserva.setId(1);
        reserva.setClase(clase);
        reserva.setSocio(socio);
        reserva.setEstado(Estado.CONFIRMADA);
        reserva.setFechaCreacion(LocalDateTime.now());

        this.reserva2 = new Reserva();
        reserva2.setId(2);
        reserva2.setClase(clase);
        reserva2.setSocio(socio);
        reserva2.setEstado(Estado.COMPLETADA);
        reserva2.setFechaCreacion(LocalDateTime.now());

    }

    @Test
    @DisplayName("Generar una Reserva en Sistema (OK)")
    void crearReservaExitosa() {

        ReservaRequestDTO request = new ReservaRequestDTO(socioId, claseId);

        when(socioRepository.findById(request.getSocioId())).thenReturn(Optional.of(socio));
        when(claseRepository.findById(request.getClaseId())).thenReturn(Optional.of(clase));
        when(reservaRepository.existsBySocioIdAndClaseIdAndEstado(socio.getId(), clase.getId(), Estado.CONFIRMADA)).thenReturn(false);
        when(claseRepository.incrementarOcupados(request.getClaseId())).thenReturn(1);
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(r -> r.getArgument(0));

        ReservaResponseDTO resultado = reservaService.crearReserva(request);

        assertNotNull(resultado);
        assertEquals("CONFIRMADA", resultado.getEstado());
        assertEquals(socio.getNombre(), resultado.getSocioNombre());
        assertEquals(clase.getActividad().getNombre(), resultado.getActividadNombre());

        verify(claseRepository, times(2)).findById(anyInt());
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Generar una Reserva (FALLA) - Socio no encontrado")
    void crearReservaSocioNoEncontrado() {
        ReservaRequestDTO request = new ReservaRequestDTO(99, claseId);
        when(socioRepository.findById(request.getSocioId())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reservaService.crearReserva(request));

        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Generar una Reserva (FALLA) - Socio inactivo")
    void crearReservaSocioInactivo() {
        ReservaRequestDTO request = new ReservaRequestDTO(socioId, claseId);
        this.socio.setActivo(false);

        when(socioRepository.findById(request.getSocioId())).thenReturn(Optional.of(socio));

        assertThrows(BusinessRuleException.class, () -> reservaService.crearReserva(request));
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Generar una Reserva (FALLA) - Clase no encontrada")
    void crearReservaClaseNoEncontrada() {
        ReservaRequestDTO request = new ReservaRequestDTO(socioId, 99);
        when(socioRepository.findById(request.getSocioId())).thenReturn(Optional.of(socio));
        when(claseRepository.findById(request.getClaseId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservaService.crearReserva(request));

        verify(claseRepository, times(1)).findById(anyInt());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Generar una Reserva (FALLA) - Fecha de Clase concluida")
    void crearReservaFechaClaseConcluida() {
        ReservaRequestDTO request = new ReservaRequestDTO(socioId, claseId);
        this.clase.setFecha(LocalDate.now().minusDays(1));

        when(socioRepository.findById(request.getSocioId())).thenReturn(Optional.of(socio));
        when(claseRepository.findById(request.getClaseId())).thenReturn(Optional.of(clase));

        assertThrows(BusinessRuleException.class, () -> reservaService.crearReserva(request));

        verify(claseRepository, times(1)).findById(anyInt());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Generar una Reserva (FALLA) - Socio ya cuenta con una reserva para la Clase")
    void crearReservaSocioConReservaPrevia() {
        ReservaRequestDTO request = new ReservaRequestDTO(socioId, claseId);

        when(socioRepository.findById(request.getSocioId())).thenReturn(Optional.of(socio));
        when(claseRepository.findById(request.getClaseId())).thenReturn(Optional.of(clase));
        when(reservaRepository.existsBySocioIdAndClaseIdAndEstado(socio.getId(), clase.getId(), Estado.CONFIRMADA)).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> reservaService.crearReserva(request));

        verify(claseRepository, times(1)).findById(anyInt());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Generar una Reserva (FALLA) - No hay cupos disponibles")
    void crearReservaNoHayCuposDisponibles() {
        ReservaRequestDTO request = new ReservaRequestDTO(socioId, claseId);

        when(socioRepository.findById(request.getSocioId())).thenReturn(Optional.of(socio));
        when(claseRepository.findById(request.getClaseId())).thenReturn(Optional.of(clase));
        when(reservaRepository.existsBySocioIdAndClaseIdAndEstado(socio.getId(), clase.getId(), Estado.CONFIRMADA)).thenReturn(false);
        when(claseRepository.incrementarOcupados(request.getClaseId())).thenReturn(0);

        assertThrows(CapacidadLlenaException.class, () -> reservaService.crearReserva(request));

        verify(claseRepository, times(1)).findById(anyInt());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Obtener todas las Reservas (OK)")
    void obtenerTodasLasReservas() {

        when(reservaRepository.findAll()).thenReturn(List.of(reserva, reserva2));

        List<ReservaResponseDTO> resultado = reservaService.obtenerTodasLasReservas();

        assertNotNull(resultado);
        assertEquals(2,resultado.size());
        assertEquals(reserva.getSocio().getNombre(),resultado.getFirst().getSocioNombre());

        verify(reservaRepository).findAll();
    }

    @Test
    @DisplayName("Obtener todas las Reservas (Vacío)")
    void obtenerTodasLasReservasVacio() {
        when(reservaRepository.findAll()).thenReturn(Collections.emptyList());

        List<ReservaResponseDTO> resultado = reservaService.obtenerTodasLasReservas();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(reservaRepository).findAll();
    }

    @Test
    @DisplayName("Obtener Reserva por ID (OK)")
    void obtenerReservaPorID() {
        Integer id = 1;
        when(reservaRepository.findById(id)).thenReturn(Optional.of(reserva));

        ReservaResponseDTO resultado = reservaService.obtenerReservaPorId(id);

        assertNotNull(resultado);
        assertEquals(reserva.getSocio().getNombre(),resultado.getSocioNombre());
        assertEquals(reserva.getClase().getActividad().getNombre(),resultado.getActividadNombre());

        verify(reservaRepository).findById(anyInt());
    }

    @Test
    @DisplayName("Obtener Reserva por ID (NO Encuentra)")
    void obtenerReservaPorIdInexistente() {
        Integer id = 99;
        when(reservaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservaService.obtenerReservaPorId(id));

        verify(reservaMapper, never()).toResponseDTO(any(Reserva.class));
    }

    @Test
    @DisplayName("Obtener Reserva por Socio (OK)")
    void obtenerReservaPorSocio() {
        Integer socioId = 2;

        when(reservaRepository.findBySocioId(socioId)).thenReturn(Arrays.asList(reserva, reserva2));

        List<ReservaResponseDTO> resultado = reservaService.obtenerReservasPorSocio(socioId);

        assertNotNull(resultado);
        assertEquals(2,resultado.size());
        assertEquals(reserva.getSocio().getNombre(),resultado.getFirst().getSocioNombre());

        verify(reservaRepository).findBySocioId(socioId);
    }

    @Test
    @DisplayName("Obtener Reserva por Socio (Vacío)")
    void obtenerReservaPorSocioVacio() {
        Integer socioId = 2;
        when(reservaRepository.findBySocioId(socioId)).thenReturn(Collections.emptyList());

        List<ReservaResponseDTO> resultado = reservaService.obtenerReservasPorSocio(socioId);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(reservaRepository).findBySocioId(socioId);
    }

    @Test
    @DisplayName("Obtener Reserva por Clase (OK)")
    void obtenerReservaPorClase() {
        Integer claseId = 1;
        when(reservaRepository.findByClaseId(claseId)).thenReturn(Arrays.asList(reserva, reserva2));

        List<ReservaResponseDTO> resultado = reservaService.obtenerReservasPorClase(claseId);

        assertNotNull(resultado);
        assertEquals(2,resultado.size());
        assertEquals(reserva.getClase().getActividad().getNombre(),resultado.getFirst().getActividadNombre());
    }

    @Test
    @DisplayName("Obtener Reserva por Clase (Vacío)")
    void obtenerReservaPorClaseVacio() {
        Integer claseId = 1;
        when(reservaRepository.findByClaseId(claseId)).thenReturn(Collections.emptyList());

        List<ReservaResponseDTO> resultado = reservaService.obtenerReservasPorClase(claseId);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(reservaRepository).findByClaseId(claseId);
    }

    @Test
    @DisplayName("Obtener Reserva por Estado (OK)")
    void obtenerReservaPorEstado() {
        Estado estado = Estado.CONFIRMADA;
        when(reservaRepository.findByEstado(estado)).thenReturn(Arrays.asList(reserva));

        List<ReservaResponseDTO> resultado = reservaService.obtenerReservasPorEstado(estado);

        assertNotNull(resultado);
        assertEquals(1,resultado.size());

        verify(reservaRepository).findByEstado(estado);
    }

    @Test
    @DisplayName("Obtener Reserva por Estado (NO Encuentra)")
    void obtenerReservaPorEstadoNoEncuentra() {
        Estado estado = Estado.CANCELADA;
        when(reservaRepository.findByEstado(estado)).thenReturn(Collections.emptyList());

        List<ReservaResponseDTO>  resultado = reservaService.obtenerReservasPorEstado(estado);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(reservaRepository).findByEstado(estado);
    }

    @Test
    @DisplayName("Cancelar una Reserva (OK)")
    void cancelarReservaExitosa() {
        Integer reservaId = this.reserva.getId();

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));

        ReservaResponseDTO resultado = reservaService.cancelarReserva(reservaId);

        assertNotNull(resultado);
        assertEquals("CANCELADA", resultado.getEstado());
        assertNotNull(resultado.getFechaCancelacion());

        verify(reservaRepository).findById(reservaId);
        verify(reservaMapper).toResponseDTO(any(Reserva.class));
    }


    @Test
    @DisplayName("Cancelar una Reserva (OK) - Clase personalizada ")
    void cancelarReservaClasePersonalizada() {
        Integer reservaId = this.reserva.getId();
        this.reserva.getClase().setPersonalizada(true);
        this.socio.setClasesPersonalizadas(0);

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));

        ReservaResponseDTO resultado = reservaService.cancelarReserva(reservaId);

        assertNotNull(resultado);
        assertEquals(1, this.socio.getClasesPersonalizadas());
        assertEquals("CANCELADA", resultado.getEstado());
        assertNotNull(resultado.getFechaCancelacion());

        verify(reservaRepository).findById(reservaId);
        verify(reservaMapper).toResponseDTO(any(Reserva.class));
    }

    @Test
    @DisplayName("Cancelar una Reserva (FALLA) - ID Inexistente")
    void cancelarReservaFallo() {
        Integer reservaId = 99;
        when(reservaRepository.findById(reservaId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservaService.cancelarReserva(reservaId));

        verify(reservaRepository).findById(reservaId);
        verify(reservaMapper, never()).toResponseDTO(any(Reserva.class));
    }

    @Test
    @DisplayName("Cancelar una Reserva (FALLA) - Estado incorrecto")
    void cancelarReservaFalloEstado() {
        Integer reservaId = this.reserva2.getId();
        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva2));

        assertThrows(BusinessRuleException.class, () -> reservaService.cancelarReserva(reservaId));

        verify(reservaRepository).findById(reservaId);
        verify(reservaMapper, never()).toResponseDTO(any(Reserva.class));
    }

    @Test
    @DisplayName("Cancelar una Reserva (FALLA) - Fecha ya culminada")
    void cancelarReservaFechaCulminada() {
        Integer reservaId = this.reserva.getId();
        this.reserva.getClase().setFecha(LocalDate.now().minusDays(1));

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));
        assertThrows(BusinessRuleException.class, () -> reservaService.cancelarReserva(reservaId));

        verify(reservaRepository).findById(reservaId);
        verify(reservaMapper, never()).toResponseDTO(any(Reserva.class));
    }

    @Test
    @DisplayName("Marcar Asistencia (OK) - Cambia estado y dispara evento)")
    void marcarAsistenciaReservaComoCompletada() {
        Integer reservaId = 1;
        this.reserva.getClase().setFecha(LocalDate.now().minusDays(1));

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));

        ReservaResponseDTO resultado = reservaService.marcarAsistencia(reservaId);

        assertNotNull(resultado);
        assertEquals("COMPLETADA", resultado.getEstado());

        verify(applicationEventPublisher).publishEvent(any(ReservaCompletadaEvent.class));
    }

    @Test
    @DisplayName("Marcar Asistencia (FALLA) - Clase futura")
    void marcarAsistenciaReservaFallaClaseFutura() {
        Integer reservaId = 1;
        this.reserva.getClase().setFecha(LocalDate.now().plusDays(1));

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));

        Exception ex = assertThrows(BusinessRuleException.class, () -> reservaService.marcarAsistencia(reservaId));
        assertEquals("No se puede marcar asistencia antes de que la clase ocurra", ex.getMessage());
        assertEquals(Estado.CONFIRMADA, reserva.getEstado());

        verify(applicationEventPublisher, never()).publishEvent(any(ReservaCompletadaEvent.class));

    }

    @Test
    @DisplayName("Marcar Asistencia (FALLA) - Reserva no confirmada (cancelada o completada)")
    void  marcarAsistenciaReservaFallaNoConfirmada() {
        Integer reservaId = this.reserva2.getId();
        this.reserva2.getClase().setFecha(LocalDate.now().minusDays(1));
        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva2));

        Exception ex = assertThrows(BusinessRuleException.class, () -> reservaService.marcarAsistencia(reservaId));

        assertEquals("La reserva no se encuentra confirmada o ya fue utilizada", ex.getMessage());
        assertNotEquals(Estado.CONFIRMADA, reserva2.getEstado());

        verify(applicationEventPublisher, never()).publishEvent(any(ReservaCompletadaEvent.class));
    }

    @Test
    @DisplayName("Cantidad de Clases completadas - Es NULL Devuelve 0")
    void contarClasesCompletadasPorSocio() {
        Integer socioId = this.socio.getId();

        when(reservaRepository.countBySocioIdAndEstadoAndClaseFechaBetween(
            eq(socioId),
            eq(Estado.COMPLETADA),
            any(LocalDate.class),
            any(LocalDate.class)
        )).thenReturn(null);

        Integer resultado = reservaService.contarClasesCompletadasEnMes(socioId);

        assertNotNull(resultado);
        assertEquals( 0,  resultado, "Si el repositorio devuelve NULL el service debe retornar 0");
    }

}