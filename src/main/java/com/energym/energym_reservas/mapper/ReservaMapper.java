package com.energym.energym_reservas.mapper;

import com.energym.energym_reservas.dto.request.ReservaRequestDTO;
import com.energym.energym_reservas.dto.response.ReservaResponseDTO;
import com.energym.energym_reservas.entity.Reserva;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "socio", ignore = true)
    @Mapping(target = "clase", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaCancelacion", ignore = true)
    @Mapping(target = "estado", constant = "CONFIRMADA")
    Reserva toEntity (ReservaRequestDTO request);

    @Mapping(target = "socioId", source = "socio.id")
    @Mapping(target = "socioNombre", source = "socio.nombre")
    @Mapping(target = "claseId", source = "clase.id")
    @Mapping(target = "actividadNombre", source = "clase.actividad.nombre")
    @Mapping(target = "horarioClase", source = "clase.horario")
    ReservaResponseDTO toResponseDTO (Reserva reserva);

    List<ReservaResponseDTO> toResponseList (List<Reserva> reservas);
}
