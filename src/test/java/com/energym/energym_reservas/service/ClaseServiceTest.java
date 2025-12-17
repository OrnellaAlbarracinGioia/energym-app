package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.request.ClaseScheduleRequestDTO;
import com.energym.energym_reservas.dto.request.ClasesBatchCreateRequestDTO;
import com.energym.energym_reservas.dto.response.ClaseResponseDTO;
import com.energym.energym_reservas.entity.*;
import com.energym.energym_reservas.exception.BusinessRuleException;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.mapper.ClaseMapperImpl;
import com.energym.energym_reservas.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaseServiceTest {

    @Mock
    ClaseRepository claseRepository;

    @Mock
    ActividadRepository  actividadRepository;

    @Mock
    EntrenadorRepository entrenadorRepository;

    @Mock
    ReservaRepository reservaRepository;

    @Mock
    SucursalRepository sucursalRepository;

    @Spy
    ClaseMapperImpl claseMapper = new ClaseMapperImpl();

    @InjectMocks
    ClaseService claseService;

    @Test
    @DisplayName("Crear Clase(s) en Sistema (OK)")
    void crearClasesExitoso() {
        Integer idEntrenador1 = 1;
        Integer idEntrenador2 = 2;

        Entrenador entrenador1 = crearEntrenador(idEntrenador1, "Julian", "1133665544");
        Entrenador entrenador2 = crearEntrenador(idEntrenador2, "Florencia", "1188997744");

        Actividad actividad = crearActividad(1, "Pilates");
        Sucursal sucursal = crearSucursal(1, "Centro Energym");

        List<ClaseScheduleRequestDTO> datosClases = Arrays.asList(
                crearDatosClase(idEntrenador1, 10, LocalDate.now(), LocalTime.now()),
                crearDatosClase(idEntrenador2, 20, LocalDate.now().plusDays(1), LocalTime.of(9,30))
        );

        ClasesBatchCreateRequestDTO request = crearClasesRequestDTO(actividad.getId(), sucursal.getId(), datosClases);

        when(actividadRepository.findById(request.getActividadId())).thenReturn(Optional.of(actividad));
        when(sucursalRepository.findById(request.getSucursalId())).thenReturn(Optional.of(sucursal));

        when(entrenadorRepository.findById(idEntrenador1)).thenReturn(Optional.of(entrenador1));
        when(entrenadorRepository.findById(idEntrenador2)).thenReturn(Optional.of(entrenador2));

        when(claseRepository.saveAll(anyList() )).thenAnswer(i -> i.getArgument(0));
        List<ClaseResponseDTO> resultado = claseService.crearClases(request);

        assertNotNull(resultado);
        assertEquals(datosClases.size(), resultado.size());
        assertEquals(actividad.getNombre(), resultado.getFirst().getActividadNombre());
        assertEquals(sucursal.getNombre(), resultado.getFirst().getSucursalNombre());
        assertEquals(entrenador1.getNombre(), resultado.getFirst().getEntrenadorNombre());

        verify(actividadRepository).findById(request.getActividadId());
        verify(sucursalRepository).findById(request.getSucursalId());
        verify(entrenadorRepository).findById(idEntrenador1);
        verify(entrenadorRepository).findById(idEntrenador2);
        verify(claseRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("Crear Clase(s) en Sistema (FALLA) - Actividad Inexistente")
    void crearClasesFallaActividad() {
        ClaseScheduleRequestDTO datos = crearDatosClase(1, 10, LocalDate.now(), LocalTime.now());
        ClasesBatchCreateRequestDTO request = crearClasesRequestDTO(1, 1, Arrays.asList(datos));

        Integer actividadId = request.getActividadId();

        when(actividadRepository.findById(actividadId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> claseService.crearClases(request));

        verify(actividadRepository).findById(actividadId);
        verify(sucursalRepository, never()).findById(any());
        verify(entrenadorRepository, never()).findById(any());
        verify(claseRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("Crear Clase(s) en Sistema (FALLA) - Sucursal Inexistente")
    void crearClasesFallaSucursal() {
        Actividad actividad = crearActividad(2, "Yoga");
        ClaseScheduleRequestDTO datos = crearDatosClase(1, 10, LocalDate.now(), LocalTime.now());
        ClasesBatchCreateRequestDTO request = crearClasesRequestDTO(actividad.getId(), 1, Arrays.asList(datos));

        Integer sucursalId = request.getSucursalId();

        when(actividadRepository.findById(request.getActividadId())).thenReturn(Optional.of(actividad));
        when(sucursalRepository.findById(sucursalId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> claseService.crearClases(request));

        verify(actividadRepository).findById(request.getActividadId());
        verify(sucursalRepository).findById(request.getSucursalId());
        verify(entrenadorRepository, never()).findById(any());
        verify(claseRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("Crear Clase(s) en Sistema (FALLA) - Entrenador Inexistente")
    void crearClasesFallaEntrenador() {
        Integer entrenadorId = 99;
        ClaseScheduleRequestDTO datos = crearDatosClase(entrenadorId, 10, LocalDate.now(), LocalTime.now());
        ClasesBatchCreateRequestDTO request = crearClasesRequestDTO(1, 1, Arrays.asList(datos));

        when(actividadRepository.findById(request.getActividadId())).thenReturn(Optional.of(new Actividad()));
        when(sucursalRepository.findById(request.getSucursalId())).thenReturn(Optional.of(new Sucursal()));

        when(entrenadorRepository.findById(entrenadorId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> claseService.crearClases(request));

        verify(actividadRepository).findById(request.getActividadId());
        verify(sucursalRepository).findById(request.getSucursalId());
        verify(entrenadorRepository).findById(entrenadorId);
        verify(claseRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("Obtener todas las Clases (OK)")
    void obtenerTodasLasClases() {
        Clase clase1 = new Clase();
        clase1.setId(1);

        Clase clase2 = new Clase();
        clase2.setId(2);

        List<Clase> clases = Arrays.asList(clase1, clase2);

        when(claseRepository.findAll()).thenReturn(clases);

        List<ClaseResponseDTO> resultado = claseService.obtenerClases();

        assertNotNull(resultado);
        assertEquals(clases.size(), resultado.size());

        verify(claseRepository).findAll();
    }

    @Test
    @DisplayName("Obtener todas las Clases (Vacío)")
    void obtenerTodasLasClasesVacio() {
        when(claseRepository.findAll()).thenReturn(Collections.emptyList());

        List<ClaseResponseDTO> resultado = claseService.obtenerClases();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(claseRepository).findAll();
    }

    @Test
    @DisplayName("Obtener Clase por ID (OK)")
    void obtenerClasePorId() {
        Actividad actividad = crearActividad(1, "Funcional");
        Sucursal sucursal = crearSucursal(2, "Central Energym");
        Entrenador entrenador = crearEntrenador (1, "Julian", "1133665544");

        Integer id = 1;
        Clase clase = new Clase();
        clase.setId(id);
        clase.setActividad(actividad);
        clase.setSucursal(sucursal);
        clase.setEntrenador(entrenador);

        when(claseRepository.findById(id)).thenReturn(Optional.of(clase));

        ClaseResponseDTO resultado = claseService.obtenerClasePorId(id);

        assertNotNull(resultado);
        assertEquals(clase.getId(), resultado.getId());
        assertEquals(clase.getActividad().getNombre(), resultado.getActividadNombre());
        assertEquals(clase.getSucursal().getNombre(), resultado.getSucursalNombre());
        assertEquals(clase.getEntrenador().getNombre(), resultado.getEntrenadorNombre());

        verify(claseRepository).findById(id);
    }

    @Test
    @DisplayName("Obtener Clase por ID (NO Encuentra)")
    void obtenerClasePorIdInexistente() {
        Integer id = 99;

        when(claseRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> claseService.obtenerClasePorId(id));

        verify(claseRepository).findById(id);
        verify(claseMapper, never()).toResponseDTO(any(Clase.class));
    }

    @Test
    @DisplayName("Eliminar Clase según ID sin Reservas asociadas (OK)")
    void eliminarClasePorId() {
        Integer id = 1;

        when(claseRepository.existsById(id)).thenReturn(true);
        when(reservaRepository.existsByClaseId(id)).thenReturn(false);

        claseService.eliminarClase(id);

        verify(claseRepository).existsById(id);
        verify(reservaRepository).existsByClaseId(id);
        verify(claseRepository).deleteById(id);
    }

    @Test
    @DisplayName("Eliminar Clase según ID Inexistente")
    void eliminarClaseConIdInexistente() {
        Integer id = 99;
        when(claseRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> claseService.eliminarClase(id));

        verify(claseRepository).existsById(id);
        verify(reservaRepository, never()).existsByClaseId(any());
        verify(claseRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Eliminar Clase según ID con Reservas asociadas (FALLA)")
    void eliminarClaseConReservasAsociadas() {
        Integer id = 1;

        when(claseRepository.existsById(id)).thenReturn(true);
        when(reservaRepository.existsByClaseId(id)).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> claseService.eliminarClase(id));

        verify(claseRepository).existsById(id);
        verify(reservaRepository).existsByClaseId(id);
        verify(claseRepository, never()).deleteById(any());
    }

    private ClaseScheduleRequestDTO crearDatosClase(Integer entrenadorId, Integer capMax, LocalDate fecha, LocalTime horario) {
        ClaseScheduleRequestDTO datosClase = new ClaseScheduleRequestDTO();
        datosClase.setEntrenadorId(entrenadorId);
        datosClase.setCapacidadMaxima(capMax);
        datosClase.setFecha(fecha);
        datosClase.setHorario(horario);

        return datosClase;
    }

    private ClasesBatchCreateRequestDTO crearClasesRequestDTO(Integer idActividad, Integer idSucursal, List<ClaseScheduleRequestDTO> datosClases) {

        ClasesBatchCreateRequestDTO clase = new ClasesBatchCreateRequestDTO();

        clase.setActividadId(idActividad);
        clase.setSucursalId(idSucursal);
        clase.setClases(datosClases);

        return clase;
    }

    private Entrenador crearEntrenador(Integer id, String nombre, String contacto){
        return new Entrenador(id, nombre, contacto, new User());
    }

    private Actividad crearActividad(Integer id, String nombre){
        return new Actividad(id, nombre, null, 40);
    }

    private Sucursal crearSucursal(Integer id, String nombre){
        return new Sucursal(id, nombre, "Av. Corrientes 123", null);
    }
}