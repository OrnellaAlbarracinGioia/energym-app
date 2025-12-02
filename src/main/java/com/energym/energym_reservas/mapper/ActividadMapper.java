package com.energym.energym_reservas.mapper;

import com.energym.energym_reservas.dto.request.ActividadRequestDTO;
import com.energym.energym_reservas.dto.response.ActividadResponseDTO;
import com.energym.energym_reservas.entity.Actividad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActividadMapper {

    @Mapping(target = "id", ignore = true)
    Actividad toEntity (ActividadRequestDTO request);

    ActividadResponseDTO toResponseDTO(Actividad actividad);

    List<ActividadResponseDTO> toResponseList(List<Actividad> actividades);
}
