package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.ActividadDTO;
import com.energym.energym_reservas.entity.Actividad;
import com.energym.energym_reservas.entity.Entrenador;
import com.energym.energym_reservas.entity.Sucursal;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.repository.ActividadRepository;
import com.energym.energym_reservas.repository.EntrenadorRepository;
import com.energym.energym_reservas.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final EntrenadorRepository entrenadorRepository;
    private final SucursalRepository sucursalRepository;

    /**
     * Obtener todas las actividades
     */
    @Transactional(readOnly = true)
    public List<ActividadDTO> getAllActividades() {
        return actividadRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener una actividad por ID
     */
    @Transactional(readOnly = true)
    public ActividadDTO getActividadById(Integer id) {

        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con id: " + id));
        return convertToDTO(actividad);
    }

    /**
     * Crear una nueva actividad
     */
    @Transactional
    public ActividadDTO createActividad(ActividadDTO actividadDTO) {

        // Crear la entidad
        Actividad actividad = Actividad.builder()
                .nombre(actividadDTO.getNombre())
                .descripcion(actividadDTO.getDescripcion())
                .duracionMinutos(actividadDTO.getDuracionMinutos())
                .build();

        Actividad savedActividad = actividadRepository.save(actividad);
        return convertToDTO(savedActividad);
    }

    /**
     * Actualizar una actividad existente
     */
    @Transactional
    public ActividadDTO updateActividad(Integer id, ActividadDTO actividadDTO) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con id: " + id));

        // Actualizar campos básicos
        actividad.setNombre(actividadDTO.getNombre());

        actividad.setDescripcion(actividadDTO.getDescripcion());
        actividad.setDuracionMinutos(actividadDTO.getDuracionMinutos());


        Actividad updatedActividad = actividadRepository.save(actividad);
        return convertToDTO(updatedActividad);
    }

    /**
     * Eliminar una actividad
     */
    @Transactional
    public void deleteActividad(Integer id) {
        if (!actividadRepository.existsById(id)) {
            throw new ResourceNotFoundException("Actividad no encontrada con id: " + id);
        }
        actividadRepository.deleteById(id);
    }

    /**
     * Convertir entidad a DTO
     */
    private ActividadDTO convertToDTO(Actividad actividad) {
        return ActividadDTO.builder()
                .id(actividad.getId())
                .nombre(actividad.getNombre())
                .descripcion(actividad.getDescripcion())
                .duracionMinutos(actividad.getDuracionMinutos())
                .build();
    }
}
