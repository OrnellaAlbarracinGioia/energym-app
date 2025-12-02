package com.energym.energym_reservas.mapper;

import com.energym.energym_reservas.dto.request.ClaseScheduleRequestDTO;
import com.energym.energym_reservas.dto.response.ClaseResponseDTO;
import com.energym.energym_reservas.entity.Clase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClaseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actividad", ignore = true)
    @Mapping(target = "personalizada", ignore = true)
    @Mapping(target = "sucursal", ignore = true)
    @Mapping(target = "entrenador", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    Clase toEntity (ClaseScheduleRequestDTO request);

    @Mapping(target = "actividadId", source = "actividad.id")
    @Mapping(target = "actividadNombre", source = "actividad.nombre")
    @Mapping(target = "entrenadorId", source = "entrenador.id")
    @Mapping(target = "entrenadorNombre", source = "entrenador.nombre")
    @Mapping(target = "sucursalId", source = "sucursal.id")
    @Mapping(target = "sucursalNombre", source = "sucursal.nombre")
    @Mapping(target = "cuposDisponibles", ignore = true)
    ClaseResponseDTO toResponseDTO (Clase clase);

    List<ClaseResponseDTO> toResponseList (List<Clase> clases);
}
