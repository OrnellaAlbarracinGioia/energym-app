package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.ClaseDTO;
import com.energym.energym_reservas.entity.Clase;
import com.energym.energym_reservas.entity.Entrenador;
import com.energym.energym_reservas.entity.Sucursal;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.repository.ClaseRepository;
import com.energym.energym_reservas.repository.EntrenadorRepository;
import com.energym.energym_reservas.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClaseService {

    private final ClaseRepository claseRepository;
    private final EntrenadorRepository entrenadorRepository;
    private final SucursalRepository sucursalRepository;

    /**
     * Obtener todas las clases
     */
    @Transactional(readOnly = true)
    public List<ClaseDTO> getAllClases() {
        return claseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener una clase por ID
     */
    @Transactional(readOnly = true)
    public ClaseDTO getClaseById(Integer id) {

        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clase no encontrada con id: " + id));
        return convertToDTO(clase);
    }

    /**
     * Crear una nueva clase
     */
    @Transactional
    public ClaseDTO createClase(ClaseDTO claseDTO) {
        // Validar que el entrenador existe
        Entrenador entrenador = entrenadorRepository.findById(claseDTO.getEntrenadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado con id: " + claseDTO.getEntrenadorId()));

        // Validar que la sucursal existe
        Sucursal sucursal = sucursalRepository.findById(claseDTO.getSucursalId())
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con id: " + claseDTO.getSucursalId()));

        // Crear la entidad
        Clase clase = Clase.builder()
                .nombre(claseDTO.getNombre())
                .capacidadMaxima(claseDTO.getCapacidadMaxima())
                .dia(claseDTO.getDia())
                .horario(claseDTO.getHorario())
                .descripcion(claseDTO.getDescripcion())
                .duracionMinutos(claseDTO.getDuracionMinutos())
                .entrenador(entrenador)
                .sucursal(sucursal)
                .build();

        Clase savedClase = claseRepository.save(clase);
        return convertToDTO(savedClase);
    }

    /**
     * Actualizar una clase existente
     */
    @Transactional
    public ClaseDTO updateClase(Integer id, ClaseDTO claseDTO) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clase no encontrada con id: " + id));

        // Actualizar campos básicos
        clase.setNombre(claseDTO.getNombre());
        clase.setCapacidadMaxima(claseDTO.getCapacidadMaxima());
        clase.setDia(claseDTO.getDia());
        clase.setHorario(claseDTO.getHorario());
        clase.setDescripcion(claseDTO.getDescripcion());
        clase.setDuracionMinutos(claseDTO.getDuracionMinutos());

        // Actualizar entrenador si cambió
        if (claseDTO.getEntrenadorId() != null &&
                !claseDTO.getEntrenadorId().equals(clase.getEntrenador().getId())) {
            Entrenador entrenador = entrenadorRepository.findById(claseDTO.getEntrenadorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado"));
            clase.setEntrenador(entrenador);
        }

        // Actualizar sucursal si cambió
        if (claseDTO.getSucursalId() != null &&
                !claseDTO.getSucursalId().equals(clase.getSucursal().getId())) {
            Sucursal sucursal = sucursalRepository.findById(claseDTO.getSucursalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada"));
            clase.setSucursal(sucursal);
        }

        Clase updatedClase = claseRepository.save(clase);
        return convertToDTO(updatedClase);
    }

    /**
     * Eliminar una clase
     */
    @Transactional
    public void deleteClase(Integer id) {
        if (!claseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Clase no encontrada con id: " + id);
        }
        claseRepository.deleteById(id);
    }

    /**
     * Obtener clases por sucursal
     */
    @Transactional(readOnly = true)
    public List<ClaseDTO> getClasesBySucursal(Integer sucursalId) {
        return claseRepository.findBySucursalId(sucursalId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertir entidad a DTO
     */
    private ClaseDTO convertToDTO(Clase clase) {
        return ClaseDTO.builder()
                .id(clase.getId())
                .nombre(clase.getNombre())
                .capacidadMaxima(clase.getCapacidadMaxima())
                .dia(clase.getDia())
                .horario(clase.getHorario())
                .descripcion(clase.getDescripcion())
                .duracionMinutos(clase.getDuracionMinutos())
                .entrenadorId(clase.getEntrenador().getId())
                .entrenadorNombre(clase.getEntrenador().getNombre())
                .sucursalId(clase.getSucursal().getId())
                .sucursalNombre(clase.getSucursal().getNombre())
                .cuposDisponibles(clase.getCuposDisponibles())
                .build();
    }
}
