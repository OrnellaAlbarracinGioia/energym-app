package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.request.EntrenadorRequestDTO;
import com.energym.energym_reservas.dto.response.EntrenadorResponseDTO;
import com.energym.energym_reservas.entity.Entrenador;
import com.energym.energym_reservas.exception.BusinessRuleException;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.mapper.EntrenadorMapperImpl;
import com.energym.energym_reservas.repository.ClaseRepository;
import com.energym.energym_reservas.repository.EntrenadorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntrenadorServiceTest {

    @Mock
    EntrenadorRepository entrenadorRepository;

    @Mock
    ClaseRepository claseRepository;

    @Spy
    EntrenadorMapperImpl entrenadorMapper = new EntrenadorMapperImpl();

    @InjectMocks
    EntrenadorService entrenadorService;


    @Test
    @DisplayName("Obtener todos los Entrenadores (OK)")
    void obtenerTodosLosEntrenadores() {
        Entrenador entrenador1 = crearEntrenador(1,"Julian", "1155662233");
        Entrenador entrenador2 = crearEntrenador(2, "Florencia", "1185632146");

        List<Entrenador> entrenadores = Arrays.asList(entrenador1, entrenador2);

        when(entrenadorRepository.findAll()).thenReturn(entrenadores);

        List<EntrenadorResponseDTO> resultado = entrenadorService.obtenerTodosLosEntrenadores();

        assertNotNull(resultado);
        assertEquals(entrenadores.size(), resultado.size());
        assertEquals(entrenador1.getId(), resultado.getFirst().getId());
        assertEquals(entrenador1.getNombre(), resultado.getFirst().getNombre());
        assertEquals(entrenador1.getContacto(), resultado.getFirst().getContacto());

        verify(entrenadorRepository).findAll();
    }

    @Test
    @DisplayName("Obtener todos los Entrenadores (VacÍo)")
    void obtenerTodosLosEntrenadoresVacio() {
        when(entrenadorRepository.findAll()).thenReturn(Collections.emptyList());

        List<EntrenadorResponseDTO> resultado = entrenadorService.obtenerTodosLosEntrenadores();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(entrenadorRepository).findAll();
    }

    @Test
    @DisplayName("Obtener Entrenador por ID (OK)")
    void obtenerEntrenadorPorId(){
        Integer id = 1;
        Entrenador entrenador = crearEntrenador(id,"Julian", "1155662233");

        when(entrenadorRepository.findById(id)).thenReturn(Optional.of(entrenador));

        EntrenadorResponseDTO resultado = entrenadorService.obtenerEntrenadorPorId(id);

        assertNotNull(resultado);
        assertEquals(entrenador.getId(), resultado.getId());
        assertEquals(entrenador.getNombre(), resultado.getNombre());
        assertEquals(entrenador.getContacto(), resultado.getContacto());

        verify(entrenadorRepository).findById(id);
    }

    @Test
    @DisplayName("Obtener Entrenador por ID (NO encuentra)")
    void obtenerEntrenadorPorIdInexistente(){
        Integer id = 99;
        when(entrenadorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> entrenadorService.obtenerEntrenadorPorId(id));
        verify(entrenadorRepository).findById(id);
        verify(entrenadorMapper, never()).toResponseDTO(any(Entrenador.class));
    }

    @Test
    @DisplayName("Busca entrenadores por Nombre (Encuentra)")
    void buscarEntrenadoresExistentesPorNombre(){
        Entrenador entrenador1 = crearEntrenador(1,"Julian", "1155662233");
        String nombreBuscado = "Julian";

        List<Entrenador> entrenadores = Arrays.asList(entrenador1);
        when(entrenadorRepository.findByNombreContainingIgnoreCase(nombreBuscado)).thenReturn(entrenadores);

        List<EntrenadorResponseDTO> resultado = entrenadorService.buscarEntrenadoresPorNombre(nombreBuscado);

        assertNotNull(resultado);
        assertEquals(entrenadores.size(), resultado.size());
        assertEquals(entrenador1.getId(), resultado.getFirst().getId());
        assertEquals(entrenador1.getNombre(), resultado.getFirst().getNombre());
        assertEquals(entrenador1.getContacto(), resultado.getFirst().getContacto());

        verify(entrenadorRepository).findByNombreContainingIgnoreCase(nombreBuscado);
    }

    @Test
    @DisplayName("Busca entrenadores por Nombre (NO Encuentra)")
    void buscarEntrenadoresInexistentesPorNombre(){
        String nombreBuscado = "Julian";

        when(entrenadorRepository.findByNombreContainingIgnoreCase(nombreBuscado)).thenReturn(Collections.emptyList());

        List<EntrenadorResponseDTO> resultado = entrenadorService.buscarEntrenadoresPorNombre(nombreBuscado);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(entrenadorRepository).findByNombreContainingIgnoreCase(nombreBuscado);
    }

    @Test
    @DisplayName("Actualizar entrenador existente (OK)")
    void actualizarEntrenadorExistente(){
        Integer id = 1;
        Entrenador entrenador = crearEntrenador(id,"Julian", "1155662233");
        String nombreAnterior = entrenador.getNombre();
        String contactoAnterior = entrenador.getContacto();

        EntrenadorRequestDTO entrenadorActualizado = new EntrenadorRequestDTO("Julián Agustín", "1179461382");

        when(entrenadorRepository.findById(id)).thenReturn(Optional.of(entrenador));
        when(entrenadorRepository.findByContacto(entrenadorActualizado.getContacto())).thenReturn(Optional.empty());
        when(entrenadorRepository.save(any(Entrenador.class))).thenAnswer(s -> s.getArgument(0));

        EntrenadorResponseDTO resultado = entrenadorService.actualizarEntrenador(id, entrenadorActualizado);

        assertNotNull(resultado);
        assertEquals(entrenador.getId(), resultado.getId());
        assertNotEquals(nombreAnterior, resultado.getNombre());
        assertNotEquals(contactoAnterior, resultado.getContacto());

        verify(entrenadorRepository).findById(id);
        verify(entrenadorRepository).findByContacto(entrenadorActualizado.getContacto());
        verify(entrenadorRepository).save(any(Entrenador.class));
        verify(entrenadorMapper).updateFromRequest(entrenadorActualizado, entrenador);
    }

    @Test
    @DisplayName("Actualizar entrenador manteniendo el mismo Contacto (NO busca duplicados)")
    void actualizarEntrenadorExistenteMismoNombre(){
        Entrenador entrenador = crearEntrenador(1,"Julian", "1155662233");
        String nombreAnterior = entrenador.getNombre();

        Integer id = 1;
        EntrenadorRequestDTO entrenadorActualizado = new EntrenadorRequestDTO("Julián", "1155662233");

        when(entrenadorRepository.findById(id)).thenReturn(Optional.of(entrenador));
        when(entrenadorRepository.save(any(Entrenador.class))).thenAnswer(s -> s.getArgument(0));

        EntrenadorResponseDTO resultado = entrenadorService.actualizarEntrenador(id, entrenadorActualizado);

        assertNotNull(resultado);
        assertEquals(entrenador.getId(), resultado.getId());
        assertEquals(entrenador.getContacto(), resultado.getContacto());
        assertNotEquals(nombreAnterior, resultado.getNombre());

        verify(entrenadorRepository).findById(id);
        verify(entrenadorRepository, never()).findByContacto(any(String.class));
        verify(entrenadorRepository).save(any(Entrenador.class));
        verify(entrenadorMapper).updateFromRequest(entrenadorActualizado, entrenador);
    }

    @Test
    @DisplayName("Actualizar entrenador con ID no existente (FALLA)")
    void actualizarEntrenadorInexistente(){
        Integer id = 99;
        EntrenadorRequestDTO entrenador = new EntrenadorRequestDTO("Julián", "1155662233");

        when(entrenadorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> entrenadorService.actualizarEntrenador(id, entrenador));

        verify(entrenadorRepository).findById(id);
        verify(entrenadorRepository, never()).findByContacto(any(String.class));
        verify(entrenadorMapper, never()).updateFromRequest(any(), any());
        verify(entrenadorRepository, never()).save(any(Entrenador.class));
    }

    @Test
    @DisplayName("Actualizar entrenador existente con Contacto ya utilizado (FALLA)")
    void actualizarEntrenadorExistenteConContactoEnUso(){
        Entrenador entrenador1 = crearEntrenador(1,"Julian", "1155662233");
        Entrenador entrenador2 = crearEntrenador(2, "Florencia", "1185632146");

        Integer id = 1;
        EntrenadorRequestDTO entrenadorActualizado = new EntrenadorRequestDTO("Julian", "1185632146");

        when(entrenadorRepository.findById(id)).thenReturn(Optional.of(entrenador1));
        when(entrenadorRepository.findByContacto(entrenadorActualizado.getContacto())).thenReturn(Optional.of(entrenador2));

        assertThrows(BusinessRuleException.class, () -> entrenadorService.actualizarEntrenador(id, entrenadorActualizado));

        verify(entrenadorRepository).findById(id);
        verify(entrenadorRepository).findByContacto(entrenadorActualizado.getContacto());
        verify(entrenadorRepository, never()).save(any(Entrenador.class));
        verify(entrenadorMapper, never()).updateFromRequest(any(), any());
    }

    @Test
    @DisplayName("Eliminar Entrenador según ID (OK)")
    void eliminarEntrenadorPorId(){
        Integer id = 1;

        when(entrenadorRepository.existsById(id)).thenReturn(true);
        when(claseRepository.existsByEntrenadorId(id)).thenReturn(false);

        entrenadorService.eliminarEntrenador(id);

        verify(entrenadorRepository).existsById(id);
        verify(claseRepository).existsByEntrenadorId(id);
        verify(entrenadorRepository).deleteById(id);
    }

    @Test
    @DisplayName("Eliminar Entrenador según ID Inexistente (FALLA)")
    void  eliminarEntrenadorConIdInexistente(){
        Integer id = 99;

        when(entrenadorRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> entrenadorService.eliminarEntrenador(id));

        verify(entrenadorRepository).existsById(id);
        verify(claseRepository, never()).existsByEntrenadorId(id);
        verify(entrenadorRepository, never()).deleteById(id);
    }

    @Test
    @DisplayName("Eliminar Entrenador según ID con Clases asociadas (FALLA)")
    void eliminarEntrenadorConClasesAsociadas() {
        Integer id = 1;

        when(entrenadorRepository.existsById(id)).thenReturn(true);
        when(claseRepository.existsByEntrenadorId(id)).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> entrenadorService.eliminarEntrenador(id));

        verify(entrenadorRepository).existsById(id);
        verify(claseRepository).existsByEntrenadorId(id);
        verify(entrenadorRepository, never()).deleteById(id);
    }

    private Entrenador crearEntrenador(Integer id, String nombre, String contacto) {
        Entrenador entrenador = new Entrenador();
        entrenador.setId(id);
        entrenador.setNombre(nombre);
        entrenador.setContacto(contacto);

        return entrenador;
    }

}