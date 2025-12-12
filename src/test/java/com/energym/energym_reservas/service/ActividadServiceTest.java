package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.request.ActividadRequestDTO;
import com.energym.energym_reservas.dto.response.ActividadResponseDTO;
import com.energym.energym_reservas.entity.Actividad;
import com.energym.energym_reservas.exception.BusinessRuleException;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.mapper.ActividadMapperImpl;
import com.energym.energym_reservas.repository.ActividadRepository;
import com.energym.energym_reservas.repository.ClaseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActividadServiceTest {

    @Mock
    ActividadRepository actividadRepository;

    @Mock
    ClaseRepository claseRepository;

    @Spy
    ActividadMapperImpl actividadMapper = new ActividadMapperImpl();

    @InjectMocks
    ActividadService actividadService;

    @Test
    @DisplayName("Crear una Actividad en Sistema (OK)")
    void crearActividadExitoso(){
        ActividadRequestDTO actividad = new ActividadRequestDTO("Yoga", null, 50);

        when(actividadRepository.findByNombreIgnoreCase(actividad.getNombre())).thenReturn(Optional.empty());
        when(actividadRepository.save(any(Actividad.class))).thenAnswer(i -> {
           Actividad a = i.getArgument(0);
           a.setId(1);
           return a;
        });

        ActividadResponseDTO resultado  = actividadService.crearActividad(actividad);

        assertNotNull(resultado);
        assertEquals(actividad.getNombre(), resultado.getNombre());

        verify(actividadRepository).findByNombreIgnoreCase(actividad.getNombre());
        verify(actividadRepository).save(any(Actividad.class));
    }

    @Test
    @DisplayName("Crear una Actividad en Sistema (FALLA)")
    void crearActividadFalla(){
        Actividad existente = crearActividad(1,"yoga", null, 20);
        ActividadRequestDTO nueva = new ActividadRequestDTO("Yoga", null, 50);

        when(actividadRepository.findByNombreIgnoreCase(nueva.getNombre())).thenReturn(Optional.of(existente));

        assertThrows(BusinessRuleException.class, () -> actividadService.crearActividad(nueva));

        verify(actividadRepository).findByNombreIgnoreCase(nueva.getNombre());
        verify(actividadRepository, never()).save(any(Actividad.class));
    }

    @Test
    @DisplayName("Obtener todas las Actividades (OK)")
    void obtenerTodasLasActividades() {
        Actividad actividad1 = crearActividad(1, "Yoga", null, 40);
        Actividad actividad2 = crearActividad(2, "Funcional", null, 50);

        List<Actividad> actividades = Arrays.asList(actividad1, actividad2);

        when(actividadRepository.findAll()).thenReturn(actividades);

        List<ActividadResponseDTO> resultado = actividadService.obtenerTodasActividades();

        assertNotNull(resultado);
        assertEquals(actividades.size(), resultado.size());
        assertEquals(actividades.getFirst().getId(),resultado.getFirst().getId());
        assertEquals(actividades.getFirst().getNombre(),resultado.getFirst().getNombre());
        assertEquals(actividades.getFirst().getDescripcion(),resultado.getFirst().getDescripcion());

        verify(actividadRepository).findAll();
    }

    @Test
    @DisplayName("Obtener todas las Actividades (Vacío)")
    void obtenerTodasLasActividadesVacio(){

        when(actividadRepository.findAll()).thenReturn(Collections.emptyList());

        List<ActividadResponseDTO> resultado = actividadService.obtenerTodasActividades();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(actividadRepository).findAll();
    }

    @Test
    @DisplayName("Obtener Actividad por ID (OK)")
    void obtenerActividadPorId(){
        Integer id = 1;
        Actividad actividad = crearActividad(id, "Yoga", null, 40);

        when(actividadRepository.findById(id)).thenReturn(Optional.of(actividad));

        ActividadResponseDTO resultado = actividadService.obtenerActividadPorId(id);

        assertNotNull(resultado);
        assertEquals(actividad.getId(), resultado.getId());
        assertEquals(actividad.getNombre(), resultado.getNombre());

        verify(actividadRepository).findById(id);
    }

    @Test
    @DisplayName("Obtener Actividad por ID (NO encuentra)")
    void obtenerActividadPorIdInexistente(){
        Integer id = 99;

        when(actividadRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> actividadService.obtenerActividadPorId(id));

        verify(actividadRepository).findById(id);
        verify(actividadMapper, never()).toResponseDTO(any(Actividad.class));
    }

    @Test
    @DisplayName("Actualizar actividad existente (OK)")
    void actualizarActividadExistente(){
        Integer id = 1;
        Actividad actividad = crearActividad(id, "Yoga", null, 40);
        String nombreAnterior = "Yoga";

        ActividadRequestDTO actividadActualizada = new ActividadRequestDTO("Yoga Relax", "Relajante", 40);

        when(actividadRepository.findById(id)).thenReturn(Optional.of(actividad));
        when(actividadRepository.findByNombreIgnoreCase(actividadActualizada.getNombre())).thenReturn(Optional.empty());
        when(actividadRepository.save(any(Actividad.class))).thenAnswer(a -> a.getArgument(0));

        ActividadResponseDTO resultado = actividadService.actualizarActividad(id,actividadActualizada);

        assertNotNull(resultado);
        assertEquals(actividad.getId(), resultado.getId());
        assertNotEquals(nombreAnterior, resultado.getNombre());
        assertNotEquals(null, resultado.getDescripcion());
        assertEquals(actividad.getDuracionMinutos(), resultado.getDuracionMinutos());

        verify(actividadRepository).findById(id);
        verify(actividadRepository).findByNombreIgnoreCase(actividad.getNombre());
        verify(actividadRepository).save(any(Actividad.class));
        verify(actividadMapper).updateFromRequest(actividadActualizada, actividad);
    }

    @Test
    @DisplayName("Actualizar actividad manteniendo el mismo Nombre (NO busca duplicados)")
    void actualizarActividadExistenteMismoNombre(){
        Integer id = 1;
        Actividad actividad = crearActividad(id, "Yoga", null, 40);

        ActividadRequestDTO actividadActualizada = new ActividadRequestDTO("Yoga", "Práctica relajante", 40);

        when(actividadRepository.findById(id)).thenReturn(Optional.of(actividad));
        when(actividadRepository.save(any(Actividad.class))).thenAnswer(a -> a.getArgument(0));

        ActividadResponseDTO resultado = actividadService.actualizarActividad(id,actividadActualizada);

        assertNotNull(resultado);
        assertEquals(actividad.getId(), resultado.getId());
        assertEquals(actividad.getNombre(), resultado.getNombre());
        assertNotEquals(null, resultado.getDescripcion());

        verify(actividadRepository).findById(id);
        verify(actividadRepository, never()).findByNombreIgnoreCase(any(String.class));
        verify(actividadRepository).save(any(Actividad.class));
        verify(actividadMapper).updateFromRequest(actividadActualizada, actividad);
    }

    @Test
    @DisplayName("Actualizar actividad con ID no existente (FALLA)")
    void actualizarActividadInexistente(){
        Integer id = 99;
        ActividadRequestDTO actividadActualizada = new ActividadRequestDTO("Yoga", "Práctica relajante", 40);
        when(actividadRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> actividadService.actualizarActividad(id, actividadActualizada));

        verify(actividadRepository).findById(id);
        verify(actividadRepository, never()).findByNombreIgnoreCase(any(String.class));
        verify(actividadRepository, never()).save(any(Actividad.class));
        verify(actividadMapper, never()).updateFromRequest(any(), any());
    }

    @Test
    @DisplayName("Actualizar actividad existente con Nombre ya utilizado (FALLA)")
    void actualizarActividadExistenteConNombreEnUso(){
        Actividad actividad1 = crearActividad(1, "Yoga", null, 40);
        Actividad actividad2 = crearActividad(2, "Yoga Fitness", null, 50);

        Integer id = 1;
        ActividadRequestDTO actividadActualizada = new ActividadRequestDTO("yoga fitness", null, 40);

        when(actividadRepository.findById(id)).thenReturn(Optional.of(actividad1));
        when(actividadRepository.findByNombreIgnoreCase(actividadActualizada.getNombre())).thenReturn(Optional.of(actividad2));

        assertThrows(BusinessRuleException.class, () -> actividadService.actualizarActividad(id,actividadActualizada));

        verify(actividadRepository).findById(id);
        verify(actividadRepository).findByNombreIgnoreCase(actividadActualizada.getNombre());
        verify(actividadRepository, never()).save(any(Actividad.class));
        verify(actividadMapper, never()).updateFromRequest(any(), any());
    }

    @Test
    @DisplayName("Eliminar Actividad según ID (OK)")
    void eliminarActividadPorId(){
        Integer id = 1;

        when(actividadRepository.existsById(id)).thenReturn(true);
        when(claseRepository.existsByActividadId(id)).thenReturn(false);

        actividadService.eliminarActividad(id);

        verify(actividadRepository).existsById(id);
        verify(claseRepository).existsByActividadId(id);
        verify(actividadRepository).deleteById(id);
    }

    @Test
    @DisplayName("Eliminar Actividad según ID Inexistente (FALLA)")
    void  eliminarActividadConIdInexistente(){
        Integer id = 99;

        when(actividadRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> actividadService.eliminarActividad(id));

        verify(actividadRepository).existsById(id);
        verify(claseRepository, never()).existsByActividadId(id);
        verify(actividadRepository, never()).deleteById(id);
    }

    @Test
    @DisplayName("Eliminar Actividad según ID con Clases asociadas (FALLA)")
    void eliminarActividadConClasesAsociadas() {
        Integer id = 1;

        when(actividadRepository.existsById(id)).thenReturn(true);
        when(claseRepository.existsByActividadId(id)).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> actividadService.eliminarActividad(id));

        verify(actividadRepository).existsById(id);
        verify(claseRepository).existsByActividadId(id);
        verify(actividadRepository, never()).deleteById(id);
    }

    private Actividad crearActividad(Integer id, String nombre, String descripcion, Integer duracionMinutos) {
        Actividad actividad = new Actividad();
        actividad.setId(id);
        actividad.setNombre(nombre);
        actividad.setDescripcion(descripcion);
        actividad.setDuracionMinutos(duracionMinutos);

        return actividad;
    }
}