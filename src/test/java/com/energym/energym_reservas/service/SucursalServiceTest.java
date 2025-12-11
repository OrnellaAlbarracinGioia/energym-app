package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.request.SucursalRequestDTO;
import com.energym.energym_reservas.dto.response.SucursalResponseDTO;
import com.energym.energym_reservas.entity.Sucursal;
import com.energym.energym_reservas.exception.BusinessRuleException;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.mapper.SucursalMapperImpl;
import com.energym.energym_reservas.repository.ClaseRepository;
import com.energym.energym_reservas.repository.SucursalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SucursalServiceTest {

    @Mock
    SucursalRepository sucursalRepository;

    @Mock
    ClaseRepository claseRepository;

    @Spy
    SucursalMapperImpl sucursalMapper = new SucursalMapperImpl();

    @InjectMocks
    SucursalService sucursalService;

    @Test
    @DisplayName("Crear una Sucursal en Sistema (OK)")
    void crearSucursalExitoso(){

        SucursalRequestDTO sucursal = new SucursalRequestDTO("Central Energym","Av. Corrientes 123");

        when(sucursalRepository.findByNombreIgnoreCase(sucursal.getNombre())).thenReturn(Optional.empty());

        when(sucursalRepository.save(any(Sucursal.class))).thenAnswer(i ->{
            Sucursal s = i.getArgument(0);
            s.setId(1);
            return s;
        });

        SucursalResponseDTO resultado = sucursalService.crearSucursal(sucursal);

        assertNotNull(resultado);
        assertEquals(sucursal.getNombre(),resultado.getNombre());
        assertEquals(sucursal.getDireccion(),resultado.getDireccion());

        verify(sucursalRepository).findByNombreIgnoreCase(sucursal.getNombre());
        verify(sucursalRepository).save(any(Sucursal.class));
    }

    @Test
    @DisplayName("Crear una Sucursal en Sistema (FALLA)")
    void crearSucursalFalla(){
        Sucursal existente = crearSucursal(null, "central energym", "Soler 886");

        SucursalRequestDTO nueva = new SucursalRequestDTO("Central Energym","Av. Corrientes 123");

        when(sucursalRepository.findByNombreIgnoreCase(
                nueva.getNombre())).thenReturn(Optional.of(existente)
        );

        assertThrows(BusinessRuleException.class, () -> sucursalService.crearSucursal(nueva));

        verify(sucursalRepository).findByNombreIgnoreCase(nueva.getNombre());
        verify(sucursalRepository, never()).save(any(Sucursal.class));
    }

    @Test
    @DisplayName("Obtener todas las Sucursales (OK)")
    void obtenerTodasLasSucursales(){
        Sucursal sucursal1 = crearSucursal(1, "Central Energym","Av. Corrientes 123");
        Sucursal sucursal2 = crearSucursal(2, "Energym Norte", "Sarmiento 1567");

        List<Sucursal> sucursales = Arrays.asList(sucursal1, sucursal2);

        when(sucursalRepository.findAll()).thenReturn(sucursales);

        List<SucursalResponseDTO> resultado = sucursalService.obtenerTodasLasSucursales();

        assertNotNull(resultado);
        assertEquals(sucursales.size(), resultado.size());
        assertEquals(sucursales.getFirst().getId(),resultado.getFirst().getId());
        assertEquals(sucursales.getFirst().getNombre(),resultado.getFirst().getNombre());
        assertEquals(sucursales.getFirst().getDireccion(),resultado.getFirst().getDireccion());

        verify(sucursalRepository).findAll();

    }

    @Test
    @DisplayName("Obtener todas las Sucursales (Vacío)")
    void obtenerTodasLasSucursalesVacio(){

        when(sucursalRepository.findAll()).thenReturn(Collections.emptyList());

        List<SucursalResponseDTO> resultado = sucursalService.obtenerTodasLasSucursales();

        assertNotNull(resultado, "La lista nunca debe ser NULL");
        assertTrue(resultado.isEmpty(), "La lista debe estar vacía");
        verify(sucursalRepository).findAll();
    }

    @Test
    @DisplayName("Obtener Sucursal por ID (OK)")
    void obtenerSucursalPorID(){

        Integer idSucursal = 1;
        Sucursal sucursal = crearSucursal(idSucursal, "Central Energym", "Av. Corrientes 123");

        when(sucursalRepository.findById(idSucursal)).thenReturn(Optional.of(sucursal));

        SucursalResponseDTO resultado = sucursalService.obtenerSucursalPorId(idSucursal);

        assertNotNull(resultado);
        assertEquals(sucursal.getId(), resultado.getId());
        assertEquals(sucursal.getNombre(),resultado.getNombre());
        assertEquals(sucursal.getDireccion(),resultado.getDireccion());

        verify(sucursalRepository).findById(idSucursal);
    }

    @Test
    @DisplayName("Obtener Sucursal por ID (NO encuentra)")
    void obtenerSucursalPorIDInexistente(){
        Integer id = 99;

        when(sucursalRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sucursalService.obtenerSucursalPorId(id));

        verify(sucursalRepository).findById(id);
        verify(sucursalMapper, never()).toResponseDTO(any(Sucursal.class));
    }

    @Test
    @DisplayName("Busca sucursales por Nombre (Encuentra)")
    void buscarSucursalesExistentesPorNombre(){
        Sucursal sucursal1 = crearSucursal(1, "Central Energym", "Av. Corrientes 123");
        Sucursal sucursal2 = crearSucursal(2, "Energym Norte", "Sarmiento 1567");

        List<Sucursal> sucursales = Arrays.asList(sucursal1, sucursal2);
        String nombreBuscado = "central";

        when(sucursalRepository.findByNombreContainingIgnoreCase(nombreBuscado)).thenReturn(sucursales);

        List<SucursalResponseDTO> sucursalesEncontradas =  sucursalService.buscarSucursalesPorNombre(nombreBuscado);

        assertNotNull(sucursalesEncontradas);
        assertEquals(sucursales.size(), sucursalesEncontradas.size());
        assertEquals(sucursal1.getId(), sucursalesEncontradas.getFirst().getId());
        assertEquals(sucursal1.getNombre(), sucursalesEncontradas.getFirst().getNombre());
        assertEquals(sucursal1.getDireccion(), sucursalesEncontradas.getFirst().getDireccion());

        verify(sucursalRepository).findByNombreContainingIgnoreCase(nombreBuscado);
    }

    @Test
    @DisplayName("Busca sucursales por Nombre (NO Encuentra)")
    void  buscarSucursalesInexistentesPorNombre(){
        String nombreBuscado = "Norte";

        when(sucursalRepository.findByNombreContainingIgnoreCase(nombreBuscado)).thenReturn(Collections.emptyList());

        List<SucursalResponseDTO> sucursalesEncontradas = sucursalService.buscarSucursalesPorNombre(nombreBuscado);

        assertNotNull(sucursalesEncontradas);
        assertTrue(sucursalesEncontradas.isEmpty());

        verify(sucursalRepository).findByNombreContainingIgnoreCase(nombreBuscado);
    }

    @Test
    @DisplayName("Busca sucursales por Dirección (Encuentra)")
    void buscarSucursalesPorDireccionExistente(){
        Sucursal sucursal = crearSucursal(1, "Central Energym", "Av. Corrientes 123");
        String nombreBuscado = "CORRIENTES";

        when(sucursalRepository.findByDireccionContainingIgnoreCase(nombreBuscado)).thenReturn(Arrays.asList(sucursal));

        List<SucursalResponseDTO> sucursalesEncontradas = sucursalService.buscarSucursalesPorDireccion(nombreBuscado);

        assertNotNull(sucursalesEncontradas);
        assertEquals(1, sucursalesEncontradas.size());
        assertEquals(sucursal.getId(), sucursalesEncontradas.getFirst().getId());

        verify(sucursalRepository).findByDireccionContainingIgnoreCase(nombreBuscado);
    }

    @Test
    @DisplayName("Busca sucursales por Dirección (NO Encuentra)")
    void buscarSucursalesPorDireccionInexistente(){
        String nombreBuscado = "Norte";

        when(sucursalRepository.findByDireccionContainingIgnoreCase(nombreBuscado)).thenReturn(Collections.emptyList());

        List<SucursalResponseDTO> sucursalesEncontradas = sucursalService.buscarSucursalesPorDireccion(nombreBuscado);

        assertNotNull(sucursalesEncontradas);
        assertTrue(sucursalesEncontradas.isEmpty());

        verify(sucursalRepository).findByDireccionContainingIgnoreCase(nombreBuscado);
    }

    @Test
    @DisplayName("Actualizar sucursal existente (OK)")
    void actualizarSucursalExistente(){
        Integer id = 1;
        Sucursal sucursal = crearSucursal(id, "Central Energym", "Av. Corrientes 123");
        String nombreAnterior = sucursal.getNombre();
        String direccionAnterior = sucursal.getDireccion();

        SucursalRequestDTO sucursalActualizada = new SucursalRequestDTO("Sede Central Energym", "Av. Corrientes 122");

        when(sucursalRepository.findById(id)).thenReturn(Optional.of(sucursal));
        when(sucursalRepository.findByNombreIgnoreCase(sucursalActualizada.getNombre())).thenReturn(Optional.empty());
        when(sucursalRepository.save(any(Sucursal.class))).thenAnswer(s -> s.getArgument(0));

        SucursalResponseDTO resultado = sucursalService.actualizarSucursal(id, sucursalActualizada);

        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(sucursal.getId(), resultado.getId(), "El ID no debería haberse modificado");
        assertNotEquals(nombreAnterior, resultado.getNombre(), "El nombre de la sucursal se debería hacer actualizado");
        assertNotEquals(direccionAnterior, resultado.getDireccion(), "La dirección de la sucursal deberia haberse modificado");

        verify(sucursalRepository).findById(id);
        verify(sucursalRepository).findByNombreIgnoreCase(sucursalActualizada.getNombre());
        verify(sucursalRepository).save(any(Sucursal.class));
        verify(sucursalMapper).updateFromRequest(sucursalActualizada, sucursal);

    }

    @Test
    @DisplayName("Actualizar sucursal manteniendo el mismo Nombre (NO busca duplicados)")
    void actualizarSucursalExistenteMismoNombre(){
        Sucursal sucursal1 = crearSucursal(1, "Central Energym", "Av. Corrientes 123");
        String direccionAnterior = sucursal1.getDireccion();

        Integer id = 1;
        SucursalRequestDTO sucursalActualizada = new SucursalRequestDTO("Central Energym", "Avenida Corriente 500");

        when(sucursalRepository.findById(id)).thenReturn(Optional.of(sucursal1));
        when(sucursalRepository.save(any(Sucursal.class))).thenAnswer(s -> s.getArgument(0));

        SucursalResponseDTO resultado = sucursalService.actualizarSucursal(id, sucursalActualizada);

        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(sucursal1.getId(), resultado.getId());
        assertEquals(sucursal1.getNombre(), resultado.getNombre());
        assertEquals(sucursal1.getDireccion(), resultado.getDireccion());
        assertNotEquals(direccionAnterior, resultado.getDireccion());

        verify(sucursalRepository).findById(id);
        verify(sucursalRepository, never()).findByNombreIgnoreCase(any(String.class));
        verify(sucursalRepository).save(any(Sucursal.class));
        verify(sucursalMapper).updateFromRequest(sucursalActualizada, sucursal1);
    }

    @Test
    @DisplayName("Actualizar sucursal con ID no existente (FALLA)")
    void actualizarSucursalInexistente(){
        Integer idInvalido = 99;
        SucursalRequestDTO sucursal = new SucursalRequestDTO("Central Energym", "Av. Corrientes 123");

        when(sucursalRepository.findById(idInvalido)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sucursalService.actualizarSucursal(idInvalido, sucursal));

        verify(sucursalRepository).findById(idInvalido);
        verify(sucursalRepository, never()).findByNombreIgnoreCase(sucursal.getNombre());
        verify(sucursalMapper, never()).updateFromRequest(any(), any());
        verify(sucursalRepository, never()).save(any(Sucursal.class));
    }

    @Test
    @DisplayName("Actualizar sucursal existente con Nombre ya utilizado (FALLA)")
    void actualizarSucursalExistenteConNombreEnUso(){
        Sucursal sucursal1 = crearSucursal(1, "Central Energym", "Av. Corrientes 123");
        Sucursal sucursal2 = crearSucursal(2, "Gym Energym", "San Fernando 2001");

        Integer idSucursal = 1;
        SucursalRequestDTO sucursalActualizada = new SucursalRequestDTO("gym Energym", "Av. Corrientes 123");

        when(sucursalRepository.findById(idSucursal)).thenReturn(Optional.of(sucursal1));
        when(sucursalRepository.findByNombreIgnoreCase(sucursalActualizada.getNombre())).thenReturn(Optional.of(sucursal2));

        Exception ex = assertThrows(BusinessRuleException.class, () -> sucursalService.actualizarSucursal(idSucursal, sucursalActualizada));
        assertTrue(ex.getMessage().contains("Ya existe una sucursal con ese nombre"));

        verify(sucursalRepository).findById(idSucursal);
        verify(sucursalRepository).findByNombreIgnoreCase(sucursalActualizada.getNombre());
        verify(sucursalMapper, never()).updateFromRequest(sucursalActualizada, sucursal1);
        verify(sucursalRepository, never()).save(any(Sucursal.class));
    }

    @Test
    @DisplayName("Eliminar Sucursal según ID (OK)")
    void eliminarSucursalPorId(){
        Integer id= 1;

        when(sucursalRepository.existsById(id)).thenReturn(true);
        when(claseRepository.existsBySucursalId(id)).thenReturn(false);

        sucursalService.eliminarSucursal(id);

        verify(sucursalRepository).existsById(id);
        verify(claseRepository).existsBySucursalId(id);
        verify(sucursalRepository).deleteById(id);
    }

    @Test
    @DisplayName("Eliminar Sucursal según ID Inexistente (FALLA)")
    void eliminarSucursalConIdInexistente(){
        Integer idInvalido = 99;
        when(sucursalRepository.existsById(idInvalido)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> sucursalService.eliminarSucursal(idInvalido));

        verify(sucursalRepository).existsById(idInvalido);
        verify(claseRepository, never()).existsBySucursalId(idInvalido);
        verify(sucursalRepository, never()).deleteById(idInvalido);
    }

    @Test
    @DisplayName("Eliminar Sucursal según ID con Clases asociadas (FALLA)")
    void eliminarSucursalConClasesAsociadas(){
        Integer id = 1;
        when(sucursalRepository.existsById(id)).thenReturn(true);
        when(claseRepository.existsBySucursalId(id)).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> sucursalService.eliminarSucursal(id));

        verify(sucursalRepository).existsById(id);
        verify(claseRepository).existsBySucursalId(id);
        verify(sucursalRepository, never()).deleteById(id);
    }

    private Sucursal crearSucursal(Integer id, String nombre, String direccion){
        Sucursal sucursal = new Sucursal();
        sucursal.setId(id);
        sucursal.setNombre(nombre);
        sucursal.setDireccion(direccion);
        return sucursal;
    }
}