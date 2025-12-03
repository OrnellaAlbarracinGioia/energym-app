package com.energym.energym_reservas.mapper;

import com.energym.energym_reservas.dto.request.SocioRequestDTO;
import com.energym.energym_reservas.dto.response.SocioResponseDTO;
import com.energym.energym_reservas.entity.Socio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SocioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "clasesPersonalizadas", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    Socio toEntity(SocioRequestDTO request);

    SocioResponseDTO toResponseDTO(Socio socio);

    List<SocioResponseDTO> toResponseList(List<Socio> socios);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "clasesPersonalizadas", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    @Mapping(target = "activo", ignore = true)
    void updateFromRequest(SocioRequestDTO request, @MappingTarget Socio socio);
}
