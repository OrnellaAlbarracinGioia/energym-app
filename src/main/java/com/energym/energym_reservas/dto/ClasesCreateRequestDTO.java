package com.energym.energym_reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClasesCreateRequestDTO {

    private Integer actividadId;
    private List<ClasesCreateDTO> clases;

    //Si existiera la capa de seguridad con Autenticación el usuario que esté queriendo cargar las clases,
    //se le detectaría la sucursal específica sin tener que pedirla
    private Integer sucursalId;

}
