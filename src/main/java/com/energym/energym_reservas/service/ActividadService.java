package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.request.ActividadRequestDTO;
import com.energym.energym_reservas.dto.response.ActividadResponseDTO;
import com.energym.energym_reservas.entity.Actividad;
import com.energym.energym_reservas.exception.BusinessRuleException;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.mapper.ActividadMapper;
import com.energym.energym_reservas.repository.ActividadRepository;
import com.energym.energym_reservas.repository.ClaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final ActividadMapper actividadMapper;
    private final ClaseRepository claseRepository;

    /**
     * Obtener todas las actividades
     */
    @Transactional(readOnly = true)
    public List<ActividadResponseDTO> obtenerTodasActividades() {
        List<Actividad> actividades = actividadRepository.findAll();
        return actividadMapper.toResponseList(actividades);
    }

    /**
     * Obtener una actividad por ID
     */
    @Transactional(readOnly = true)
    public ActividadResponseDTO obtenerActividadPorId(Integer id) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad", "id", id));

        return actividadMapper.toResponseDTO(actividad);
    }

    /**
     * Crear una nueva actividad
     */
    public ActividadResponseDTO crearActividad(ActividadRequestDTO request) {

        if(actividadRepository.findByNombreIgnoreCase(request.getNombre()).isPresent()){
            throw new BusinessRuleException("Ya existe una actividad con ese nombre");
        }

        Actividad actividad = actividadMapper.toEntity(request);
        Actividad savedActividad = actividadRepository.save(actividad);
        return actividadMapper.toResponseDTO(savedActividad);
    }

    /**
     * Actualizar una actividad existente
     */
    public ActividadResponseDTO actualizarActividad(Integer id, ActividadRequestDTO request) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad", "id", id));

        if(!actividad.getNombre().equalsIgnoreCase(request.getNombre()) &&
            actividadRepository.findByNombreIgnoreCase(request.getNombre()).isPresent()){
            throw new BusinessRuleException("Ya existe una actividad con ese nombre");
        }

        actividadMapper.updateFromRequest(request, actividad);
        Actividad savedActividad = actividadRepository.save(actividad);

        return actividadMapper.toResponseDTO(savedActividad);
    }

    /**
     * Eliminar una actividad
     */
    public void eliminarActividad(Integer id) {
        if (!actividadRepository.existsById(id)) {
            throw new ResourceNotFoundException("Actividad", "id", id);
        }

        boolean tieneClases = claseRepository.existsByActividadId(id);
        if (tieneClases) {
            throw new BusinessRuleException("No es posible eliminar la actividad porque tiene clases asociadas.");
        }
        actividadRepository.deleteById(id);
    }
}
