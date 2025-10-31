package com.energym.energym_reservas.service;

import com.energym.energym_reservas.dto.ClaseDTO;
import com.energym.energym_reservas.dto.ClasesCreateDTO;
import com.energym.energym_reservas.dto.ClasesCreateRequestDTO;
import com.energym.energym_reservas.entity.Actividad;
import com.energym.energym_reservas.entity.Clase;
import com.energym.energym_reservas.entity.Entrenador;
import com.energym.energym_reservas.entity.Sucursal;
import com.energym.energym_reservas.repository.ClaseRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClaseService {
    private ClaseRepository claseRepository;


    public List<ClaseDTO> createClases(@Valid ClasesCreateRequestDTO clasesCreateRequestDTO) {

        List<Clase> clases = new ArrayList<>();

        List<ClasesCreateDTO> clasesDTO = clasesCreateRequestDTO.getClases();

        for (ClasesCreateDTO c : clasesDTO) {
            Clase clase = Clase.builder()
                    .actividad(Actividad.builder().id(clasesCreateRequestDTO.getActividadId()).build())
                    .sucursal(Sucursal.builder().id(clasesCreateRequestDTO.getSucursalId()).build())
                    .entrenador(Entrenador.builder().id(c.getEntrenadorId()).build())
                    .fecha(c.getFecha())
                    .horario(c.getHorario())
                    .capacidadMaxima(c.getCapacidadMaxima())
                    .build();
            clases.add(clase);
        }

        List<Clase> clasesGuardadas = claseRepository.saveAll(clases);
        return convertToDTO(clasesGuardadas);
    }

    private List<ClaseDTO> convertToDTO(List<Clase> clases) {

        List<ClaseDTO> clasesDTO = new ArrayList<>();

        for(Clase c :clases ){

            ClaseDTO claseDTO = ClaseDTO.builder()
                    .id(c.getId())
                    .actividad(c.getActividad())
                    .sucursal(c.getSucursal())
                    .entrenador(c.getEntrenador())
                    .fecha(c.getFecha())
                    .capacidadMaxima(c.getCapacidadMaxima())
                    .horario(c.getHorario())
                    .build();
            clasesDTO.add(claseDTO);
        }
        return clasesDTO;
    }
}
