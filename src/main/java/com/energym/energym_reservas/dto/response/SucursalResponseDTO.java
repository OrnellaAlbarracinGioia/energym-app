package com.energym.energym_reservas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SucursalResponseDTO {
    private Integer id;
    private String nombre;
    private String direccion;
}
