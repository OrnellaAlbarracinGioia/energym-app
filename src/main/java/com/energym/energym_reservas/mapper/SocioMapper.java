package com.energym.energym_reservas.mapper;

import com.energym.energym_reservas.dto.request.SocioRequestDTO;
import com.energym.energym_reservas.dto.response.SocioResponseDTO;
import com.energym.energym_reservas.entity.Socio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SocioMapper {

    @Mapping(target = "id", ignore = true) // Se genera automático
    @Mapping(target = "fechaRegistro", ignore = true) // Se genera en PrePersist
    @Mapping(target = "activo", constant = "true") // Por defecto true
    @Mapping(target = "clasesPersonalizadas", constant = "0") // Inicia en 0
    @Mapping(target = "reservas", ignore = true) // Lista vacía al inicio
    Socio toEntity(SocioRequestDTO request);

    SocioResponseDTO toResponseDTO(Socio socio);

    List<SocioResponseDTO> toResponseList(List<Socio> socios);
}
