package com.energym.energym_reservas.mapper;

import com.energym.energym_reservas.dto.request.EntrenadorRequestDTO;
import com.energym.energym_reservas.dto.response.EntrenadorResponseDTO;
import com.energym.energym_reservas.entity.Entrenador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntrenadorMapper {

    @Mapping(target = "id", ignore = true)
    Entrenador toEntity(EntrenadorRequestDTO request);

    EntrenadorResponseDTO toResponseDTO(Entrenador entrenador);

    List<EntrenadorResponseDTO> toResponseList(List<Entrenador> entrenadores);
}
