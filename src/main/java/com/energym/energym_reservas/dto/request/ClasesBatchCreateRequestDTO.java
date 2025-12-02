package com.energym.energym_reservas.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClasesBatchCreateRequestDTO {

    @NotNull(message = "Debe proporcionar una actividad")
    private Integer actividadId;

    //Si existiera la capa de seguridad con Autenticación el usuario que esté queriendo cargar las clases,
    //se le detectaría la sucursal específica sin tener que pedirla
    @NotNull(message = "Debe proporcionar una sucursal")
    private Integer sucursalId;

    @Valid
    @NotNull(message = "Debe incluir al menos una Clase dictada")
    private List<ClaseScheduleRequestDTO> clases;

}

