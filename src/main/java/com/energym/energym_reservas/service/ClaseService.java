package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.ClaseDTO;
import com.energym.energym_reservas.dto.ClasesCreateDTO;
import com.energym.energym_reservas.dto.ClasesCreateRequestDTO;
import com.energym.energym_reservas.dto.ReservaDTO;
import com.energym.energym_reservas.entity.Actividad;
import com.energym.energym_reservas.entity.Clase;
import com.energym.energym_reservas.entity.Entrenador;
import com.energym.energym_reservas.entity.Sucursal;
import com.energym.energym_reservas.repository.ActividadRepository;
import com.energym.energym_reservas.repository.ClaseRepository;
import com.energym.energym_reservas.repository.EntrenadorRepository;
import com.energym.energym_reservas.repository.SucursalRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClaseService {

    private final ClaseRepository claseRepository;
    private final ActividadRepository actividadRepository;
    private final EntrenadorRepository entrenadorRepository;
    private final SucursalRepository sucursalRepository;


    public List<ClaseDTO> createClases(ClasesCreateRequestDTO clasesCreateRequestDTO) {

        // Validar que la actividad existe
        Actividad actividad = actividadRepository.findById(clasesCreateRequestDTO.getActividadId())
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        // Validar que la sucursal existe
        Sucursal sucursal = sucursalRepository.findById(clasesCreateRequestDTO.getSucursalId())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        List<ClasesCreateDTO> clasesCreateDTO = clasesCreateRequestDTO.getClases();

        List<ClaseDTO> clasesDTO = new ArrayList<>();
        for (ClasesCreateDTO c : clasesCreateDTO) {
            Entrenador entrenador = entrenadorRepository.findById(c.getEntrenadorId())
                    .orElseThrow(() -> new RuntimeException("Entrenador no encontrado"));

            Clase clase = Clase.builder()
                    .actividad(actividad)
                    .sucursal(sucursal)
                    .entrenador(entrenador)
                    .fecha(c.getFecha())
                    .horario(c.getHorario())
                    .capacidadMaxima(c.getCapacidadMaxima())
                    .build();
            Clase claseGuardada = claseRepository.save(clase);
            ClaseDTO claseDTO = convertirADTO(claseGuardada);
            clasesDTO.add(claseDTO);
        }
        return clasesDTO;
    }

    @Transactional(readOnly = true)
    public List<ClaseDTO> obtenerClases() {
        return claseRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private ClaseDTO convertirADTO(Clase clase) {
        return ClaseDTO.builder()
                .id(clase.getId())
                .actividadId(clase.getActividad().getId())
                .sucursalId(clase.getSucursal().getId())
                .entrenadorId(clase.getEntrenador().getId())
                .fecha(clase.getFecha())
                .capacidadMaxima(clase.getCapacidadMaxima())
                .horario(clase.getHorario())
                .build();
    }
}