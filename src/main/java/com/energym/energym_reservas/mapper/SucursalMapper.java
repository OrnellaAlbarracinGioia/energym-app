package com.energym.energym_reservas.mapper;

import com.energym.energym_reservas.dto.request.SucursalRequestDTO;
import com.energym.energym_reservas.dto.response.SucursalResponseDTO;
import com.energym.energym_reservas.entity.Sucursal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SucursalMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clases", ignore = true)
    Sucursal toEntity (SucursalRequestDTO request);

    SucursalResponseDTO toResponseDTO(Sucursal sucursal);

    List<SucursalResponseDTO> toResponseList(List<Sucursal> sucursales);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clases", ignore = true)
    void updateFromRequest(SucursalRequestDTO request, @MappingTarget Sucursal sucursal);
}
