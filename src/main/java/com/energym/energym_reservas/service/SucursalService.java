package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.SucursalDTO;
import com.energym.energym_reservas.entity.Sucursal;
import com.energym.energym_reservas.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SucursalService {
    
    private final SucursalRepository sucursalRepository;
    
    // CREATE
    public SucursalDTO crearSucursal(SucursalDTO sucursalDTO) {
        // Validar que no exista una sucursal con el mismo nombre
        if (sucursalRepository.findByNombre(sucursalDTO.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe una sucursal con ese nombre");
        }
        
        Sucursal sucursal = Sucursal.builder()
                .nombre(sucursalDTO.getNombre())
                .direccion(sucursalDTO.getDireccion())
                .build();
        
        Sucursal sucursalGuardada = sucursalRepository.save(sucursal);
        return convertirADTO(sucursalGuardada);
    }
    
    // READ - Obtener todas las sucursales
    @Transactional(readOnly = true)
    public List<SucursalDTO> obtenerTodasLasSucursales() {
        return sucursalRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // READ - Obtener sucursal por ID
    @Transactional(readOnly = true)
    public SucursalDTO obtenerSucursalPorId(Integer id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con id: " + id));
        return convertirADTO(sucursal);
    }
    
    // READ - Buscar sucursales por nombre
    @Transactional(readOnly = true)
    public List<SucursalDTO> buscarSucursalesPorNombre(String nombre) {
        return sucursalRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // READ - Buscar sucursales por dirección
    @Transactional(readOnly = true)
    public List<SucursalDTO> buscarSucursalesPorDireccion(String direccion) {
        return sucursalRepository.findByDireccionContainingIgnoreCase(direccion).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // UPDATE - Actualizar sucursal
    public SucursalDTO actualizarSucursal(Integer id, SucursalDTO sucursalDTO) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con id: " + id));
        
        // Validar que el nombre no esté en uso por otra sucursal
        if (sucursalDTO.getNombre() != null && 
            !sucursalDTO.getNombre().equals(sucursal.getNombre())) {
            sucursalRepository.findByNombre(sucursalDTO.getNombre())
                    .ifPresent(s -> {
                        if (!s.getId().equals(id)) {
                            throw new RuntimeException("Ya existe una sucursal con ese nombre");
                        }
                    });
        }
        
        if (sucursalDTO.getNombre() != null) {
            sucursal.setNombre(sucursalDTO.getNombre());
        }
        
        if (sucursalDTO.getDireccion() != null) {
            sucursal.setDireccion(sucursalDTO.getDireccion());
        }
        
        Sucursal sucursalActualizada = sucursalRepository.save(sucursal);
        return convertirADTO(sucursalActualizada);
    }
    
    // DELETE
    public void eliminarSucursal(Integer id) {
        if (!sucursalRepository.existsById(id)) {
            throw new RuntimeException("Sucursal no encontrada con id: " + id);
        }
        sucursalRepository.deleteById(id);
    }
    
    // Metodo auxiliar para convertir entidad a DTO
    private SucursalDTO convertirADTO(Sucursal sucursal) {
        return SucursalDTO.builder()
                .id(sucursal.getId())
                .nombre(sucursal.getNombre())
                .direccion(sucursal.getDireccion())
                .build();
    }
}
