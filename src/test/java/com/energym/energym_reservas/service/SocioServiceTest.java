package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.request.SocioRequestDTO;
import com.energym.energym_reservas.dto.response.ReservaResponseDTO;
import com.energym.energym_reservas.dto.response.SocioResponseDTO;
import com.energym.energym_reservas.entity.*;
import com.energym.energym_reservas.exception.BusinessRuleException;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.mapper.ReservaMapper;
import com.energym.energym_reservas.mapper.ReservaMapperImpl;
import com.energym.energym_reservas.mapper.SocioMapper;
import com.energym.energym_reservas.mapper.SocioMapperImpl;
import com.energym.energym_reservas.repository.ReservaRepository;
import com.energym.energym_reservas.repository.SocioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SocioServiceTest {

    @Mock
    SocioRepository socioRepository;

    @Mock
    ReservaRepository reservaRepository;

    @Spy
    SocioMapper socioMapper = new SocioMapperImpl();

    @Spy
    ReservaMapper reservaMapper = new ReservaMapperImpl();

    @InjectMocks
    SocioService socioService;

    @Test
    @DisplayName("Obtener todos los Socios (Existentes)")
    void obtenerTodosLosSocios() {
        Socio socio1 = crearSocio(1,"Miguel", "1122334455");
        Socio socio2 = crearSocio(2, "Florencia", "1122334466");

        List<Socio> sociosSimulado = Arrays.asList(socio1,socio2);

        when(socioRepository.findAll()).thenReturn(sociosSimulado);

        List<SocioResponseDTO> resultado = socioService.obtenerTodosLosSocios();

        assertNotNull(resultado);
        assertEquals(resultado.size(), sociosSimulado.size(), "Deberían existir 2 socios");
        assertEquals(resultado.getFirst().getId(), sociosSimulado.getFirst().getId());
        assertEquals(resultado.getFirst().getNombre(), sociosSimulado.getFirst().getNombre());
        assertEquals(resultado.getFirst().getTelefono(), sociosSimulado.getFirst().getTelefono());

        verify(socioRepository).findAll();
    }

    @Test
    @DisplayName("Obtener todos los Socios (Vacío)")
    void obtenerTodosLosSociosVacio() {
        when(socioRepository.findAll()).thenReturn(Collections.emptyList());

        List<SocioResponseDTO> resultado = socioService.obtenerTodosLosSocios();

        assertNotNull(resultado, "La lista nunca debe ser NULL");
        assertTrue(resultado.isEmpty(), "La lista debe estar vacía");

        verify(socioRepository).findAll();
    }

    @Test
    @DisplayName("Obtener Socio por ID (Existente)")
    void obtenerSocioPorId() {
        Socio socio = crearSocio(3,"Ramon", "1188997766");

        when(socioRepository.findById(3)).thenReturn(Optional.of(socio));

        SocioResponseDTO socioResultado = socioService.obtenerSocioPorId(3);

        assertNotNull(socioResultado);
        assertEquals(socio.getId(), socioResultado.getId());
        assertEquals(socio.getNombre(), socioResultado.getNombre());
        assertEquals(socio.getTelefono(), socioResultado.getTelefono());
        assertEquals(socio.getFechaRegistro(), socioResultado.getFechaRegistro());

        verify(socioRepository).findById(3);
    }

    @Test
    @DisplayName("Obtener socio por ID (No existe)")
    void obtenerSocioPorIdInexistente() {
        Integer id = 99;
        when(socioRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> socioService.obtenerSocioPorId(id), "Si un ID no existe en la BD se debería arrojar la Exception");

        verify(socioRepository).findById(id);
        verify(socioMapper, never()).toResponseDTO(any());
    }

    @Test
    @DisplayName("Actualizar Socio correctamente")
    void actualizarSocio() {
        Integer id = 1;
        Socio socio = crearSocio( id,"Miguel", "1122334455");
        String telefonoViejo = socio.getTelefono();

        SocioRequestDTO socioActualizado = new SocioRequestDTO("Miguel", "1122334444");

        when(socioRepository.findById(id)).thenReturn(Optional.of(socio));
        when(socioRepository.save(any(Socio.class))).thenAnswer(s -> s.getArgument(0));

        SocioResponseDTO resultado = socioService.actualizarSocio(id, socioActualizado);

        assertNotNull(resultado, "El resultado no deberia ser nulo");
        assertEquals(socio.getId(), resultado.getId(), "El ID no debería haberse modificado");
        assertNotEquals(telefonoViejo, resultado.getTelefono(), "El teléfono se debería haber actualizado");

        verify(socioRepository).findById(id);
        verify(socioMapper).updateFromRequest(socioActualizado, socio);
        verify(socioRepository).save(any(Socio.class));
    }

    @Test
    @DisplayName("No actualizar Socio inexistente")
    void noActualizarSocioInexistente() {
        Integer idInvalido = 99;

        when(socioRepository.findById(idInvalido)).thenReturn(Optional.empty());

        SocioRequestDTO socioActualizado = new SocioRequestDTO("Miguel", "1122334444");

        assertThrows(ResourceNotFoundException.class, () -> socioService.actualizarSocio(idInvalido, socioActualizado));

        verify(socioRepository).findById(idInvalido);
        verify(socioMapper, never()).updateFromRequest(any(), any());
        verify(socioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Eliminar (Desactivar) Socio con ID Existente")
    void eliminarSocioPorId(){
        Integer id = 10;
        Socio socio = crearSocio( id,"Fernando", "1185634712");

        when(socioRepository.findById(id)).thenReturn(Optional.of(socio));

        socioService.eliminarSocio(id);

        assertNotNull(socio);
        assertFalse(socio.getActivo(), "El socio debe quedar inactivo (False)");

        verify(socioRepository).findById(id);
        verify(socioRepository).save(socio);
    }

    @Test
    @DisplayName("Intentar eliminar (Desactivar) Socio con ID Inexistente")
    void eliminarSocioPorIdInexistente(){
        Integer id = 99;
        when(socioRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> socioService.eliminarSocio(id));

        verify(socioRepository).findById(id);
        verify(socioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Eliminar permanentemente Socio con ID Existente sin Reservas")
    void eliminarPermanenteSocioPorIdSinReserva(){
        Integer id = 10;

        when(socioRepository.existsById(id)).thenReturn(true);
        when(reservaRepository.existsBySocioId(id)).thenReturn(false);

        socioService.eliminarSocioPermanente(id);

        verify(socioRepository).existsById(id);
        verify(reservaRepository).existsBySocioId(id);
        verify(socioRepository).deleteById(id);
    }

    @Test
    @DisplayName("Intentar eliminar permanentemente Socio con ID Existente con Reservas")
    void eliminarPermanenteSocioPorIdExistenteConReserva(){
        Integer id = 10;

        when(socioRepository.existsById(id)).thenReturn(true);
        when(reservaRepository.existsBySocioId(id)).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> socioService.eliminarSocioPermanente(id));

        verify(socioRepository).existsById(id);
        verify(reservaRepository).existsBySocioId(id);
        verify(socioRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Intentar eliminar permanentemente Socio con ID Inexistente")
    void eliminarPermanenteSocioPorIdInexistente(){
        Integer id = 99;

        when(socioRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> socioService.eliminarSocioPermanente(id));

        verify(socioRepository).existsById(id);
        verify(reservaRepository, never()).existsBySocioId(any());
        verify(socioRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Adicionar una Clase Personalizada a Socio según ID Existente")
    void aplicarPersonalizadaGratuitaASocioExistente() {
        Integer id = 4;
        Socio socio = crearSocio( id,"Morena", "1198736602");

        Integer cantidadInicial = socio.getClasesPersonalizadas();

        when(socioRepository.findById(id)).thenReturn(Optional.of(socio));

        socioService.beneficioClasePersonalizadaGratuita(id);

        assertNotNull(socio);
        assertTrue(cantidadInicial < socio.getClasesPersonalizadas(), "La cantidad de clases se debe haber actualizado e incrementado su valor");
        assertEquals(1, socio.getClasesPersonalizadas(), "La cantidad de Clases debe haber incrementado en 1 su valor");
        verify(socioRepository).findById(id);
        verify(socioRepository).save(socio);
    }

    @Test
    @DisplayName("Adicionar Clase Personalizada a Socio con ID Inexistente")
    void aplicarPersonalizadaGratuitaASocioInexistente() {
        Integer id = 4;
        when(socioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> socioService.beneficioClasePersonalizadaGratuita(id));

        verify(socioRepository).findById(id);
        verify(socioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Solicitar el Historial de Asistencia de Socio con ID Existente")
    void solicitarHistorialDeAsistenciaPorSocioId() {
        Integer id = 4;
        Socio socio = crearSocio( id,"Morena", "1198736602");

        List<Reserva> reservas = crearReservasCompletadas(socio);

        when(socioRepository.existsById(id)).thenReturn(true);
        when(reservaRepository.findBySocioIdAndEstado(id, Estado.COMPLETADA)).thenReturn(reservas);

        List<ReservaResponseDTO> resultado = socioService.obtenerHistorialAsistencia(id);

        assertNotNull(resultado);
        assertEquals(reservas.size(), resultado.size());
        assertEquals(socio.getNombre(), resultado.getFirst().getSocioNombre());

        verify(socioRepository).existsById(id);
        verify(reservaRepository).findBySocioIdAndEstado(id, Estado.COMPLETADA);
        verify(reservaMapper).toResponseList(reservas);
    }

    @Test
    @DisplayName("Solicitar el Historial Vacío de Asistencia de Socio con ID Existente")
    void solicitarHistorialDeAsistenciaVacioPorSocioId() {
        Integer id = 4;

        when(socioRepository.existsById(id)).thenReturn(true);
        when(reservaRepository.findBySocioIdAndEstado(id, Estado.COMPLETADA)).thenReturn(Collections.emptyList());

        List<ReservaResponseDTO>  resultado = socioService.obtenerHistorialAsistencia(id);

        assertNotNull(resultado, "El resultado no deberías ser nulo");
        assertTrue(resultado.isEmpty(), "La lista de Reservas debería ser vacía");

        verify(socioRepository).existsById(id);
        verify(reservaRepository).findBySocioIdAndEstado(id, Estado.COMPLETADA);
        verify(reservaMapper).toResponseList(Collections.emptyList());
    }

    @Test
    @DisplayName("Intenta solicitar el Historial de Asistencia de Socio con ID Inexistente")
    void solicitarHistorialDeAsistenciaPorSocioIdInexistente() {
        Integer id = 99;

        when(socioRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> socioService.obtenerHistorialAsistencia(id));

        verify(socioRepository).existsById(id);
        verify(reservaRepository, never()).findBySocioIdAndEstado(any(), any());
        verify(reservaMapper, never()).toResponseList(any());
    }

    private List<Reserva> crearReservasCompletadas(Socio socio) {
        List<Reserva> reservas = new ArrayList<>();

        Actividad actividad = new Actividad();
        actividad.setId(1);
        actividad.setNombre("Yoga");

        Clase clase = new Clase();
        clase.setId(1);
        clase.setActividad(actividad);
        clase.setHorario(LocalTime.of(9, 30, 0, 0));

        Reserva reserva1 = new Reserva();
        reserva1.setId(1);
        reserva1.setSocio(socio);
        reserva1.setClase(clase);
        reserva1.setFechaCreacion(LocalDateTime.of(2025, 12, 9, 10, 15, 0));
        reserva1.setEstado(Estado.COMPLETADA);

        Reserva reserva2 = new Reserva();
        reserva2.setId(2);
        reserva2.setSocio(socio);
        reserva2.setClase(clase);
        reserva2.setFechaCreacion(LocalDateTime.now());
        reserva2.setEstado(Estado.COMPLETADA);

        reservas.add(reserva1);
        reservas.add(reserva2);

        return reservas;
    }

    private Socio crearSocio(Integer id, String nombre, String telefono) {
        Socio socio = new Socio();
        socio.setId(id);
        socio.setNombre(nombre);
        socio.setFechaRegistro(LocalDateTime.now());
        socio.setTelefono(telefono);
        socio.setActivo(true);
        socio.setClasesPersonalizadas(0);

        return socio;
    }
}