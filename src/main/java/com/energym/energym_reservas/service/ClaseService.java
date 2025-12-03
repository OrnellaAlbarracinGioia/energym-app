package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.request.ClaseScheduleRequestDTO;
import com.energym.energym_reservas.dto.request.ClasesBatchCreateRequestDTO;
import com.energym.energym_reservas.dto.response.ClaseResponseDTO;
import com.energym.energym_reservas.entity.Actividad;
import com.energym.energym_reservas.entity.Clase;
import com.energym.energym_reservas.entity.Entrenador;
import com.energym.energym_reservas.entity.Sucursal;
import com.energym.energym_reservas.exception.BusinessRuleException;
import com.energym.energym_reservas.exception.ResourceNotFoundException;
import com.energym.energym_reservas.mapper.ClaseMapper;
import com.energym.energym_reservas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ClaseService {

    private final ClaseRepository claseRepository;
    private final ActividadRepository actividadRepository;
    private final EntrenadorRepository entrenadorRepository;
    private final ReservaRepository reservaRepository;
    private final SucursalRepository sucursalRepository;
    private final ClaseMapper claseMapper;

    /*
     * Crear una o muchas clases
     */
    public List<ClaseResponseDTO> crearClases(ClasesBatchCreateRequestDTO request) {

        // Validar que la actividad existe
        Actividad actividad = actividadRepository.findById(request.getActividadId())
                .orElseThrow(() -> new ResourceNotFoundException("Actividad", "id", request.getActividadId()));

        // Validar que la sucursal existe
        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal", "id", request.getSucursalId()));

        List<Clase> clasesACrear = new ArrayList<>();
        for (ClaseScheduleRequestDTO claseSchedule : request.getClases()) {
            Entrenador entrenador = entrenadorRepository.findById(claseSchedule.getEntrenadorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Entrenador", "id", claseSchedule.getEntrenadorId()));

            Clase clase = claseMapper.toEntity(claseSchedule);
            clase.setActividad(actividad);
            clase.setSucursal(sucursal);
            clase.setEntrenador(entrenador);
            clasesACrear.add(clase);
        }
        List<Clase> savedClases = claseRepository.saveAll(clasesACrear);
        return claseMapper.toResponseList(savedClases);
    }

    /*
     * Obtener todas las Clases
     */
    @Transactional(readOnly = true)
    public List<ClaseResponseDTO> obtenerClases() {
        List<Clase> clases = claseRepository.findAll();
        return claseMapper.toResponseList(clases);
    }

    /*
     * Obtener clase por ID
     */
    @Transactional(readOnly = true)
    public ClaseResponseDTO obtenerClasePorId(Integer id) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clase", "id", id));
        return claseMapper.toResponseDTO(clase);
    }

    /*
     * Eliminar clase por ID
     */
    public void eliminarClase(Integer id) {
        if(!claseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Clase", "id", id);
        }

        boolean tieneReservas = reservaRepository.existsByClaseId(id);
        if(tieneReservas) {
            throw new BusinessRuleException("Una clase no puede ser eliminada ya que contiene reservas activas");
        }

        claseRepository.deleteById(id);
    }
}